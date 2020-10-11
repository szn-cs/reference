// -------------------Binary search example:--------------------------------------

var dataset = [1,3,5,6,7,9,10,12,13,14,16,17,18,20]  /* sorted list */

console.log(`Binary search - found at index: ${binarySearch(dataset, 20)}\n`)

// publicly facing method function (notice the different api exposed / signature from helper func)
function binarySearch(l , key) { 
    // preprocess data
    
    // search recursively
    let indexFound = binarySearch_recursive(l, 0, l.length, key)
    
    // do something with data before returning to the client.
    return indexFound;
}

function binarySearch_recursive(l, low, high, key) {     
  if(low > high) return false; // base terminating case
  
  let middle = Math.floor((low + high) / 2) // reduction / recursive case    
  if(l[middle] > key)
    return binarySearch_recursive(l, low, middle - 1, key)
  else if(l[middle] < key)
    return binarySearch_recursive(l, middle + 1, high, key)
    
  // base terminating case: index value equals key
  return middle
}

// -------------------Print string example:--------------------------------------

var string = "abcde"
print_loop(string)
print_recursive1(string)
print_recursive2(string)

// non recusrive version (using loop iteration) 
function print_loop(srting) { 
    let array = []
    console.log("print_loop - printing letters:")
    for(let i=0; i < string.length; i++) 
        array.push(string.charAt(i))    
    for(let l of array) 
        console.log(l)
    return array
}

// recursive function 1
function print_recursive1(string) { 
    // input data is unaffected by the recursion logic, 
    // permitting a different exposure of the api 
    console.log("print_recursive1 - printing letters:")
    
    let array = print_helper1(string)
    for(let l of array) 
        console.log(l)
    
    // additional data manipulation
    return array
}
    
// helper funciton abstracting the complexity of the recursion
function print_helper1(string) { 
        let aggregator = []
        if(string.length == 0) return aggregator; // base terminating case
        
        // extract letters
        aggregator.push(string.charAt(0)) // add current letter to array
        string = string.substring(1) // reduction
        
        // concat arrays
        aggregator = [...aggregator, ...print_helper1(string)] 
        
        return aggregator
}

// recusive function 2 - notice the api exposed is different that helper function
function print_recursive2(string) { 
    console.log("print_recursive2 - printing string:")
    print_helper2(string, 0)
       
}

// helper function - version with string kept intact
function print_helper2(string, index) { 
        if(index == string.length) return; // base terminating case
    
        console.log(string.charAt(index))
        index++
        print_helper2(string, index)
}