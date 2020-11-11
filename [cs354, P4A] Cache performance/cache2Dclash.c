////////////////////////////////////////////////////////////////////////////////
// Main File:        cache2Dclash.c
// This File:        cache2Dclash.c
// Semester:         CS 354 Fall 2020
// Author:           Safi Nassar
////////////////////////////////////////////////////////////////////////////////

int arr2D[128][8];

int main() {
	for(int i=0; i<100; i++)
		for(int r=0; r<128; r += 64)
			for(int c=0; c<8; c++) 
				arr2D[r][c] = i + r + c; 
}
