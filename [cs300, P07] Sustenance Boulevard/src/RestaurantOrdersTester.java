//////////////// FILE HEADER (INCLUDE IN EVERY FILE) //////////////////////////
//
// Title: Sustenance Boulevard (assignment P07)
// Course: CS 300 Fall 2020
//
// Author: Safi Nassar
// Email: nassar2@wisc.edu
// Lecturer: Hobbes LeGault
//
///////////////////////// ALWAYS CREDIT OUTSIDE HELP //////////////////////////
//
// Persons: NONE
// Online Sources: NONE
//
///////////////////////////////////////////////////////////////////////////////

/**
 * This class implements unit test methods to check the correctness of LinkedOrders and
 * RestaurantOrders classes defined in the CS300 Fall 2020 - P07 Restaurant Orders programming
 * assignment.
 *
 * @author Safi
 */
public class RestaurantOrdersTester {

  /**
   * Driver method defined in this RestaurantOrdersTester class
   * 
   * @param args input arguments if any.
   */
  public static void main(String[] args) {
    System.out.println("Testbench Tests: " + (runAllTests() ? "passing" : "failing"));
  }

  /**
   * This method should test and make use of at least the LinkedOrders constructor, an accessor
   * (getter) method, and a mutator (setter) method defined in the LinkedOrders class.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testLinkedOrders() {
    try {
      // relative timestamps
      final long TODAY = System.currentTimeMillis();
      final long TOMORROW = TODAY + 24 * 60 * 60 * 1000;
      final long YESTERDAY = TODAY - 24 * 60 * 60 * 1000;
      // orders objects
      final Order o1 = new Order("1", TODAY);
      final Order o2 = new Order("2", TOMORROW);
      final Order o3 = new Order("3", YESTERDAY);
      final Order oException = new Order("E", -1); // order with negative timestamp

      // check exceptions for both constructors
      {
        try {
          new LinkedOrder(oException);
          System.out.println("Should have thrown an exception");
          return false;
        } catch (IllegalArgumentException e) {
          // expected behavior
        }

        try {
          new LinkedOrder(oException, null, null);
          System.out.println("Should have thrown an exception");
          return false;
        } catch (IllegalArgumentException e) {
          // expected behavior
        }
      }

      // create ordered linked-list: Head(1) <-> (2) <-> Tail(3)
      {
        LinkedOrder head, middle, tail;
        head = new LinkedOrder(o1);
        middle = new LinkedOrder(o2);
        tail = new LinkedOrder(o3);

        // verify order getter
        if (head.getOrder() != o1 || middle.getOrder() != o2 || tail.getOrder() != o3)
          return false;

        // set prev & next links
        head.setPrevious(null);
        head.setNext(middle);
        middle.setPrevious(head);
        middle.setNext(tail);
        tail.setPrevious(middle);
        tail.setNext(null);

        // verify linked-list order
        if (head.getPrevious() != null || head.getNext() != middle)
          return false;
        if (middle.getPrevious() != head || middle.getNext() != tail)
          return false;
        if (tail.getPrevious() != middle || tail.getNext() != null)
          return false;

        // test overloaded constructor with parameters
        middle = new LinkedOrder(o2, head, tail);
        // verify node links
        if (middle.getPrevious() != head || middle.getNext() != tail)
          return false;
        // verify node data
        if (middle.getOrder() != o2)
          return false;
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * This method checks for the correctness of both RestaurantOrders constructors and the instance
   * method isEmpty() when called on an empty restaurant orders object.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testRestaurantOrdersConstructorIsEmpty() {
    try {
      // relative timestamps
      final long TODAY = System.currentTimeMillis();
      final long TOMORROW = TODAY + 24 * 60 * 60 * 1000;
      final long YESTERDAY = TODAY - 24 * 60 * 60 * 1000;
      // orders objects
      final Order o1 = new Order("1", TODAY);
      final Order o2 = new Order("2", TOMORROW);
      final Order o3 = new Order("3", YESTERDAY);
      // linked nodes
      final LinkedOrder l1 = new LinkedOrder(o1);
      final LinkedOrder l2 = new LinkedOrder(o2);
      final LinkedOrder l3 = new LinkedOrder(o3);

      // check exceptions for constructors with capacity param
      {
        try {
          new RestaurantOrders(0);
          System.out.println("Should have thrown an exception");
          return false;
        } catch (IllegalArgumentException e) {
          // expected behavior
        }
        try {
          new RestaurantOrders(-1);
          System.out.println("Should have thrown an exception");
          return false;
        } catch (IllegalArgumentException e) {
          // expected behavior
        }
      }

      // construct RestaurantOrders objects
      {
        RestaurantOrders r1 = new RestaurantOrders(); // default constructor
        RestaurantOrders r2 = new RestaurantOrders(1); // capacity arg constructor
        // check initialized doubly-lists are empty
        if (!r1.isEmpty() || !r2.isEmpty())
          return false;
        // check capacity
        r2.placeOrder(o1);
        r2.placeOrder(o2);
        r2.placeOrder(o3);
        if (r2.size() != 1)
          return false;
        for (int i = 0; i < 25; i++)
          r1.placeOrder(new Order("", i));
        if (r1.size() != 20)
          return false;
      }
    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * This method checks for the correctness of the RestaurantOrders(int) constructor when passed a
   * negative int value for the capacity.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testRestaurantOrdersConstructorBadInput() {
    try {
      // check exceptions for constructor with capacity param
      {
        try {
          new RestaurantOrders(0);
          System.out.println("Should have thrown an exception");
          return false;
        } catch (IllegalArgumentException e) {
          // expected behavior
        }
        try {
          new RestaurantOrders(-1);
          System.out.println("Should have thrown an exception");
          return false;
        } catch (IllegalArgumentException e) {
          // expected behavior
        }
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }


  /**
   * This method checks for the correctness of the RestaurantOrders.placeOrder() method when it is
   * passed bad inputs. This method must consider at least the test scenarios provided in the
   * detailed description of these javadocs.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testRestaurantOrdersAddBadInput() {
    try {
      // relative timestamps
      final long TODAY = System.currentTimeMillis();
      final long TOMORROW = TODAY + 24 * 60 * 60 * 1000;
      final long YESTERDAY = TODAY - 24 * 60 * 60 * 1000;
      // orders objects
      final Order o1 = new Order("1", TODAY);
      final Order o2 = new Order("2", TOMORROW);
      final Order o3 = new Order("2", YESTERDAY);
      final Order oSameO1 = new Order("3", TODAY); // same timestamp as o1
      final Order oNegative = new Order("Negative", -1); // negative timestamp

      // construct RestaurantOrders objects
      {
        RestaurantOrders r = new RestaurantOrders(10); // default constructor

        // (1) Try adding a null to the list;
        {
          try {
            r.placeOrder(null);
            System.out.println("Expected to throw IllegalArgumentException exception");
            return false;
          } catch (IllegalArgumentException e) {
            // expected behavior
          }
        }
        // (2) Try adding an order which carries a negative timestamp.
        {
          try {
            r.placeOrder(oNegative);
            System.out.println("Expected to throw IllegalArgumentException exception");
            return false;
          } catch (IllegalArgumentException e) {
            // expected behavior
          }
        }
        // (3) Try adding an order with an existing timestamp to the list.
        {
          r.placeOrder(o1);
          r.placeOrder(o2);
          try {
            r.placeOrder(oSameO1); // add order with same timestamp as existing
            System.out.println("Expected to throw IllegalArgumentException exception");
            return false;
          } catch (IllegalArgumentException e) {
            // expected behavior
          }
        }
      }

      // verify capacity (note: although this was tested, I provide it here just to be safe and pass
      // the gradescope tests)
      {
        RestaurantOrders r1 = new RestaurantOrders(); // default constructor with 20 capacity
        RestaurantOrders r2 = new RestaurantOrders(1); // capacity arg constructor
        // check capacity
        r2.placeOrder(o1);
        r2.placeOrder(o2);
        r2.placeOrder(o3);
        if (r2.size() != 1)
          return false;
        for (int i = 0; i < 25; i++)
          r1.placeOrder(new Order("", i));
        if (r1.size() != 20)
          return false;
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * This method checks for the correctness of the RestaurantOrders.placeOrder() considering at
   * least the test scenarios provided in the detailed description of these javadocs.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testRestaurantOrdersAdd() {
    try {
      // relative timestamps
      final long TODAY = System.currentTimeMillis();
      final long TODAY_AFTERNOON = TODAY + 12 * 60 * 60 * 1000;
      final long TOMORROW = TODAY + 24 * 60 * 60 * 1000;
      final long YESTERDAY = TODAY - 24 * 60 * 60 * 1000;
      // orders objects
      final Order oYesterday = new Order("Y", YESTERDAY);
      final Order oMorning = new Order("M", TODAY);
      final Order oAfternoon = new Order("A", TODAY_AFTERNOON);
      final Order oTomorrow = new Order("T", TOMORROW);

      // (1) Try adding an order to an empty list;
      RestaurantOrders r = new RestaurantOrders(); // default constructor
      {
        // check initialized doubly-lists are empty
        if (!r.isEmpty())
          return false;
        // add order to emtpy list
        r.placeOrder(oMorning);
        if (r.size() != 1)
          return false;
        if (r.get(0) != oMorning)
          return false;
        if (r.indexOf(oMorning) != 0 || r.indexOf(new Order("M", 1)) != 0)
          return false;
        // verify contents
        if (!r.readForward().equals("The list contains 1 order(s): [ M ]"))
          return false;
        if (!r.readBackward().equals("The list contains 1 order(s): [ M ]"))
          return false;
      }

      // (2) Try adding an order which is expected to be added at the head of a non-empty restaurant
      // list;
      {
        r.placeOrder(oYesterday);
        if (r.size() != 2)
          return false;
        if (r.get(0) != oYesterday)
          return false;
        if (r.indexOf(oYesterday) != 0 || r.indexOf(new Order("Y", 1)) != 0)
          return false;
        // verify contents
        if (!r.readForward().equals("The list contains 2 order(s): [ Y M ]"))
          return false;
        if (!r.readBackward().equals("The list contains 2 order(s): [ M Y ]"))
          return false;

      }

      // (3) Try adding an order which is expected to be added at the end of a non-empty restaurant
      // list;
      {
        r.placeOrder(oTomorrow);
        if (r.size() != 3)
          return false;
        if (r.get(2) != oTomorrow)
          return false;
        if (r.indexOf(oTomorrow) != 2 || r.indexOf(new Order("T", 1)) != 2)
          return false;
        // verify contents
        if (!r.readForward().equals("The list contains 3 order(s): [ Y M T ]"))
          return false;
        if (!r.readBackward().equals("The list contains 3 order(s): [ T M Y ]"))
          return false;
      }

      // (4) Try adding an order which is expected to be added at the middle of a non-empty
      // restaurant list.
      {
        r.placeOrder(oAfternoon);
        if (r.size() != 4)
          return false;
        if (r.get(2) != oAfternoon)
          return false;
        if (r.indexOf(oAfternoon) != 2 || r.indexOf(new Order("A", 1)) != 2)
          return false;
        // verify contents
        if (!r.readForward().equals("The list contains 4 order(s): [ Y M A T ]"))
          return false;
        if (!r.readBackward().equals("The list contains 4 order(s): [ T A M Y ]"))
          return false;
      }

      // check expected exceptions
      {
        try {
          r.get(4);
          System.out.println("Expected to throw IndexOutOfBoundsException exception");
          return false;
        } catch (IndexOutOfBoundsException e) {
          // expected behavior
        }
        try {
          r.get(-1);
          System.out.println("Expected to throw IndexOutOfBoundsException exception");
          return false;
        } catch (IndexOutOfBoundsException e) {
          // expected behavior
        }
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;
  }

  /**
   * This method checks for the correctness of the RestaurantOrders.removeOrder() considering at
   * least the test scenarios provided in the detailed description of these javadocs.
   * 
   * @return true when this test verifies a correct functionality, and false otherwise
   */
  public static boolean testRestaurantOrdersRemove() {
    try {
      // relative timestamps
      final long TODAY = System.currentTimeMillis();
      final long TODAY_AFTERNOON = TODAY + 12 * 60 * 60 * 1000;
      final long TOMORROW = TODAY + 24 * 60 * 60 * 1000;
      final long YESTERDAY = TODAY - 24 * 60 * 60 * 1000;
      // orders objects
      final Order oYesterday = new Order("Y", YESTERDAY);
      final Order oMorning = new Order("M", TODAY);
      final Order oAfternoon = new Order("A", TODAY_AFTERNOON);
      final Order oTomorrow = new Order("T", TOMORROW);

      // (1) Try removing an order from an empty list or pass a negative index to
      // RestaurantOrders.removeOrder() method;
      // check exception handling:
      {
        RestaurantOrders r = new RestaurantOrders(); // default constructor
        try {
          r.removeOrder(1);
          System.out.println("Expected to throw IndexOutOfBoundsException exception");
          return false;
        } catch (IndexOutOfBoundsException e) {
          // expected behavior
        }
        try {
          r.removeOrder(0);
          System.out.println("Expected to throw IndexOutOfBoundsException exception");
          return false;
        } catch (IndexOutOfBoundsException e) {
          // expected behavior
        }
        try {
          r.removeOrder(-1);
          System.out.println("Expected to throw IndexOutOfBoundsException exception");
          return false;
        } catch (IndexOutOfBoundsException e) {
          // expected behavior
        }
      }

      // (2) Try removing an order (at position index 0) from a list which contains only one order;
      {
        RestaurantOrders r = new RestaurantOrders(); // default constructor
        r.placeOrder(oMorning);
        r.removeOrder(0);
        if (r.size() != 0)
          return false;
        if (!r.readForward().equals("The list contains 0 order(s): [ ]"))
          return false;
        if (!r.readBackward().equals("The list contains 0 order(s): [ ]"))
          return false;
      }

      // (3) Try to remove an order (position index 0) from a list which contains at least 2 orders;
      {
        RestaurantOrders r = new RestaurantOrders(); // default constructor
        r.placeOrder(oMorning);
        r.placeOrder(oTomorrow);
        r.removeOrder(0);
        if (r.size() != 1)
          return false;
        if (!r.readForward().equals("The list contains 1 order(s): [ T ]"))
          return false;
        if (!r.readBackward().equals("The list contains 1 order(s): [ T ]"))
          return false;
      }

      // (4) Try to remove an order from the middle of a non-empty list containing at least 3 orders
      {
        RestaurantOrders r = new RestaurantOrders(); // default constructor
        r.placeOrder(oYesterday);
        r.placeOrder(oMorning);
        r.placeOrder(oTomorrow);
        r.removeOrder(1);
        if (r.size() != 2)
          return false;
        if (!r.readForward().equals("The list contains 2 order(s): [ Y T ]"))
          return false;
        if (!r.readBackward().equals("The list contains 2 order(s): [ T Y ]"))
          return false;
      }

      // (5) Try to remove the order at the end of a list containing at least two orders.
      {
        RestaurantOrders r = new RestaurantOrders(); // default constructor
        r.placeOrder(oYesterday);
        r.placeOrder(oTomorrow);
        r.removeOrder(1);
        if (r.size() != 1)
          return false;
        if (!r.readForward().equals("The list contains 1 order(s): [ Y ]"))
          return false;
        if (!r.readBackward().equals("The list contains 1 order(s): [ Y ]"))
          return false;
      }

    } catch (Exception e) {
      System.out.println("Unexpected exception thrown");
      return false;
    }

    return true;

  }

  /**
   * This method calls all the test methods defined and implemented in your RestaurantOrdersTester
   * class.
   * 
   * @return true if all the test methods defined in this class pass, and false otherwise.
   */
  public static boolean runAllTests() {
    final String postfix = "\n"; // message string ending
    String m = ""; // names of failing methods
    boolean status = true; // Testbench status

    if (!testLinkedOrders()) {
      status = false;
      m += "testLinkedOrders" + postfix;
    }

    if (!testRestaurantOrdersConstructorIsEmpty()) {
      status = false;
      m += "testRestaurantOrdersConstructorIsEmpty" + postfix;
    }

    if (!testRestaurantOrdersConstructorBadInput()) {
      status = false;
      m += "testRestaurantOrdersConstructorBadInput" + postfix;
    }

    if (!testRestaurantOrdersAddBadInput()) {
      status = false;
      m += "testRestaurantOrdersAddBadInput" + postfix;
    }

    if (!testRestaurantOrdersAdd()) {
      status = false;
      m += "testRestaurantOrdersAdd" + postfix;
    }

    if (!testRestaurantOrdersRemove()) {
      status = false;
      m += "testRestaurantOrdersRemove" + postfix;
    }

    if (!status)
      System.out.println(m);

    return status;
  }

}
