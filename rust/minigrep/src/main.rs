use std::env;
use std::fs;
mod lib;
use lib::Config;

fn main() {
    let args: Vec<String> = env::args().collect();
    let config = Config::new(&args);
    println!("string1 {} string2 {}", config.search, config.path);

    let content = fs::read_to_string(config.path).expect("Not able to read file.");
    println!("{}", content);
}
