
public class PrimeChecker {
  // Returns 0 if value is not prime, 1 if value is prime
     public static int isPrime(int testVal, int divVal) {
        // Base case 1: 0 and 1 are not prime, testVal is not prime
        if(testVal == 0 || testVal == 1) return 0;
        // Base case 2: testVal only divisible by 1, testVal is prime
        if(divVal == 1) return 1;
        // Recursive Case
           // Check if testVal can be evenly divided by divVal
           if(testVal % divVal == 0) return 0;
           else     
           // If not, recursive call to isPrime with testVal and (divVal - 1)
              return isPrime(testVal, divVal - 1);
     }
  
     public static void main(String[] args) {
        int primeCheckVal;     // Value checked for prime
  
        // Check primes for values 1 to 10
        for (primeCheckVal = 1; primeCheckVal <= 10; ++primeCheckVal) {
           if (isPrime(primeCheckVal, (primeCheckVal - 1)) == 1) {
              System.out.println(primeCheckVal + " is prime.");
           }
           else {
              System.out.println(primeCheckVal + " is not prime.");
           }
        }
     }
  }
  