use std::env;

pub struct Config {
    pub search: String,
    pub path: String,
}

impl Config {
    pub fn new(args: &[String]) -> Config {
        let search = args[1].clone();
        let path = args[2].clone();
        Config { search, path }
    }
}
