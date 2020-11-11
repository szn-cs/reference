tracePath="traces/trace2"
s=4; 
E=1; 
b=4; 
./csim-ref -v -s $s -E $E -b $b -t $tracePath; 
./csim -v -s $s -E $E -b $b -t $tracePath