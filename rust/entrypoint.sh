cargo test -- --show-output
cargo test -- --test test_name

### 

MIRIFLAGS="-Zmiri-tag-raw-pointers" cargo +nightly miri run

### 

rustup doc 
rustup doc --std


### 

cargo clippy
cargo fmt

### 

# rust toolchain installed in ~/.rustup
find ~/.rustup/ -name rust-lld

## 

cargo watch -q -c -x "run -q"


### 

cargo tree --format "{p} {f}"