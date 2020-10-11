console.log(insertionSort())
function insertionSort(l = [5,4,3,1,2,9,6,8,0]) { 
  for(let nu = 1 /* next unsorted */; nu < l.length; nu++) {
    let c = nu  // current element
    while(c > 0 && l[c] < l[c-1]) {
      [l[c-1], l[c]] = [l[c], l[c-1]] // swap
      c--
    }
  }
  
  return l
}