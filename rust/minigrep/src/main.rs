use std::process; 
use std::env; 
// use minigrep::Config; 

fn main() {
    let args: Vec<String> = env::args().collect(); 

    // parse arguments
    let config = config::build(&args).unwrap_or_else(|err| { 
        eprintln!("Problem parsing the arguments: {err}"); 
        process::exit(1); 
    });

    // execute search
    if let Err(e) = minigrep::run(config) { 
        eprintln!("App error: {e}"); 
        process::exit(1); 
    } 
}
