public class RecursionExamples {



  public static int sum(int n) {
    if (n == 0) // base case
      return 1;
    else // recursive case
      return sum(n - 1) + sum(n % 2);
  }



  public static void recursive(int x) {
    System.out.println(x);
    recursive(x - 1);
  }

  public static int gcd(int x, int y) {
    if (x == 0)
      return y;
    else
      return gcd(y % x, x);
  }

}