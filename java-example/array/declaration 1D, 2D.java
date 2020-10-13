public class ArraysSample {
  private static double[] grades;

  public static void main(String[] args) {
    // An array represents a collection of elements of the same type

    // arrays are reference type in java
    // grades[0] = 95.2; // Oops a NullPointerException is thrown!
    // int gradesMaxCount = grades.length; // // Oops a NullPointerException will be thrown!

    // A NullPointerException is thrown if the user tries to use a reference whose value is null
    // We have to create the array before using it!
    grades = new double[4]; // create an array and save its reference (its memory address) into
                            // grades
    grades[0] = 95.2;
    grades[1] = 87.5;
    grades[2] = 90.8;
    // grades[5] = 76.9; // Oops an ArrayIndexOutOfBoundsException is thrown!
    // indices must be in the range 0 .. grades.length -1

    // One dimensional arrays
    // ----------------------
    // create an array of integers which contains 4 int default values
    int[] temperatures; // define an array. The array is not yet created. "temperatures == null"
    temperatures = new int[4]; // This array can store up to 4 elements of type int
    int capacity = temperatures.length;

    // Arrays are zero-indexed in java
    // initialize the content of an array individually
    temperatures[0] = 35;
    temperatures[1] = 55;
    temperatures[2] = 75;
    temperatures[3] = 80;
    //temperatures[4] = 90; // Oops! An ArrayIndexOutOfBoundsException!
                          // Do not use an index out of the range 0.. temperatures.length-1

    temperatures[0] = 10;
    // use of a loop to initialize/set the content of an array
    for (int i = 1; i < temperatures.length; i++) {
      temperatures[i] = temperatures[i - 1] + 15;
    }

    // Note that, instead of assigning array elements individually, or in a for loop,
    // we can initialize the array at the time of declaration.
    int[] digits = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    capacity = digits.length; // 10

    int[] numbers = new int[] {10, 20, 30, 40, 50};

    // Two dimensional arrays
    // ----------------------
    double[][] coordinates = new double[3][2]; // two dimensional array of 3 rows.
                                               // Each row refers to one dimensional array
                                               // of two elements (i.e. 2 columns)

    // As defined above, coordinates array can store up to 6 default double values.
    // default initial values: 0.0

    // Two dimensional arrays
    // ----------------------
    capacity = coordinates.length; // 3
    int size1 = coordinates[0].length; // 2
    int size2 = coordinates[1].length; // 2
    int size3 = coordinates[2].length; // 2

    coordinates[0][0] = 10.1;
    coordinates[0][1] = 20.2;
    coordinates[2][0] = 30.3;
    coordinates[2][1] = 40.4;
    coordinates[1][0] = 78.5;
    coordinates[1][1] = 85.0;
    // coordinates[3][0] = 50.1; // Oops! ArrayIndexOutOfBoundsException!

    coordinates[1] = null;
    coordinates[0] = new double[] {20.0, 65.2, 85.3};
    size1 = coordinates[0].length; // 3
    size3 = coordinates[2].length; // 2
    // size2 = coordinates[1].length; // Oops! A NullPointerException is thrown
    // because coordinates[1] is null
    coordinates[1] = new double[] {56.3, 67.2, 89.4, 91.5};
    for (int i = 0; i < coordinates.length; i++) {
      if (coordinates[i] != null)
        System.out.print(coordinates[i].length + " ");
    }

    // capacity of an array: positive integer
    int[] data = new int[0]; // useless array. This array cannot store any element.
    
    // create a two dimensional array which stores 5 null references
    double[][] grades = new double[5][];
    grades[0] = new double[]{95.3, 89.5};
    grades[1] = new double[2];

  }
}