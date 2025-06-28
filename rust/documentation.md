Queue: you can insert only in one end and remove from the other.
Deque: you can insert and remove from both ends.
So using a Deque, you can model a Queue as well as a Stack. 

---

Multiple owners that can mutate the value: 
type Link<T> = Option<Rc<RefCell<Node<T>>>>;
Re-introduce mutability into shared reference & ownership smart pointers. 

Rc & RefCell for single-thread
Arc & Mutex for multiple threads 

---

Foreign Function Interface (FFI)
Unsafe uses FFI to interface with safe rust. 

---

- Add miri tool to the rust compiler to analyze unsafe code: 
`rustup +nightly-2022-01-21 component add miri`
`cargo +nightly miri test`

extra-strict mode experimental feature : 
`MIRIFLAGS="-Zmiri-tag-raw-pointers" cargo +nightly-2022-01-21 miri test`

--- 

concept of nested reborrows (stack of borrows). Only one live pointer on the top of the stack at any given time (with exclusive mutable access). 
raw pointers of other raw pointers to the same value share the same borrow. 

--- 

array initialization: 
```rust
    let a : [i32; 10] = [0; 10]; 
    println!("{:?}", &a[..])
```

--- 




# Crates: 
- image: write image files from array pixel values; 
- num: deal with complex numbers; 
- crossbeam: scoped thread creation primitives; 
- text-colorizer 
- regex 


# Notes: 
- concept of runtime-guareded mutability for multiple references: Rust provides runtime-checked interior mutability types (Mutex, RefCell, RwLock) as a controlled, explicit escape hatch for these specific scenarios. These types encapsulate the "unsafe" operations (like manipulating raw pointers or atomically swapping state) but provide a safe API on top, moving the "guarantees" from compile-time to runtime.