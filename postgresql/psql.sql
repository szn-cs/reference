-- 
-- SQL can be run in pgadmin4 interface or psql command
-- https://www.postgresql.org/docs/current/reference.html
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

--- --- --- --- --- 
--- --- --- --- --- 
--- --- --- --- --- 
