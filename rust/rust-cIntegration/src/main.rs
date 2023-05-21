#[link(name = "library", kind = "static")]

extern "C" {
  fn add(v1: f32, v2: f32) -> f32; 
}

fn main() {
    println!("Rust execution.");
    let r  = unsafe { add(2., 13.) };
    println!("Rust value is: {}", r);
} 
