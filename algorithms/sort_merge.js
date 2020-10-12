console.log(mergeSort())
function mergeSort(ref = {l: [9,2,8,3, 7, 5, 6, 4]}, s = 0, e = ref.l.length-1) { 
  // base case: single element
  if(s >= e) return;

  let m = Math.floor((s+e) / 2) // middle

  // recursive case: divide
  mergeSort(ref, s, m) // left partition
  mergeSort(ref, m+1, e) // right partition

  // action 
  merge(ref, s, m ,e)

  return ref.l
}

// merge sorted lists
function merge(ref, l /*left*/, m /*middle*/, r /*right*/) {
  let s = l // starting index of original list
  let mergedList = [] // range of merge: sl - er 
  // indecies of partitions: start & end for right & left
  let ls = l, le = m, rs = m+1, re = r
   
  // compare
  while(ls <= le && rs <= re) {
    if(ref.l[ls] < ref.l[rs])
      mergedList.push(ref.l[ls]) && ls++; 
    else
      mergedList.push(ref.l[rs]) && rs++;
  }

  // push remaining elements
  while(ls <= le) 
    mergedList.push(ref.l[ls]) && ls++;   
  while(rs <= re)
    mergedList.push(ref.l[rs]) && rs++;

  // reflect changes on original list
  for(let i = 0; i < mergedList.length; i++)
    ref.l[s + i] = mergedList[i]
}