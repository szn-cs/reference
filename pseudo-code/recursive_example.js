// fibunacci number at index
// console.log(fibunacci_index(7))
function fibunacciIndex(termIndex) {
  if(termIndex == 0 || termIndex == 1) return termIndex; 
  var currentSum = fibunacciIndex(termIndex - 1) + fibunacciIndex(termIndex - 2)
  return currentSum
}

// print n fibunacci numbers (excluding 0, 1)
// fibunacciTermsNumber(20)
function fibunacciTermsNumber(n, prev1 = 1, prev2 = 0) {
  // base terminating case
  if(n <= 0) return;

  // reduction / recursive case
  var fibunacciTerm = prev1 + prev2
  console.log(`${fibunacciTerm} = ${prev1} + ${prev2}`)
  fibunacciTermsNumber(--n, fibunacciTerm, prev1)
}

// starting from 0 where f(0)=0 & f(1) = 1
// console.log(fibunacciAtTermNumber(4)) // should equal 3
function fibunacciAtTermNumber(termPosition) { 
  if(termPosition == 0) return 0; else if (termPosition == 1) return 1; 
  return fibunacciAtTermNumber(termPosition - 1) + fibunacciAtTermNumber(termPosition - 2)
}

// Note: iteration is more performant than recursion for factorial problem
// console.log(factorial_recursive(4))
function factorial_recursive(n) { 
  if(n == 1) return 1
  return n * factorial_recursive(n - 1)
}
// console.log(factorial_iteration(4))
function factorial_iteration(n) {
  let f; 
  for(f = n, n--; n > 1; --n)
    f = f * n
  return f
}

// console.log(exponentiation(4, 5)) // 4^5 = 1024
function exponentiation(base, exponent) {
  if(exponent == 1) return base 
  return base * exponentiation(base, exponent - 1)
}

// console.log(binarySearch(undefined, undefined, undefined, 3))
function binarySearch(l = [1,3,5,6,7,9,10,12,13,14,16,17,18,20] /* sorted list */, low = 0, high = l.length - 1, key) { 
  // base terminating case
  if(low > high) return false;
  
  // reduction / recursive case
  let middle = Math.floor((low + high) / 2)
  if(l[middle] > key)
    return binarySearch(l, low, middle - 1, key)
  else if(l[middle] < key)
    return binarySearch(l, middle + 1, high, key)
    
  // base terminating case
  return middle // index value equals key
}

// console.log(cumulativeSum(5))
function cumulativeSum(n) {
  if(n == 0) return 0
  return n + cumulativeSum(n - 1)
}

// console.log(reverseList())
function reverseList(l = [6,5,4,3,2,1,0], startIndex = 0, endIndex = l.length - 1) { 
  if(startIndex == endIndex) return l;
  [l[startIndex], l[endIndex]] = [l[endIndex], l[startIndex]] // swap positions
  return reverseList(l, startIndex + 1, endIndex - 1)
}

// console.log(greatestCommonDivisor(24, 18))
function greatestCommonDivisor(n1, n2) {
  // base case
  if(n1 == n2) return n1

  // reduction/recursive case
  if(n1 < n2) [n1, n2] = [n2, n1] // swap variables
  return greatestCommonDivisor(n1 - n2, n2)
}

// permutations (without repititions of the same digit/symbol) 
scramble(['a', 'b', 'c'], [], 3) // abc => 3 symbols, 3 positions => 3 x (3-1) x (3-2) = 6 permutations possible. 
function scramble(l, chosen, positions) {
  if(positions == 0 || l.length == 0) console.log(chosen.join())
  else {
    for(let i = 0; i < l.length; i++) {
      newL = [...l]; newL.splice(i, 1); // remove repitions
      scramble(newL, [...chosen, l[i]], positions - 1)
    }
  }
}