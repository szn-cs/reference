compile:
	gcc -Wall -m32 -std=gnu99 -o cache1D cache1D.c
	gcc -Wall -m32 -std=gnu99 -o cache2Drows cache2Drows.c
	gcc -Wall -m32 -std=gnu99 -o cache2Dcols cache2Dcols.c
	gcc -Wall -m32 -std=gnu99 -o cache2Dclash cache2Dclash.c

simulateCache:
	mkdir -p ./output/

	/s/pin-3.16/pin -injection child -t /s/pin-3.16/source/tools/Memory/obj-ia32/dcache.so -c 2 -a 1 -b 32 -o ./output/cache1D_32.txt -- cache1D
	/s/pin-3.16/pin -injection child -t /s/pin-3.16/source/tools/Memory/obj-ia32/dcache.so -c 2 -a 1 -b 64 -o ./output/cache1D_64.txt -- cache1D

	/s/pin-3.16/pin -injection child -t /s/pin-3.16/source/tools/Memory/obj-ia32/dcache.so -c 2 -a 1 -b 64 -o ./output/cache2Drows_64.txt -- cache2Drows 

	/s/pin-3.16/pin -injection child -t /s/pin-3.16/source/tools/Memory/obj-ia32/dcache.so -c 2 -a 1 -b 64 -o ./output/cache2Dcols_64.txt -- cache2Dcols

	/s/pin-3.16/pin -injection child -t /s/pin-3.16/source/tools/Memory/obj-ia32/dcache.so -c 2 -a 1 -b 64 -o ./output/cache2Dclash_a1_64.txt -- cache2Dclash
	/s/pin-3.16/pin -injection child -t /s/pin-3.16/source/tools/Memory/obj-ia32/dcache.so -c 2 -a 2 -b 64 -o ./output/cache2Dclash_a2_64.txt -- cache2Dclash

print:
	# cat ./output/*
	find ./output/* -type f -print -exec cat {} \;
	
