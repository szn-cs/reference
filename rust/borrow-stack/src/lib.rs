
pub fn function() { 
    unsafe { 
        let mut x : u64 = 10; 
        let ptr1 = &mut x; 
        *ptr1 += 1u64; 
        let ptr2 = ptr1 as *mut _; 
        *ptr2 += 1u64; 
        let ptr3 = &mut *ptr2; 
        let ptr4 = ptr3 as *mut _; 

        *ptr4 += 1u64; 
        *ptr2 += 1u64; 
        *ptr3 += 1u64;


        println!("value = {}", *ptr2); 
        
        *ptr2 += 1u64; 
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn test1() {
        function(); 
    }
    
}
