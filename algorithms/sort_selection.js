console.log(selectionSort())
function selectionSort(l = [5,4,3,2,1]) {
  // sorted mark
  for(let s = 0; s < l.length - 1; s++) {
    let smallest = s // reduce # of swaps
    
    // unsorted mark
    for(let u = s + 1; u < l.length; u++) {
      if(l[u] < l[smallest]) // compare
        smallest = u
    }

    if(smallest == s) continue; // skip redundant

    console.log(`swap ${l[s]} <- ${l[smallest]}`)
    ;[l[s], l[smallest]] = [l[smallest], l[s]] // swap
  }

  console.log(outer, inner)
  return l
}