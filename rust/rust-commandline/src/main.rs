use std::env;
use std::fs;

struct Config {
    pattern: String,
    filename: String,
}

fn main() {
    let arguments: Vec<String> = env::args().collect();
    let config: Config = parse_config(&arguments);

    println!("arguemtns are: {}, {}", &config.pattern, &config.filename);

    let contents: String =
        fs::read_to_string(&config.filename).expect("File should have been able to be read");

    println!("File contents are: \n{}", contents);
}

fn parse_config(args: &[String]) -> Config {
    let pattern: String = args[1].clone();
    let filename: String = args[2].clone();

    Config { pattern, filename }
}

// TODO: Next https://doc.rust-lang.org/book/ch12-03-improving-error-handling-and-modularity.html#creating-a-constructor-for-config
