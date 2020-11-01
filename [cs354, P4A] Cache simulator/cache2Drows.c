////////////////////////////////////////////////////////////////////////////////
// Main File:        cache2Drows.c
// This File:        cache2Drows.c
// Semester:         CS 354 Fall 2020
// Author:           Safi Nassar
////////////////////////////////////////////////////////////////////////////////

int arr2D[3000][500];

int main() {
	for(int r=0; r<3000; r++)
		for(int c=0; c<500; c++) 
			arr2D[r][c] = r + c; 
}
