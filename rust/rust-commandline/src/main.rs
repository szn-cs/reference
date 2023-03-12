use std::env;
use std::fs;
use std::path::Path;

// (source ./script.sh && run)
fn main() {
    let arguments: Vec<String> = env::args().collect();

    dbg!(&arguments);

    let pattern: &String = &arguments[1];
    let filename: &String = &arguments[2];

    println!("pattern: {}\nfilename: {}", pattern, filename);

    let path = Path::new(filename);
    let contents: String = fs::read_to_string(path).expect("Unable to read file.");

    println!("Contents:\n{}", contents);
}
