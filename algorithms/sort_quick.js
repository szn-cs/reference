console.log(quickSort())
function quickSort(ref = {l: [9,2,8,3,7,4,6,5]}, s = 0 /*start*/, e = ref.l.length-1 /*end*/) {
  // base case: single element
  if(s >= e) return; 

  // action: 
  let se = highLowPartition(ref, s, e)

  // recursive case: 
  quickSort(ref, s, se)
  quickSort(ref, se + 1, e)

  return ref.l
}

function highLowPartition(ref, l /*low*/, h /*high*/) { 
  let middle = Math.floor((l + h) / 2) // middle index
  let pivot = ref.l[middle]

  while(true) {
    // propagate until swap needed
    while(ref.l[l] < pivot) 
      l++
    while(ref.l[h] > pivot)
      h--
    
    if(l >= h) break;

    [ref.l[l], ref.l[h]] = [ref.l[h], ref.l[l]] // swap
    l++; h--; // skip redundant check
  }

  return h // low partition last index
}