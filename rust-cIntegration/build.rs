extern crate cmake; 

use cmake::Config; 

fn main() { 
  let dst = Config::new("cLibrary").build(); 

  println!("cargo:rustc-link-search=native={}", dst.display());
  println!("cargo:rustc-list-lib=static=library");

}