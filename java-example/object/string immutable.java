public class Strings {

  public static void main(String[] args) {

    String greetingMessage = "Welcome"; // greetingMessage refers to a new String
                                        // object of state "Welcome".
    String s = greetingMessage; // a shallow copy of greetingMessage

    greetingMessage.toUpperCase(); // a new string created as result of calling .toUpperCase()
                                   // method.
                                   // But, its reference has not been stored anywhere
    System.out.println(greetingMessage); // "Welcome" displayed.
                                         // No changes have been made to greetingMessage
                                         // Keep in mind that strings are immutable objects in Java
    
    greetingMessage = greetingMessage.toUpperCase();
    System.out.println(greetingMessage); // "WELCOME" is now displayed

    String msg = "WELCOME";
    boolean equal = msg == greetingMessage; // "==" compares references
    // equal: false
    boolean compare1 = msg.equals(greetingMessage); // .equals() compares objects
    // compare1: true
    boolean compare2 = msg.equals(s); // String.equals() performs a case sensitive comparison
    // compare2: false
    boolean compare3 = msg.equalsIgnoreCase(s); // String.equalsIgnoreCase() performs 
                                                // a case insensitive comparison
    // compare3: true

  }

}
