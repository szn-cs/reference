// taken from http://pages.cs.wisc.edu/~cs200/lecture/current/GameOfLife.java
/*       
        //Glider
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', '@', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', '@', '@', ' ', ' ', ' ', ' '},
        {' ', '@', '@', ' ', ' ', ' ', ' ', ' '},
        
        //Beacon
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', '@', '@', ' ', ' ', ' ', ' ', ' '},
        {' ', '@', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', ' ', ' ', '@', ' ', ' ', ' '},
        {' ', ' ', ' ', '@', '@', ' ', ' ', ' '},

        //R-pentomino
        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', '@', '@', ' ', ' ', ' ', ' '},
        {' ', '@', '@', ' ', ' ', ' ', ' ', ' '},
        {' ', ' ', '@', ' ', ' ', ' ', ' ', ' '},
                 
*/
import java.util.Random;
import java.util.Scanner;

public class GameOfLife {

    static final char LIVING = '@';
    static final char DEAD = ' ';
    
	public static void main(String[] args){
		Scanner input = new Scanner( System.in);
		Random randGen = new Random();
		
        char[][] world = new char[][] {
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
        };
        
        randomWorld( world, randGen);
        char[][] newWorld = new char[world.length][world[0].length];
        char[][] tempWorld;
        int generation = 0;

        String choice;
 //       int numLiving;
        do {
            System.out.println("Generation: " + generation);
            printWorld(world);
//            System.out.print("<Enter> or 'end': ");
//            choice = input.nextLine().trim().toLowerCase();

       //     numLiving = numLiving(world);

//            if (!choice.equals("end")) {
                // next generation
                nextGeneration(world, newWorld);

                tempWorld = world;
                world = newWorld;
                newWorld = tempWorld;
              
                generation++;
 //           }

        } while ( !matchingArrays(world, newWorld));
//        } while (!choice.equals("end"));


        input.close();
	} 	

	static void randomWorld( char[][] world, Random randGen) {
	
		for ( int row = 0; row < world.length; ++row) {
			for ( int col = 0; col < world[row].length; ++col) {
				if ( randGen.nextDouble() < 0.25) {
					world[row][col] = LIVING;
				} else {
					world[row][col] = DEAD;
				}
			}
		}
		
	}
	
	static int numLiving( char[][]world) {
		int numLiving = 0;
		for ( int row = 0; row < world.length; ++row) {
			for ( int col = 0; col < world[row].length; ++col) {
				if ( world[row][col] == LIVING) {
					numLiving++;
				}
			}
		}
		return numLiving;
	}
	
	static boolean matchingArrays( char[][]world, char[][]newWorld) {
//		int numDifferences;
		for ( int row = 0; row < world.length; ++row) {
			for ( int col = 0; col < world[row].length; ++col) {
				if ( world[row][col] != newWorld[row][col]) {
					return false;
			//		numDifferences++;
				}
			}
		}	
		return true;
//		return numDifferences;
	}
	
	/** 
	 * Whether a cell is living in the next generation of the game.
	 * 
	 * The rules of the game are as follows:
	 * 1) Any live cell with fewer than two live neighbors dies, as if caused
	 *    by under-population.
	 * 2) Any live cell with two or three live neighbors lives on to the next 
	 *    generation.
	 * 3) Any live cell with more than three live neighbors dies, as if by 
	 *    overcrowding.
	 * 4) Any dead cell with exactly three live neighbors becomes a live cell, 
	 *    as if by reproduction.
	 * 
	 * @param numLivingNeighbors The number of neighbors that are currently
	 *        living.
	 * @param cellCurrentlyLiving Whether the cell is currently living.
	 * 
	 * @return True if this cell is living in the next generation.    
	 */
	public static char isCellLivingInNextGeneration( int numLivingNeighbors, 
			char currentCell) {

		if ( currentCell == LIVING) {
			if ( numLivingNeighbors < 2) {
				return DEAD;
			} else if ( numLivingNeighbors > 3) {
				return DEAD;
			} else {
				return LIVING;
			}
		} else {
			if ( numLivingNeighbors == 3) {
				return LIVING;
			} else {
				return DEAD;
			}
		}
	}

	public static boolean isNeighborAlive(char [][]world, int neighborCellRow, 
			int neighborCellColumn) {
		if (( neighborCellRow >= 0) && ( neighborCellRow < world.length)) {

			if ( (neighborCellColumn >= 0) 
					&& ( neighborCellColumn < world[neighborCellRow].length)) {
				return world[neighborCellRow][neighborCellColumn] != DEAD;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static int numNeighborsAlive(char[][] world, int cellRow, 
			int cellColumn) {
		int numNeighbors = 0;
		//neighbors in the row above
		for(int column = cellColumn - 1; column <= cellColumn + 1; column++) {
			if( isNeighborAlive(world, cellRow-1, column)) {
				numNeighbors++;
			}
		}        

		//neighbors in the row below
		for(int column = cellColumn - 1; column <= cellColumn + 1; column++) {
			if( isNeighborAlive(world, cellRow+1, column)) {
				numNeighbors++;
			}
		}

		//neighbor to the left
		if( isNeighborAlive(world, cellRow, cellColumn - 1)) {
			numNeighbors++;
		}     

		//neighbor to the right
		if( isNeighborAlive(world, cellRow, cellColumn + 1)) {
			numNeighbors++;
		} 
		return numNeighbors;
	}

	public static void nextGeneration(char[][] currentWorld, char[][] newWorld) {
		int numNeighbors;
		for(int row = 0; row < newWorld.length; row++) {

			for(int column = 0; column < newWorld[0].length; column++) {

				numNeighbors = numNeighborsAlive(currentWorld, row, column);

				newWorld[row][column] = isCellLivingInNextGeneration( 
						numNeighbors, currentWorld[row][column]);
			}
		}

	}

	public static void printWorld( char[][] world) {
		int numAlive = 0;

		for( char[] row : world) {
			for( char ch : row) {
			    System.out.print( ch);
				if( ch != DEAD) {
					numAlive++;
				}
			}
			System.out.println();
		}

		System.out.println( numAlive + " cells are alive.");
		System.out.println("");
	}
}