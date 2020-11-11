////////////////////////////////////////////////////////////////////////////////
// Main File:        cache2Dcols.c
// This File:        cache2Dcols.c
// Semester:         CS 354 Fall 2020
// Author:           Safi Nassar
////////////////////////////////////////////////////////////////////////////////

int arr2D[3000][500]; 

int main() { 
	for(int c=0; c<500; c++)
		for(int r=0; r<3000; r++)
			arr2D[r][c] = r + c;	
}
