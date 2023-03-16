use std::env;
use std::fs;
use std::path;

fn main() {
    // parse arguments
    let arguments: Vec<String> = env::args().collect();
    let (_pattern, filename) = parse_config(&arguments);

    let path = path::Path::new(filename);
    let contents: String = fs::read_to_string(path).expect("Unable to read file.");

    println!("Contents: {}", contents);
}

fn parse_config(arguments: &[String]) -> (&str, &str) {
    let pattern = &arguments[1];
    let filename = &arguments[2];

    (pattern, filename)
}

// https://doc.rust-lang.org/book/ch12-03-improving-error-handling-and-modularity.html#grouping-configuration-values
