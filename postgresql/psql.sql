-- 
-- SQL can be run in pgadmin4 interface or psql command
-- https://www.postgresql.org/docs/current/reference.html
-- https://www.postgresql.org/docs/current/bookindex.html
-- https://www.postgresql.org/docs/current/sql.html
-- https://www.postgresql.org/docs/17/runtime-config-client.html
-- https://neon.tech/postgresql/postgresql-cheat-sheet

-- \d 
-- \d <table>

select * from users limit 10; 
select id, first_name from users limit 10; 

-- pgres organization: database > schemas > tables
-- Table schema guildlines: small/compact (use correct types), simple/understandable, representitive of the data (do not intermingle compound data to save space)
    -- efficient indexing; make queries faster; 
-- Entity > Record > Properties
-- data type (postgresql has a data type for everything), contraints, indexes; aggreate functions (sum, avg, count, max, min); group by, order by, limit, offset, distinct, where, having, join, union, intersect, except...
-- subquery command
-- transactions; normalization (reduce redundancy); 

--- --- --- --- --- 
-- https://www.postgresql.org/docs/current/datatype-numeric.html#DATATYPE-NUMERIC
-- integrs (whole numebrs, fast) vs floating point numbers (approximation decimal, fast) vs decimal numerics (exact decimal, slow)

-- alias # bytes: int2=SMALLINT, int4=INTEGER, int8=BIGINT
-- unsigned acheived with check constraints
drop table if exists ex_smallint; 
CREATE TABLE ex_smallint (
    person_name TEXT, 
    age int2
);

INSERT INTO ex_smallint (person_name, age) VALUES
('Alice', 30),
('Bob', 40),
('Charlie', 50);
SELECT * from ex_smallint; 

-- numeric types: exact precision fractions but slow 
-- alias # bytes: numeric=DECIMAL

-- casting 
select 123456789123451111115::int8; 
select 123456789123451111115::DECIMAL; 
select 12.345::DECIMAL(5, 1); -- enforce # of digits (preceision: digits #, scale: fractional portion digits)
select 1234567.123::DECIMAL(5, -2); -- precision of significant portion, scale rounds from decimal point

-- fractional types: approximate fractions but fast
-- alias # bytes: float4=REAL, float8=DOUBLE PRECISION;
DROP TABLE IF EXISTS ex_real; 
CREATE TABLE ex_real (
    sensor_name TEXT, 
    reading float4
);

-- casting
SELECT 7.0::float8 * (2.0 / 10.0) = 1.4::float8;
SELECT 7.0::float8 * (2.0 / 10.0) - 1.4::float8 < .001;
SELECT 7.0::DECIMAL * (2.0 / 10.0) = 1.4::DECIMAL;

-- measure performance
SELECT sum(num::float8 / (num + 1)) ::float8 FROM generate_series(1, 2000000) num;
SELECT sum(num::DECIMAL / (num + 1)) ::DECIMAL FROM generate_series(1, 2000000) num;

-- money type: precision of 2 decimal digits
-- NOTE: do not store currency as money type (either use integer or decimal)
DROP TABLE IF EXISTS ex_money;
CREATE TABLE ex_money (
    item_name TEXT, 
    price money
);
INSERT INTO ex_money (item_name, price) VALUES
('apple', 1.23),
('banana', 899.50),
('smarkwatch', .99),
('cherry', '$1003.45');
SELECT * FROM ex_money;

-- cast 
SELECT 199.9876::money; 
SHOW lc_monetary;
-- SET lc_monetary = 'en_GB.UTF-8';
-- doesn't convert the value only the display
SELECT 1000::money; 

-- store currency values: 
-- 1. store as lowest denomination (cents)
SELECT (100.78 * 100)::int4 as cents; 
-- 2. store as DECIMAL
SELECT (100.78)::DECIMAL(10,4) as dollars; -- depending on scale/precision needed

-- can be used for calculations, less for storing the vlaues (not much usecases for it)
-- Infinity only exist in unbounded range types
-- NaN is not very useful
SELECT 'NaN'::DECIMAL = 'NaN'::DECIMAL;
SELECT 'NaN'::DECIMAL > 100000::DECIMAL;
SELECT 'NaN'::float4; 
SELECT 'Infinity'::DECIMAL; SELECT '-Infinity'::DECIMAL; -- open ended has infinity but closed range (precision, scale) does not
SELECT 'NaN'::integer; -- not present
SELECT 'Infinity'::integer; -- not present
SELECT 'Infinity'::DECIMAL - '1'::DECIMAL = 'infinity'::DECIMAL;
SELECT 'NaN'::DECIMAL ^ 0 = 1; 

-- casting examples
SELECT pg_typeof(100::int8), pg_typeof(cast(100 as int4)); 
-- SELECT pg_typeof(integer '100'); -- decorated literal 
SELECT pg_column_size(100::int2), pg_column_size(100::int4), pg_column_size(100::int8); 
SELECT pg_column_size(9999.9999::DECIMAL), pg_column_size(99999999999.99999999999::DECIMAL); -- numeric/decimal is varying size data type

--- --- --- --- --- 
-- character types (performance is equivalent on all): fixed length, character varying, text
-- alias # bytes: char=CHARACTER, varchar=CHARACTER VARYING, text=TEXT
-- NOTE: do not use fixed-width char type (dones't provide complete enforcement, wastes space, performance drawbacks because it pads the data or truncates it)
DROP TABLE IF EXISTS ex_char;
CREATE TABLE ex_char (
    name CHAR(5), -- DON'T USE IT
    description CHARACTER VARYING(10), -- doesn't pad data, only specifies limit 
    bio TEXT -- unlimited length, internally for large text it uses TOAST (The Oversized-Attribute Storage Technique which stores large values in a separate table providing compact records)
);
INSERT INTO ex_char (name, description) VALUES
('Alice', 'abcderf'),
('Bob', 'B'),
('Charlie', 'C');

--- --- --- --- --- 
-- check constraints: enforcement for data integrity. These types of constraints are well suited for placement in the database layer.
-- containts references only the current row. 
-- modification of constraint requires to drop and recreate it (possible in a single statement).
DROP TABLE IF EXISTS ex_check;
CREATE TABLE ex_check (
    price DECIMAL CONSTRAINT price_must_be_positive CHECK (price > 0),  -- enforce portion of domain/business logic
    discount_price DECIMAL CHECK (discount_price > 0),
    abbreviation TEXT CONSTRAINT text_length_short CHECK (length(abbreviation) > 2), 
    CONSTRAINT text_length_exceeded CHECK (LENGTH(abbreviation) < 6), -- table level constraint (can be used for multiple columns, but mostly stylistic preference)
    CONSTRAINT discount_must_be_less_than_price CHECK (price > discount_price)
);
INSERT INTO ex_check (price, abbreviation) VALUES
(1.23, 'abc'),
(1.23, 'abcdefg'),
(1.23, 'abcde'),
(-1, 'def'); -- violates check constraint
INSERT INTO ex_check (price, discount_price, abbreviation) VALUES
(1.23, 1.22, 'abc'),
(1.23, 1.24, 'abcdefg');

-- domains types: user defined data types + constraints (encapsulate containt+type; communicate intent; reuse )
-- can be used on multiple tables too but can only reference a single column
-- domains can be altered and validate only new data (ignoring stale data that needs update)
CREATE DOMAIN us_postal_code as text constraint format_check CHECK (
    VALUE ~ '^\d{5}$' OR VALUE ~ '^\d{5}-\d{4}$' -- regex definition
);
DROP TABLE IF EXISTS ex_domain;
CREATE TABLE ex_domain (
    street TEXT NOT NULL,
    city TEXT NOT NULL,
    postal_code us_postal_code NOT NULL -- e.g. 05341-1234
);
INSERT INTO ex_domain (street, city, postal_code) VALUES
('123 Main St', 'Springfield', '12345'),
('456 Elm St', 'Shelbyville', '12345-6789'),
('789 Oak St', 'Capital City', '1234');


-- NULL (unknown value)
-- opt for default to be NOT NULL check constraint
CREATE TABLE ex_null (
    name TEXT NOT NULL,
    price DECIMAL NOT NULL CHECK (price > 0)
); 

-- unique constraint
CREATE TABLE ex_unique (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, -- IDENTITY is NOT NULL & UNIQUE
    product_number TEXT CONSTRAINT must_be_unique UNIQUE, -- unique constraint
    product_name TEXT UNIQUE NULLS NOT DISTINCT -- treat NULLS as unique
);
INSERT INTO ex_unique VALUES (DEFAULT, '1234', 'B'), (DEFAULT, '1234', 'A'); -- violates unique constraint
INSERT INTO ex_unique VALUES (DEFAULT, NULL, 'A'), (DEFAULT, NULL, 'B'); -- NULLs by default are distinct
INSERT INTO ex_unique VALUES (DEFAULT, 'A', NULL), (DEFAULT, 'B', NULL); -- NULL treated as equal
-- table contraint 
CREATE TABLE ex_unique_combination_unique (
    product_number TEXT, 
    product_name TEXT, 
    CONSTRAINT unique_product_number_name UNIQUE (product_number, product_name) -- unique constraint on multiple columns
);

-- EXCLUSION CONSTRAINTS
CREATE EXTENSION IF NOT EXISTS btree_gist; -- GIST index for exclusion constraint for INTEGER values (adds functionality to allow strict equality checks)
CREATE TABLE ex_exclusion (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    room_num INTEGER,
    reservation_period TSRANGE, 
    EXCLUDE USING GIST (room_num WITH =, reservation_period WITH &&) -- check for overlapping ranges (checked on insertion with all other periods in the column)
); 
INSERT INTO ex_exclusion VALUES 
    (DEFAULT, 1, '[2024-01-01 00:00:00, 2024-01-02 02:00:00)'), 
    (DEFAULT, 2, '[2024-01-01 00:00:00, 2024-01-02 02:00:00)'); 
INSERT INTO ex_exclusion VALUES 
    (DEFAULT, 2, '[2024-01-02 01:30:00, 2024-01-03 05:00:00)'); -- violates exclusion constraint

-- partial exclusion constraints
CREATE TABLE ex_exclusion_partial (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    room_num INTEGER,
    booking_status TEXT, 
    reservation_period TSRANGE, 
    EXCLUDE USING GIST (room_num WITH =, reservation_period WITH &&) WHERE (booking_status != 'cancelled')
); 
INSERT INTO ex_exclusion_partial VALUES 
    (DEFAULT, 2, 'cancelled', '[2024-01-01 00:00:00, 2024-01-02 02:00:00)'),
    (DEFAULT, 2, 'confirmed', '[2024-01-02 01:30:00, 2024-01-03 05:00:00)'); 
INSERT INTO ex_exclusion VALUES s
    (DEFAULT, 2, 'confirmed', '[2024-01-02 01:30:00, 2024-01-03 05:00:00)');

-- FOREIGN KEY CONSTRAINTS (enforces referential integrity between tables)
CREATE TABLE states (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name TEXT
);
CREATE TABLE cities (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    state_id BIGINT REFERENCES states(id),
    name TEXT
);
INSERT INTO states (name) VALUES
('Illinois'),
('California'),
('Texas');
INSERT INTO cities (state_id, name) VALUES
(1, 'Chicago'),
(1, 'Springfield'),
(2, 'Los Angeles'),
(3, 'Houston');
INSERT INTO cities (state_id, name) VALUES (5, 'New York'); -- violates foreign key constraint

CREATE TABLE cities_composite_foreign_key_and_action_on_delete (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    state_id BIGINT,
    name TEXT
    -- NO ACTION allows the check to be deffered in a transaction, while RESTRICT does not allow it
    FOREIGN KEY (id,state_id) REFERENCES states(id,id) ON DELETE NO ACTION, -- just an example
    FOREIGN KEY (id,state_id) REFERENCES states(id,id) ON DELETE RESTRICT, -- just an exampleC
    -- cascading is dangerous as it can result in deleting large number of records across tables if containts are intertwined
    -- to avoid this, use ON DELETE SET NULL or ON DELETE SET DEFAULT which can be later cleaned up
    FOREIGN KEY (id,state_id) REFERENCES states(id,id) ON DELETE CASCADE, -- just an example
);

--- --- --- --- ---
-- character sets and collations (defines behavior of comparison - relationship between characters)
-- can apply specific encoding for specific columns or per operations, in addition to the database encoding configured
\l+ -- database encoding
show client_encoding; -- client encoding of psql command. discrepancy between server and client will attempt automatic conversion
SELECT 'abc' = 'ABC' COLLATE "en_US.cutf8" AS result; -- en_US.utf8 is case sensitive
CREATE COLLATION en_us_ci (
    provider = icu,
    locale = 'en-US-u-ks-level1',
    deterministic = false -- any order comparison
);
SELECT 'abc' = 'ABC' COLLATE "en_us_ci" AS result;

--- --- --- --- --- 
-- binary data types: BYTEA 
-- e.g. storing checksums for comparisons
-- although TOAST will be applied, for storing large amount of data use a file instead 
-- there are plugins that transparently store large data in a file or move them into a CDN providers or S3 compatible storage with pointers
-- storing in database is consistent and doesn't require additional storage management (deleting remote S3 files when records are deleted)
DROP TABLE IF EXISTS ex_bytea;
CREATE TABLE ex_bytea (
    file_name TEXT,
    data BYTEA
);

-- https://www.postgresql.org/docs/17/functions-binarystring.html
show bytea_output;
INSERT INTO ex_bytea (file_name, data) VALUES
('file1', '\x1234'),
('file2', '\x5678');
SELECT * FROM ex_bytea; 
set bytea_output = 'escape'; 
SELECT * FROM ex_bytea; 
SELECT pg_typeof(md5('hello world')); -- TEXT md5 is for checksums
SELECT pg_typeof(digest('hello world', 'sha256')); -- BYTEA sha256 is for security
SELECT pg_typeof(decode(md5('hello world'), 'hex')); -- BYTEA
SELECT pg_column_size(md5('hello world')::uuid), pg_column_size(decode(md5('hello world'), 'hex')), pg_column_size(md5('hello world')); -- when stored as TEXT it is larger; uuid is the smallest representation of md5

--- --- --- --- --- 
-- uuid as a data type: 
CREATE TABLE ex_uuid (
    uuid_value UUID
);
INSERT INTO ex_uuid (uuid_value) VALUES
('123e4567-e89b-12d3-a456-426614174000'),
('123e4567-e89b-12d3-a456-426614174001');
SELECT uuid_value, pg_typeof(uuid_value), pg_column_size(uuid_value) FROM ex_uuid;
SELECT pg_column_size(uuid_value::text), pg_column_size(uuid_value) FROM ex_uuid; -- uuid stored more efficiently on-disk than TEXT or BYTEA representation
SELECT gen_random_uuid(); -- random (uuid version 4)
-- postgresql UUIDv7 extension for uuid version 7 (time-based) is a good choice for primary keys, where each piece of uuid represents different information (time, machine, etc) which makes it unique universally even across clusters.

--- --- --- --- --- 
-- Booleans data type
CREATE TABLE ex_boolean (
    is_active BOOLEAN
);
INSERT INTO ex_boolean (is_active) VALUES
(TRUE),
(FALSE),
(NULL), 
('t'), -- true
('f'), -- false
('true'), -- true
('false'), -- false
('yes'), -- true
('no'), -- false
('on'), -- true
('off'), -- false
('1'), -- true
('0'); -- false; 
SELECT * FROM ex_boolean;
SELECT 1::BOOLEAN, '1'::BOOLEAN, 1::integer; 
SELECT pg_column_size(1::BOOLEAN), pg_column_size(1::int2); -- boolean is 1 byte, integer is 2 bytes

--- --- --- --- --- 
-- Enums data type:
-- do not use an enum for constantly changing options. For that use a lookup table (through foreign key). 
-- another option instead of an enum, a checked constraint can be used (DOMAIN with CONSTRAINT on TEXT). easier for maintenance when values are change over time.
CREATE TYPE mood AS ENUM ('happy', 'sad', 'angry');
CREATE TABLE ex_enum (
    current_mood mood
);
ALTER TYPE mood ADD VALUE 'excited' before 'sad';
INSERT INTO ex_enum (current_mood) VALUES
('happy'),
('sad'),
('angry'), 
('excited');
SELECT * FROM ex_enum ORDER BY current_mood; -- orders by position in enum definition

-- alter definition
CREATE TYPE mood_new AS ENUM ('happy', 'sad', 'angry', 'neutral');
BEGIN; 
-- (NOTE: example not working, follow vid#19) 
UPDATE ex_enum 
    SET current_mood = 'neutral' 
    WHERE current_mood not in ('happy', 'sad', 'angry', 'neutral');

ALTER TABLE ex_enum
    ALTER COLUMN current_mood TYPE mood_new
    USING current_mood::text::mood_new;
COMMIT;

-- enums as underlying integer
SELECT * FROM pg_catalog.pg_enum; 
SELECT enum_range(null::mood); -- range of values defined

--- --- --- --- --- 
-- TIMESTAMP data types: timestamptz and timestamp (date-time can be: timestamp without timzone and timestamp with timezone). timezone converts to UTC in storage and on reteival converts to local timezone configured
-- always use timestamp with timezone (makes it easier to manage and convert to local timezone) of ISO 8601 format to prevent ambiguity
CREATE TABLE ex_timestamp (
    created_at timestamptz(0 - 6) -- what is provided in brackets up 6 fractional seconds to store
);
SELECT now()::timestamptz(3), now()::timestamptz(0);
SELECT date_trunc('second', now())::timestamptz; -- remove fractional seconds instead of rounding them up

-- ISO 8601 standard for date time (YYYY-MM-DDTHH:MM:SS.sss+/-00:00) format
show DateStyle; 
SET DateStyle = 'ISO, DMY'; -- defines output format    
SELECT '2024-01-01 12:00:00'::date; 
SELECT '1/3/2014'::date; -- ambiguous date format handled by DateStyle DMY (day/month/year) or MDY (month/day/year)
SELECT to_timestamp(1691376000.123); -- epoch time UNIX (always timezone of 00) to timestamptz

--- timezones
--- for sanity keep in UTC for as long as possible and convert it to the user's timezone
--- use named timezones not offsets (offsets can change due to daylight savings, some times they are neg and other times positive)
show time zone; -- client session timezone
ALTER DATABASE app SET TIME ZONE 'UTC'; -- modify postgresql timezone at database level
show config_file; -- postgresql.conf file cluster-level config
SET TIME ZONE 'America/Chicago'; 
SELECT '2024-01-01 12:00:00'::timestamptz;
SELECT '2024-01-01 11:30:00+00'::timestamptz; -- stored in UTC but when read got convered to America/Chicago timezone

-- use named timezone: use full time zone names
SET TIME ZONE 'UTC'; 
SELECT '2024-01-01 12:00:00'::timestamptz as base, pg_typeof('2024-01-01 12:00:00'::timestamptz), '2024-01-01 12:00:00'::timestamptz AT TIME ZONE 'America/Chicago' as central_america, pg_typeof('2024-01-01 12:00:00'::timestamptz AT TIME ZONE 'America/Chicago');
SELECT '2024-01-01 12:00:00'::timestamptz AT TIME ZONE '-06:00', '2024-01-01 12:00:00'::timestamptz AT TIME ZONE INTERVAL '-06:00', '2024-01-01 12:00:00'::timestamptz AT TIME ZONE '+06:00'; -- offsets using POSIX style specification; IMPOTRANT: it is opposite to ISO 8601 sign convention which is used elsewhere in postgresql ! otherwise use with interval to flip the sign conversion
SELECT * FROM pg_timezone_names WHERE NAME LIKE '%Chicago%'; -- CDT for daylight savings which would be -5 instead of -6 UT

--- --- --- --- --- 
-- date type and time type: time, timetz (time with timezone is not recommended to use as it relies on daylight times), DATE
CREATE TABLE ex_date (
    date_of_birth DATE
); 
SHOW DateStyle;
SELECT '1/3/2024'::DATE; 

CREATE TABLE ex_time (
    time_of_day TIME(0 - 6)
);
SELECT '12:01:05.1234'::TIME(1); 
SELECT now()::time, CURRENT_DATE + 1; 
SELECT pg_typeof(CURRENT_TIME); -- time with timezone; DO NOT USE TIME WITH TIMEZONE
SELECT CURRENT_TIMESTAMP; 
SELECT pg_typeof(LOCALTIME), pg_typeof(LOCALTIMESTAMP); -- time without timezone

--- --- --- --- --- 
-- interval data type (range/durations; useful for date/time overlap check queries)
show intervalstyle; 
SELECT '1 year 2 months 3 days 04:05:06'::INTERVAL; 
set intervalstyle = 'iso_8601';
SELECT '1 year 2 months 3 days 04:05:06'::INTERVAL; 
SELECT INTERVAL '1-6' YEAR TO MONTH; -- alternative syntax to declare interval
SELECT INTERVAL '6000' SECOND; 

--- --- --- --- --- 
-- serial types (not preferred to create primary keys; modern postgresql provides better options; internally represented as sequence and integer)
-- be generous with the size of the serial (BIGSERIAL i.e.g BIGINT) to prevent overflow
-- the sequence generates numbers in non transactional manner which prevents conflicts. 
CREATE TABLE ex_serial (
    id SERIAL, 
    id_big BIGSERIAL PRIMARY KEY -- internally biginteger 
); 
-- equivalent to
CREATE SEQUENCE ex_serial_sequence_internally AS integer; 
CREATE TABLE ex_serial (
    id integer NOT NULL DEFAULT nextval('ex_serial_sequence_internally')
);
ALTER SEQUENCE ex_serial_sequence_internally OWNED BY ex_serial.id;

-- use generated columns instead serial
CREATE TABLE ex_serial_gen (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
    order_number SERIAL,
    name TEXT 
); 
INSERT INTO ex_serial_gen (name) 
    VALUES ('Alice'), ('Bob'), ('Charlie') 
    RETURNING id, order_number;  -- returns autogenerated values in same query
SELECT * FROM ex_serial_gen;

-- sequence type: 
CREATE SEQUENCE seq AS BIGINT INCREMENT 1 START 10 MINVALUE 1 MAXVALUE 1000000000; 
-- currval used for local client session not value of sequence globally.
SELECT currval('seq'), nextval('seq');

-- identity columns as ids
-- this is the recommended way (BIGINT) to create auto incrementing ids in porstgres (even over uuid)
CREATE TABLE ex_id_column (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
    name TEXT
);
INSERT INTO ex_id_column (name) 
    VALUES ('Alice'), ('Bob'), ('Charlie') 
    RETURNING id;
SELECT * FROM ex_id_column;
INSERT INTO ex_id_column (id, name) VALUES (1, 'Alice'); -- error generated always cannot provide value
INSERT INTO ex_id_column (id, name) VALUES (DEFAULT, 'Alice');

INSERT INTO ex_id_column (id, name) OVERRIDING SYSTEM VALUE VALUES (16, 'Alice'); -- not recommended; will require resetting the sequence to bring it back in sync
-- reset sequence
SELECT pg_get_serial_sequence('ex_id_column', 'id'); -- public.ex_id_column_id_seq
SELECT setval('public.ex_id_column_id_seq', (SELECT max(id) from ex_id_column)); 

-- define generated with allowing user to provide values for sequence
CREATE TABLE ex_id_column_default (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY, 
    name TEXT
);
INSERT INTO ex_id_column_default (id, name) 
    VALUES (1, 'Alice'), (2, 'Bob'), (3, 'Charlie') 
    RETURNING id;

--- --- --- --- --- 
-- network address types: cidr, inet, macaddr
CREATE TABLE ex_inet (
    ip_address inet
); 
INSERT INTO ex_inet (ip_address) VALUES 
    ('192.168.1.10/4'), 
    ('10.0.0.1'), 
    ('2001:db8::ff00:42:8329/64'), 
    ('2001:db8::ff00:42:8329');
SELECT * FROM ex_inet;
SELECT pg_column_size('200:db8::ff00:42:8329'::INET), pg_column_size('200:db8::ff00:42:8329'::TEXT); -- underlying datastructure representation is more efficient than text

SELECT ip_address, 
    host(ip_address) as host,
    masklen(ip_address) as masklen,
    network(ip_address) as network,
    abbrev(ip_address) as abbrev
FROM ex_inet;

CREATE TABLE ex_cidr (
    network_address cidr
);
INSERT INTO ex_cidr (network_address) VALUES 
    ('192.168.0.0/24'), 
    ('10.0.0.0/8'), 
    ('2001:db8::/32');
SELECT * FROM ex_cidr;

SELECT pg_column_size('08:00:20:00:00:00'::MACADDR), pg_column_size('08:00:20:00:00:00'::MACADDR8); --mac address of 8 vs 6 bytes

--- --- --- --- --- 
-- JSON types: json and jsonb (use jsonb for performance and storage efficiency, and stricter validation)
-- supports 256 MB of json data (not that it is recommended to store large json data in the database)
-- nested json fields that are constantly queried is a hint to break them into top-level columns; another approach is to use generated columns to extract json data and duplicate it in top level columns
-- access patterns determine the data organization
-- if the schema is well defined and keys/fields are updated independently, then break them into separate columns; otherwise keeping the data as a blob is fine
SELECT pg_column_size('{"a":       "hellow world", "a": "no dup"}'::json) AS json_size, pg_column_size('{"a":       "hellow world", "a": "no dup"}'::jsonb) AS jsonb_size; -- jsonb is more efficient & stores compressed binary form of json not bare text

-- querying 
WITH json_data AS (
    SELECT '{
        "string": "hello world",
        "number": 123,
        "boolean": true,
        "array": [1, 2, 3],
        "object": {
            "key": "value"
        }
    }'::jsonb AS json_object
)
SELECT 
    json_object->'string' AS string_field,
    json_object->>'string' AS string_field, -- unquote the value
    json_object->'number' AS number_field,
    json_object->'array'->2 AS array_field,
    json_object->'object'->>'key' AS object_field
FROM json_data;

--- --- --- --- --- 
-- arrays (vs breaking data into table) 
CREATE TABLE ex_array (
    int_array integer[],
    text_array TEXT[],
    nested_array integer[][]
);
INSERT INTO ex_array (int_array, text_array, nested_array) VALUES
    (ARRAY[1, 2, 3], ARRAY['a', 'b', 'c'], ARRAY[ARRAY[1, 2], ARRAY[3, 4]]),
    ('{4, 5, 6}', '{d, e, f}', '{{5, 6}, {7, 8}}'); -- alternative syntax (default presentation format)
SELECT * FROM ex_array;

SELECT 
    int_array[1], -- 1-based indexing (not 0-based)
    text_array[1:3], -- slicing  
    text_array[2:] -- slicing  open ended support
FROM ex_array; 
SELECT 
    nested_array
    FROM ex_array
    WHERE nested_array[1][2] = 6; -- nested array access
SELECT 
    nested_array
    FROM ex_array
    WHERE nested_array @> ARRAY[4]; -- includes operator
SELECT unnest(nested_array) FROM ex_array;
SELECT unnest(int_array) FROM ex_array; 

WITH custom_table AS ( -- CTE (common table expression) using the result set of unnest creats a virtual table
    SELECT unnest(text_array) AS t FROM ex_array
)
SELECT * FROM custom_table WHERE t = 'b';

--- --- --- --- --- 
-- generated columns feature: reference to another column with some transformation
-- generated columns can reference current row only; an must be deterministic (no randomness in value); generated columns cannot reference other generated columns;
CREATE TABLE ex_people (
    height_cm DECIMAL,
    height_in DECIMAL GENERATED ALWAYS AS (height_cm / 2.54) STORED -- postgres doesn't support virtual columns (computed on the fly).
); 
INSERT INTO ex_people (height_cm) VALUES
(170),
(180),
(190);
SELECT * FROM ex_people;

CREATE TABLE ex_users (
    email TEXT, 
    email_domain TEXT GENERATED ALWAYS AS (split_part(email, '@', 2)) STORED
);
SELECT split_part('xyz@somewebsite.com', '@', 2); 
INSERT INTO ex_users (email) VALUES ('xyz@somewebsite.com'); 
SELECT * FROM ex_users;

--- --- --- --- --- 
-- text searching data types: tsvector and tsquery
SELECT to_tsvector('The Fox is quick Fox and the dog is lazy laziness'); -- vector format of sorted list of distinct lexemes (words) with their positions taht can be searched on 
SELECT to_tsquery('lazy'); 
SELECT to_tsvector('english', 'The Fox is quick Fox and the dog is lazy laziness') @@ to_tsquery('lazy'); -- search for lexeme in the vector

CREATE TABLE ex_text_search (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    content TEXT, 
    serach_vector TSVECTOR GENERATED ALWAYS AS (to_tsvector('english', content)) STORED -- for English content (language must be specified to make the transfomation immutable and deterministic)
);
INSERT INTO ex_text_search (content) VALUES
('The Fox is quick Fox and the dog is lazy laziness'),
('The quick brown fox jumps over the lazy dog'),
('A journey of a thousand miles begins with a single step');

SELECT * FROM ex_text_search WHERE serach_vector @@ tsquery('mile'); 

--- --- --- --- --- 
-- bit string data types (string of true/false values): bit and bit varying
-- for large pieces of information instead of creating lots of columns for boolean values; another approach is to store an integer with bitwise operations to extract the values; but bit strings deliver the intention better
SELECT B'0001', '0101'::BIT(4);
SELECT B'0101' & B'0100'; -- exmaple user's feature flags

CREATE TABLE ex_bits (
    bit3 BIT(3), 
    bitv BIT VARYING(32) -- max length of 32 bits
);
INSERT INTO ex_bits (bit3, bitv) VALUES
(B'101', B'110010101'),
(B'1', B'110010101'), -- fails
(B'101', B'110010101111111111111111111111111111111111111111111'), -- fails
(B'111', B'10101010101010101010101010101010');

--- --- --- --- --- 
-- Ranges metadata type (with bounded/unbounded lower & upper bounds): INT4RANGE, INT8RANGE, NUMRANGE, DATERANGE, TSRANGE, TSTZRANGE
-- INT4MULTIRANGE, INT8MULTIRANGE, NUMMULTIRANGE, DATEMULTIRANGE, TSMULTIRANGE, TSTZMULTIRANGE
-- e.g. checking for room reservation conflicts
SELECT '[1,5]'::int4range, -- discrete step range -- [1,6) represented internally same as [1,5]
     '[1,5]'::numrange; -- continuous range including fractions -- [1,6) is not equal to [1,5] for continuous values
SELECT '[1,5)'::numrange, numrange(1,5,'[)'), '(,)'::numrange, 'empty'::tsrange;
CREATE TABLE ex_range ( 
    int_range INT4RANGE, 
    date_range DATERANGE,
    num_range NUMRANGE, 
    ts_range TSRANGE
); 
INSERT INTO ex_range (int_range, date_range, num_range, ts_range) VALUES 
( '[2,7)', '[2022-01-01, 2022-03-01)', '[10.0, 20.0)', '[2022-01-01 00:00:00, 2022-03-01 08:00:00)' ),
( '[4,8]', '[2023-02-01, 2023-02-15)', '(1.1, 3.3)', '[2023-02-01 09:00:00, 2023-02-15 11:59:59)' ),
( '(-2,2]', '[2025-05-01, 2025-05-31]', '[100.0, 500.0]', '[2025-05-01 06:00:00, 2025-05-31 18:30:00]' ),
( '[10,12)', '[2024-06-01, 2024-07-01)', '[0.5, 1.5)', '[2024-06-01 12:00:00, 2024-07-01 23:59:59)' ); 
SELECT * FROM ex_range WHERE int_range @> 3; -- check if the range contains the value
SELECT * FROM ex_range WHERE int_range && '[5,6]'; -- check if the ranges overlap
SELECT INT4RANGE(10,20, '[]') * INT4RANGE(15,25, '[]'); -- intersection of two ranges

SELECT upper(int4range(10,20,'[]')), upper_inc(int4range(10,20,'[]')); 
SELECT upper(numrange(10,20,'[)')), upper_inc(numrange(10,20,'[)')); -- upper, upper_inc representation of continuous range
SELECT lower(int4range(10,20,'[]')), lower_inc(int4range(10,20,'[]'));

-- multirange
SELECT '{[1,5), [7,10)}'::int4multirange @> 8; -- multirange of two ranges

--- --- --- --- --- 
-- Composite types (has its infrequent usecases but mostly the alternatives are better - discrete columns, JSONB, or separate tables)
CREATE TYPE address AS ( 
    number TEXT, 
    street TEXT, 
    city TEXT,
    state TEXT,
    postal TEXT
);
SELECT ROW('123', 'Main St', 'Springfield', 'IL', '62704')::address, ('123', 'Main St', 'Springfield', 'IL', '62704')::address;

CREATE TABLE ex_composite ( 
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
    addr address 
); 
INSERT INTO ex_composite (addr) VALUES 
(('123', 'Main St', 'Springfield', 'IL', '62704')), 
(('456', 'Elm St', 'Shelbyville', 'IL', '62705'));
SELECT * FROM ex_composite; 
SELECT id, addr.number FROM ex_composite; -- ERROR! address is treated as table, use (address).number instead
SELECT id, (addr).number FROM ex_composite; 

--- --- --- --- --- 
-- INDEXES (separate b-tree table maintains a partial copy of data in addition to the pointer to origin table)
-- indexes slow down inserts/updates/deletes; 
-- Contains pointer (CTID) to the origin table (physical location) to get rest of raw data.
-- every index in postgresql is a secondary index (not applied directly to the origin table, but rather referential to the origin table)
-- Primary Key (unique, not null, automatic index) is closer to a primary index i.e. clustered index (data stored along with the index) but not exactly.
SELECT * FROM pg_indexes WHERE tablename = 'users';

-- heap table (division of storage into blocks, and offsets within the blocks)
-- CTIDs (physical location identifier; hidden system columns):
SELECT ctid from users; -- (page/block, position/offset) these physical locations susceptible to change;

-- B-Tree index (default index type; ordered tree structure)
SELECT * FROM users where name = 'xyz'; -- traverses index and retrieves data from CTID location

-- Primary key: BIGINT vs UUIDv7
-- use BIGINTs, the issue with UUIDs/ULID has many varients (UUIDv4 gen_random_uuid are random and defualt in postgresql which are not sequential and incur in postgres a minor performance hit); UUID size is larger (amortized by using uuid columns in postgresql) and has random insertion (amortized by using UUIDv7 or ULID which has a time portion incrementing sequentially). The benefit of UUIDv7/ULID is ability to generate them without coordination or centralized authority (unique across nodes). Lastly, BIGINT can result in a security vulnerability which can be resolved by generating a public id (using library like "nano-id") for public facing (e.g. URL) along side the Primary key id of the table.
-- recommendation: prefer BIGINT + secondary public id (generated by e.g. nano-id); or use time based sequential UUIDv7/ULID.

-- designing indexes requires knowledge of access patterns to the data; while designing schemas is straight forward by looking at the data types and their relationships. 
-- it is better to have a single composite index over multiple columns than having multiple indexes on each column.
-- indications of an index: WHERE, GROUP BY, ORDER BY, JOIN, SELECT need to be taken into consideration.

SELECT count(*) FROM users;  
\d users
DROP INDEX users_birthday_idx;
EXPLAIN SELECT * FROM users WHERE birthday = '1989-02-14'; -- parallel sequential scan
CREATE INDEX bday ON users USING BTREE(birthday);
EXPLAIN SELECT * FROM users WHERE birthday = '1989-02-14'; -- bitmap index scan (for strict equality)
EXPLAIN SELECT * FROM users WHERE birthday < '1989-02-14'; -- bitmap index scan (for after unbounded range)
EXPLAIN SELECT * FROM users WHERE birthday between '1989-02-14' and '1989-12-31'; -- bitmap index scan (for between unbounded range)
EXPLAIN SELECT * FROM users WHERE birthday > '1989-02-14'; -- index not used! because of index selectivity (for before unbounded range)
EXPLAIN SELECT * FROM users ORDER BY birthday; -- straight forward bitmap index scan  (as the index is already sorted)
EXPLAIN SELECT * FROM users ORDER BY created_at; -- sequential scan + sort (no index defined)
EXPLAIN SELECT count(*), birthday FROM users GROUP BY birthday; -- using index

-- index cardinality (# of distinct elemnts in column) & selectivity (# of distinct values in the column / # of rows in the table): helps determine the index benefit or if it should be used at all. 
SELECT count(DISTINCT birthday) as cardinality, (count(DISTINCT birthday)::decimal / count(*)::decimal)::decimal(7,4) as selectivity FROM users; -- calculate cardinality and selectivity 
SELECT (count(DISTINCT id)::decimal / count(*)::decimal)::decimal(7,4) as perfect_selectivity FROM users; -- 1.00 is perfect selectivity
SELECT (count(DISTINCT is_pro)::decimal / count(*)::decimal)::decimal(17,14) FROM users; -- example of poor selectivity
-- specific queries may have a better selectivity (good candidate for index) than the overall table selectivity (e.g. if data is skewed and it is the data looking for, then index can be benefitial even for low overal selectivity)
SELECT (count(*) FILTER (WHERE is_pro is true))::decimal / count(*)::decimal FROM users; -- example of skewed data; query example - show queries where ... and is_pro is true; would benefit from an index.

SELECT count(*) FROM users WHERE birthday > '1989-02-14'; -- because the data in the column affects the decision of whether to use the index or go for sequential scan (postgresql policies are dependent on the stats of the data in the column which are maintained by postgresql)

--- Composite indexes (on multiple columns): 
-- left most prefix rule followed by B-Tree; usage rules: 1. left-to-right no skipping when querting in order of index definition; 2. stops at the first range (because it starts scanning when encountering a range); 
-- should consider strict equality and ranges: most common query column (strict equality) should be first in the index definition; while ranges should be at the end of the index definition.
CREATE INDEX multi ON users USING BTREE(first_name, last_name, birthday); -- order is important 
EXPLAIN SELECT * FROM users WHERE (first_name = 'Aaron'); -- uses index
EXPLAIN SELECT * FROM users WHERE (last_name = 'Francis'); -- not using index because skipping first_name in order
EXPLAIN SELECT * FROM users WHERE (first_name = 'Aaron' AND last_name = 'Francis'); -- uses index
EXPLAIN SELECT * FROM users WHERE (first_name = 'Aaron' AND birthday = '1989-02-14'); -- uses index because postgresql optimizes query although last_name is skipped; It would first traverse using first_name and then sequentially scan to get birthday (btter than scanning the entire table). 
EXPLAIN SELECT * FROM users WHERE (first_name = 'Aaron' AND last_name = 'Francis' AND birthday = '1989-02-14'); -- uses index fully to traverse until records are reached

CREATE INDEX multi2 ON users USING BTREE(first_name, birthday);
EXPLAIN SELECT * FROM users WHERE (first_name = 'Aaron' AND birthday = '1989-02-14'); -- picks the optimal index "multi2" over "multi" as it can do more efficient traversal

CREATE INDEX first_last_birth ON users USING BTREE(first_name, last_name, birthday); 
CREATE INDEX first_birth_last ON users USING BTREE(first_name, birthday, last_name); 
EXPLAIN SELECT * FROM users WHERE (first_name = 'Aaron' AND last_name = 'Francis' AND birthday < '1989-12-31'); -- picks the optimal index with birthday at the end.

--- postgresql can combine discrete index in query execution (while less optimal than a composite index): 
CREATE INDEX "first" ON users USING BTREE(first_name); 
CREATE INDEX "last" ON users USING BTREE(last_name); 
EXPLAIN SELECT * FROM users WHERE first_name = 'Aaron' or last_name = 'Francis'; -- uses both indexes to get the result set

-- choice of index to use depends on postgresql optimization policies and the data it is working with.
CREATE INDEX first_last ON users USING BTREE(first_name, last_name); 
EXPLAIN SELECT * FROM users WHERE first_name = 'Aaron' AND last_name = 'Francis'; -- postgresql decides to use the composite index
EXPLAIN SELECT * FROM users WHERE first_name = 'Aaron' OR last_name = 'Francis'; -- postgresql decides to use separate indecies.

--- Covering index concept (where the entire data needs of the query are duplicated in the index and satisfied by it without having to retrieve data from origin table)
CREATE INDEX "first" ON users USING BTREE(first_name);
EXPLAIN SELECT * FROM users WHERE first_name = 'Aaron'; -- uses index and then lookups heap for the data using CTID
EXPLAIN SELECT first_name FROM users WHERE first_name = 'Aaron' ORDER BY first_name; -- uses purely the index (with no additional steps, everything required by the query is satisfied by that index)

-- bringing additional data to the index to statisfy more query cases
CREATE INDEX multi_plus ON users USING BTREE(first_name, last_name) INCLUDE (id); -- adds the data to the leaf nodes of the B-Tree but doesn't affect traversal
EXPLAIN SELECT first_name, last_name, id FROM users where first_name = 'Aaron' ORDER BY last_name; -- uses the index and doesn't need to go to the heap table (origin table) to get the data

-- NOTE: in a highly updated data table, covering indecies may not be hit as often as the data in the index may not be up to date (postgresql visibility rules are checked to ensure the data is not stale).

--- partial index (permits to create an index over a portion of the table based on a condition; usesful for targeting skewed data)
CREATE INDEX "email" ON users USING BTREE(email) WHERE is_pro is true; -- limits BTREE only on portion of table data
EXPLAIN SELECT * FROM users WHERE email = 'aaron.francis@example.com' -- doesn't use index
EXPLAIN SELECT * FROM users WHERE email = 'aaron.francis@example.com' and is_pro is true; -- uses index 

-- enforce partial uniqueness across the table (unique when a given condition holds)
CREATE UNIQUE INDEX email on users(email) where deleted_at is null; -- unique index for active accounts
EXPLAIN SELECT * FROM users WHERE email = 'aaron.francis@example.com' WHERE deleted_at IS NULL; 

-- postgresql can read index from back to front and from front to back (ascending and descending order) 
-- while for multicolumn composite index for queries where both desc and asc orders are used, the index is not supported.
CREATE INDEX first_name_birthday ON users USING BTREE(first_name ASC, birthday DESC); -- define default order of indecies 
EXPLAIN SELECT * FROM users ORDER BY first_name ASC, birthday DESC; -- using index 
EXPLAIN SELECT * FROM users ORDER BY first_name DESC, birthday ASC; -- using index scan backwards
EXPLAIN SELECT * FROM users ORDER BY first_name DESC, birthday DESC; -- violates order, using incremental sort 
EXPLAIN SELECT * FROM users ORDER BY first_name DESC; -- uses index backwards

-- NULL values are treated as largest value when ordering results
CREATE INDEX birthday_null_order ON users USING BTREE(birthday DESC NULLS FIRST); 
EXPLAIN SELECT * FROM users ORDER BY birthday DESC NULLS FIRST LIMIT 10; -- uses index
EXPLAIN SELECT * FROM users ORDER BY birthday ASC NULLS LAST LIMIT 10; -- uses index backwards
EXPLAIN SELECT * FROM users ORDER BY birthday DESC NULLS LAST LIMIT 10; -- sequential scan 

-- funcation index (indecies on functions)
SELECT email, split_part(email, '@', 2) as domain FROM users LIMIT 10; -- computed dynamically 
CREATE INDEX domain ON users USING BTREE(split_part(email, '@', 2)); -- create index on a defined function
EXPLAIN SELECT emaiL FROM users WHERE  (split_part(email, '@', 2) = 'toy.com') LIMIT 10; -- uses index 
CREATE INDEX lower_email ON users USING BTREE(lower(email)); 
EXPLAIN SELECT * FROM users WHERE (lower(email) = lower('AARON.francis@example.com')) LIMIT 10; 

-- example of duplicate indecies
CREATE INDEX email on users USING BTREE(email); 
CREATE INDEX email_is_pro on users USING BTREE(email, is_pro); -- left most is functionally the same as the singular index (it can replace entirely).

--- Hash index (useful for strict equality only; implemented using a hash function; unlike BTREE implementation it cannot be used for search matches or ranges, etc.)
CREATE INDEX email_btree ON users USING BTREE(email);
CREATE INDEX email_hash ON users USING HASH(email);
EXPLAIN SELECT * FROM users WHERE (email = 'aaron.francis@example.com') LIMIT 10; -- uses hash index
EXPLAIN SELECT * FROM users WHERE (email < 'aaron.francis@example.com') LIMIT 10; -- uses btree index
EXPLAIN SELECT * FROM users WHERE (email LIKE '%@example.com') LIMIT 10; -- sequential scan decided

-- in production have clean naming e.g. {table_name}_{column(s)_{index type} 

--- --- --- --- --- 
-- EXPLAIN query plan (postgresql optimizes cost of query plan depending on data, indexies, statistics, etc.) - produces a query plan tree with dependent stages streaming data nested -> root;
EXPLAIN (format json) SELECT * FROM users where (email = 'aaron.francis@example.com') LIMIT 10; 
-- types of scans (in order of performance): sequential scan (read entire table sequentially); bitmap index scan (construct from index a bitmap of pages containing condition)-> bitmap heap scan (orders bitmap pages, visit locations of page mapped, verifies condition to pull enteries matching); index scan (get rows directly from index); Index only scan (covering index without visiting the database heap)
-- costs in comparable arbitrary postgresql units to estimate query without running it; startup time .. total time
-- # of rows that gets emitted 
EXPLAIN SELECT * FROM users WHERE email < 'b' LIMIT 10; -- shows low cost bc of unit
EXPLAIN SELECT * FROM users WHERE email < 'b' ORDER BY last_name LIMIT 10; -- cost is back up again because it required ordering of all the retieved rows 
EXPLAIN ANALYZE SELECT * FROM users WHERE email < 'b' ORDER BY last_name LIMIT 10; -- runs query and measures the costs in ms
EXPLAIN (ANALYZE, COSTS OFF) SELECT * FROM users WHERE email < 'b' ORDER BY last_name LIMIT 10; -- runs query and measures the costs in ms

--- --- --- --- --- 
-- Query 
-- unqualified join -> cross-join (cartesian join; x * y rows)
SELECT users.id, users.first_name, bookmarks.id, bookmarks.url FROM users CROSS JOIN bookmarks LIMIT 10; -- cross join with size = users size x bookmarks size
SELECT users.id, users.first_name, bookmarks.id, bookmarks.url FROM users, bookmarks LIMIT 10; -- alternate syntax
SELECT upper(letter) || number FROM letters CROSS JOIN numbers; -- produce a concatenated result {letter}{number}
SELECT (chr(l) || n) AS Code FROM generate_series(1, 100) AS numbers(n) CROSS JOIN generate_series(65, 90) AS letters(l);

-- qualified join -> inner join (only matching rows; default type of join)
SELECT users.id, users.first_name, bookmarks.id, bookmarks.url FROM users INNER JOIN bookmarks ON users.id = bookmarks.user_id LIMIT 10; -- inner join
SELECT users.id, users.first_name, bookmarks.id, bookmarks.url FROM users JOIN bookmarks ON users.id = bookmarks.user_id WHERE (bookmarks.id = 738396) LIMIT 10; 
UPDATE bookmarks SET user_id = NULL WHERE id = 738396; -- remove foreign key constraint
SELECT users.id, users.first_name, bookmarks.id, bookmarks.url FROM users JOIN bookmarks ON users.id = bookmarks.user_id WHERE (bookmarks.id = 738396) LIMIT 10; -- orphans eliminated

-- shorthand syntax
SELECT * FROM users JOIN bookmarks ON USING(user_id) LIMIT 10;  -- requires both to hold "user_id" field

-- OUTER JOIN (LEFT, RIGHT, FULL)
SELECT users.id, users.first_name, bookmarks.id, bookmarks.url FROM users LEFT JOIN bookmarks ON users.id = bookmarks.user_id LIMIT 10;
SELECT users.id, users.first_name, bookmarks.id, bookmarks.url FROM users LEFT JOIN bookmarks ON users.id = bookmarks.user_id WHERE bookmarks.user_id is NULL LIMIT 10;

--- joining on a subquery result
SELECT * FROM users LEFT JOIN bookmarks ON users.id = bookmarks.user_id WHERE bookmarks.user_id is NULL LIMIT 10;
CREATE INDEX bookmarks_secure_url ON bookmarks(user_id, (starts_with(url, 'https'))); -- using a function reference filter the table
EXPLAIN SELECT * FROM bookmarks WHERE (starts_with(url, 'https')) is TRUE LIMIT 10;  -- by itself will not do an index scan 
EXPLAIN SELECT users.id, first_name, url FROM users LEFT JOIN ( -- join on subquery
    SELECT * FROM bookmarks WHERE starts_with(url, 'https') IS TRUE -- when used within a join, both user_id and starts_with portion of the index will be used. 
) AS bookmarks_secure ON users.id = bookmarks_secure.user_id LIMIT 100; 

--- LATERAL qualifier (for every row in the pre-seeding table run the subquery and use its result set; expensive because it runs the subquery for every row in the pre-seeding table)
-- there are more performant approaches to lateral join
SELECT users.id, first_name, url FROM users LEFT JOIN LATERAL (
    SELECT * FROM bookmarks WHERE users.id = bookmarks.user_id ORDER BY users.id DESC LIMIT 1
) AS most_recent_bookmark ON TRUE WHERE most_recent_bookmark.id IS NULL LIMIT 10;
SELECT users.id, first_name, url FROM users INNER JOIN LATERAL (
    SELECT * FROM bookmarks WHERE users.id = bookmarks.user_id ORDER BY users.id DESC LIMIT 1
) AS most_recent_bookmark ON TRUE LIMIT 10;

---- ---- ---- ----

--- put two set generating function side-by-side
SELECT * FROM generate_series(1, 10) AS a, generate_series(101, 120) AS b; -- cross join will produce a cartesian product (not what we intend for putting two set generating functions side-by-side)
SELECT * FROM rows FROM (
    generate_series(1, 10), 
    generate_series(101, 120)
) AS combined_table(s1, s2) WHERE s1 IS NOT NULL;
SELECT day_date::DATE, day_of_year FROM rows FROM (
    generate_series('2025-01-01'::DATE, '2025-12-31', '1 day'::interval),
    generate_series(1, 380)
) AS day_of_year(day_date, day_of_year) WHERE day_date IS NOT NULL; 

-- lined-up without ordinality
SELECT * FROM rows FROM (
    UNNEST(ARRAY[101, 102, 103]), 
    UNNEST(ARRAY['Computer', 'Car', 'Machine']), 
    UNNEST(ARRAY[999.99, 499.49, 299.99])
) AS combined(product_id, product_name, price);

--- filling gaps in a result sequence (using left join of geenrate series)
SELECT sale_date, sum(amount) FROM sales GROUP BY sale_date ORDER_BY sale_date ASC; -- data may procude gaps in the results for non existing sale_dates
SELECT 
    all_dates.sale_date::DATE, 
    coalesce(total_amount, 0) -- will pick the first non-null value (if total_amount is null wll pick 0)
FROM generate_series('2024-01-01'::DATE, '2024-1-31'::DATE, '1 day'::interval) AS month_dates(sale_date) LEFT JOIN (
    SELECT sale_date, sum(amount) as total_amount FROM sales GROUP BY sale_date
) AS sales ON sales.sale_date = all_dates.sale_date; 

--- use subquery to filter data from table based on related data from related table (filter table based on subquery results)
SELECT user_id, count(*) FROM bookmarks GROUP BY user_id HAVING count(*) > 16; -- (get users that have > 16 bookmarks) -- HAVING is like WHERE but operates after GROUP BY operation
-- (full query: get users that have > 16 bookmarks) -- NOTE: this query will be optimized as a semi-join (and not executed in 2 completely separate stages); named `semi` because no data is taken from the subquery table)
SELECT * FROM users WHERE id IN ( 
    SELECT user_id, count(*) FROM bookmarks GROUP BY user_id HAVING count(*) > 16
); 
-- equivalent join
SELECT users.id, first_name, last_name, bookmarks.count FROM users INNER JOIN (
    SELECT user_id, count(*) as count FROM bookmarks GROUP BY user_id HAVING count(*) > 16
) AS bookmarks ON users.id = bookmarks.user_id ORDER BY bookmarks.count;
EXPLAIN (ANALYZE, COSTS OFF) SELECT users.id, first_name, last_name, bookmarks.count FROM users INNER JOIN (
    SELECT user_id, count(*) as count FROM bookmarks GROUP BY user_id HAVING count(*) > 16
) AS bookmarks ON users.id = bookmarks.user_id ORDER BY bookmarks.count; -- `loops` section of the query execution measurement shows the # of times the subquery was run

-- where exists 
SELECT * FROM users WHERE EXISTS ( -- for every user will run the subquery and short-circuit on the first instance found (while WHERE IN  will do a semi-join)
    SELECT 1 FROM bookmarks WHERE users.id = bookmarks.user_id AND starts_with(url, 'https') 
);

-- issue with 'not in'
SELECT * FROM users WHERE id NOT IN (
    VALUES (1), (2), (NULL) -- NULL value will cause the entire query to return no results
);

-- WHERE value IN (checks if value exists in subquery result; can optimize as semi-join) vs WHERE EXISTS (checks which rows satisfy condition; executes subquery on every row)

--- --- --- --- --- 
-- combine/merge queries into a single list
SELECT 1 UNION SELECT 2; -- must have same number of columns and data type; deduplicates results;
SELECT 1 UNION ALL SELECT 1; -- ALL modifier will avoid deduplicate results; more performant (doesn't have to compare to avoid duplicates)

SELECT true as active, * FROM users 
UNION
SELECT false, * FROM users_deleted_archive; -- allows to query both tables at once

SELECT generate_series(1, 5)
INTERSECT ALL 
(
    SELECT generate_series(3, 7) 
    UNION ALL 
    SELECT generate_series(10, 15)
);


SELECT generate_series(3, 11) -- get all series except where it overlaps with the target series
EXCEPT ALL 
SELECT generate_series(10, 15);

--- --- --- --- --- 
-- set generating functions
SELECT generate_series('2024-01-01'::date, '2024-12-31'::date, '3 day'::INTERVAL)::DATE AS date; 
SELECT generate_series(1, 100, 3)::INT AS number;
SELECT UNNEST(ARRAY[1, 2, 3, 4, 5]) AS tag_name;
SELECT ordinality, element FROM UNNEST(ARRAY['first', 'second', 'third']) WITH ORDINALITY AS table_name(element, ordinality); 
SELECT * FROM jsonb_to_recordset('[
    {"id": 1, "name": "Alice", "email": "alice@example.com"},
    {"id": 2, "name": "Bob", "email": "bob@example.com"},
    {"id": 3, "name": "Charlie", "email": "charlie@example.com"}
]'::jsonb) AS table_name(id INT, name TEXT, email TEXT); 
SELECT regexp_matches('The quick brown fox jumps over the lazy dog', '\m\w{4}\M', 'g') AS match; 
SELECT m[1] AS name, m[2] AS age FROM (SELECT regexp_matches('NAME: Alice, Age: 40; Name: bob, Age: 33', 'Name: (\w+), Age: (\d+)', 'g') AS m) AS sub; 

--- --- --- --- --- 
-- creating index for a foreign key (in the child table); The pateny table's primary key is enforced by an index, but the child is not covered.
CREATE TABLE states (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
    name TEXT
); 
CREATE TABLE cities ( 
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY, 
    state_id BIGINT REFERENCES states(id), 
    name TEXT
); 
SELECT * FROM pg_indexes WHERE tablename = 'states'; -- index exists on primary key
SELECT * FROM pg_indexes WHERE tablename = 'cities'; -- index exists on primary key, but no index on foreign key 

SELECT * FROM pg_indexes WHERE tablename = 'bookmarks'; -- index exists on primary key, but no index on foreign key 
EXPLAIN SELECT * FROM users INNER JOIN bookmarks ON bookmarks.user_id = users.id WHERE users.id < 100; -- relies on sequential scan to find matching user_id in bookmarks table
CREATE INDEX idx_bookmarks_user_id ON bookmarks(user_id, additional_field_index_1, additional_field_index_2); -- accomplishes join with index scans effectively; take advantange of composite index for allowing index filtering on the right of the join column field;  


---- ---- ---- ---- ----
---------------------------- extensive notes reached @ 75.-Grouping; after which shallow notes are recorded
--- topics for grouping: group by grouping sets, group by rollup, group by cube; can be used in 'window' section too 
--- topics for window (preserves all rows and gains additional info, unlike aggregation or grouping which loses row info filtering them out) functions using `over` keyword and partition by; frames (rows, groups, range) within a partition of window; window preceding and following; lead and lag function to peak ahead and behind for values; 
--- topics for CTEs (Common Table Expressions; allows for refactoring and breaking large queries into manageable parts): `with` keyword;  postgresql can optimize queries that rely on CTEs by deciding when to materialize the temporary tables.
--- topics for recursive CTEs: `with recursive` keyword with anchor condition and recursive statement of `union all`; recursive CTEs can be used to simplify the application logic;
--- NULL handling topics: `is distinct from`; `coalesce`; `NULL FIRST`; `nullif`; `where {id} not in` returns nothing if null exist; 
--- ROW VALUE SYNTAX topics: cursor (key-set) based pagination pattern; EXTRACT keyword; 
--- VIEWS (a named query stored in database; CTE but the query gets stored for later execution and reference; underlying query will have to run to produce the data) topics: CREATE VIEW; 

SHOW search_path; 
CREATE SCHEMA views; 
SET search_path = views,public; -- find table name in views then in public
CREATE VIEW views.users AS (
    SELECT * FROM public.users 
    UNION ALL 
    SELECT * FROM public.users_archive
);
EXPLAIN SELECT * FROM users; -- references views.users
EXPLAIN SELECT * FROM public.users; 

--- materialized views (the results are written to the disk; may be stale data until the view is refereshed): `create materialized view`, `alter maeterialized view`, `refresh materialized view`; 
--- upsert (insert or update; `INSERT ... ON CONFLICT`);
--- returning keyword;
 
--- text search using limited vanilla postgresql: LIKE/ILIKE operators with wildcard operator (slow for long content); text search with ts_vector, ts_query (support a DSL for rules), ts_rank with customization of rank weights (relavence), and matching operator `@@`; plainto_tsquery, phraseto_tsquery, websearch_to_tsquery (similar to syntax supported by web search engines; user friendly); use language in to_tsvector, setwright; GIN index over ts_vectors; ts_headline to highlight matching in postgresql; 

--- JSON topics: does JSON have strict schema? is it used as a blob or querying always the fields? indexing into JSON blob is supported; JSON (compact & slower; reasonable usage is when json origianl order is important & exact representation which is rarely the case; or if it will be sent to the user and never operated on;) vs. JSONB (stores json parsed on disk; takes more space & overhead; more performant in binary representation; removes whitespace, rearranges keys, deduplicates fields;); `is json`; `jsonb_build_object`, `to_json`, `jsonb_build_array`, `jsonb_build_array`, json_agg(row_to_json()); `->` & `->>` & `#>` extract operator (returns NULL if doesn't exist), jsonb_path_query (implements standard query path) `$.status` with unquote `#>> '{}'`; `@>` & `<@` containment check operator, `?` & `?|` & `?&` existence operator; expand to recordset table `jsonb_each`, `jsonb_each_text`, `jsonb_to_recordset` with lateral qualifier vs creating EAV style table; in-place updates of json `jsonb_set` + `json_scalar`, remove field  `-`, jsonb_insert; 
--- index json topics: functional straight index BTree index on result of expression vs index over a generated column BTree index (under the hood both are performant; choosing approach is a matter of preference; but generated column requires querying the newly generated column) vs GIN index (used to index entire json blob; much better for containment and existence operator queries; performance overhead for large json; larger size index) with json_ops or json_path_ops options (smaller with less supported operations); 

--- pgvector extension topics (store vector embeddings and query against them): OpenAI to generate embeddings, `<->` operator to find related, sematic search on embeddings (creating embeddings for search query and use `<->` to order results of related content embeddings); `<->` L2 operator, `<=>` Cosine operator, `<+>` L1 operator, Inner product `<=>` (choosing depending on model used and their recommendation and testing it against the data);
--- index vectors: HNSW (higher quality/accuracy but requires more resources and query time) vs IVF-flat index (query with vectors indexes will perform an spproximate nearest neighbours search; speeds up queries over accuracy tradeoff); 


--- --- --- --- --- 

-- READ list: 
-- https://medium.com/@Amir_M4A/how-postgresql-stores-data-heaps-blocks-and-ctids-explained-f66d052cf87d
-- https://medium.com/quadcode-life/structure-of-heap-table-in-postgresql-d44c94332052 