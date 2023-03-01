public class AutoBoxingUnboxing {

  public static void main(String[] args) {
    // Java compiler makes an automatic conversion of the primitive types and their corresponding
    // reference ones (aka wrapper classes).

    // Autoboxing is the automatic conversion from a Java primitive
    // type to its corresponding reference one.
    // This includes converting an int to an Integer, a Float to float,
    // a short to Short, a double to a Double, and so on.

    // If the conversion goes the other way, this is called unboxing.

    // Here are examples of autoboxing and auto-unboxing operations:

    // Example #1: Autoboxing
    Character ch = 'a'; // This is autoboxing (auto-converting a char into a Character Object)
    Integer x = 3; // This is autoboxing (auto-converting an int into an Integer Object)

    // Example 2: AutoUnboxing
    int b = new Integer(1); // This is unboxing. 
                            // The compiler simply runs: int x = new Integer(1).intValue();
    Character c = new Character('O');
    char o = c; // This is unboxing. The compiler simply runs: char o = c.charValue();

  }

}
