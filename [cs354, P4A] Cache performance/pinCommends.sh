# pin v3.16
#/s/pin-3.16/pin -injection child -t /s/pin-3.16/source/tools/Memory/obj-ia32/dcache.so -c <capacity> -a <associativity> -b <block-size> -o <output-file> -- <your-exe>

# D-cache (data as opposed to instruction cache) - default params: 
# capacity: 2  (2KB or 2048 bytes)
# associativity: 1   (direct-mapped)

