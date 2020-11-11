//File header comes here
/**
* This class implements unit test methods to check the correctness of LinkedOrders and RestaurantOrders
* classes defined in the CS300 Fall 2020 - P07 Restaurant Orders programming assignment.
*
*/
public class RestaurantOrdersTester {

/**
* This method should test and make use of at least the LinkedOrders constructor, an accessor
* (getter) method, and a mutator (setter) method defined in the LinkedOrders class.
* 
* @return true when this test verifies a correct functionality, and false otherwise
*/
public static boolean testLinkedOrders() {
 return false;
}

/**
* This method checks for the correctness of both RestaurantOrders constructors and the instance
* method isEmpty() when called on an empty restaurant orders object.
* 
* @return true when this test verifies a correct functionality, and false otherwise
*/
public static boolean testRestaurantOrdersConstructorIsEmpty() {
 return false;
}

/**
* This method checks for the correctness of the RestaurantOrders(int) constructor when passed a
* negative int value for the capacity.
* 
* @return true when this test verifies a correct functionality, and false otherwise
*/
public static boolean testRestaurantOrdersConstructorBadInput() {
 return false;
}


/**
* This method checks for the correctness of the RestaurantOrders.placeOrder()() method when it is passed bad
* inputs. This method must consider at least the test scenarios provided in the detailed
* description of these javadocs. (1) Try adding a null to the list; (2) Try adding an order which
* carries a negative timestamp. (3) Try adding an order with an existing timestamp to the list.
* 
* @return true when this test verifies a correct functionality, and false otherwise
*/
public static boolean testRestaurantOrdersAddBadInput() {
 return false;
}

/**
* This method checks for the correctness of the RestaurantOrders.placeOrder()() considering at least the test
* scenarios provided in the detailed description of these javadocs. (1) Try adding an order to an
* empty list; (2) Try adding an order which is expected to be added at the head of a non-empty
* restaurant list; (3) Try adding an order which is expected to be added at the end of a non-empty
* restaurant list; (4) Try adding an order which is expected to be added at the middle of a non-empty
* restaurant list. For each of those scenarios, make sure that the size of the list is
* appropriately updated after a call without errors of the add() method, and that the contents of
* the list is as expected whatever if list is read in the forward or backward directions. You can
* also check the correctness of RestaurantOrders.get(int), RestaurantOrders.indexOf(Order), and
* RestaurantOrders.size() in this test method.
* 
* @return true when this test verifies a correct functionality, and false otherwise
*/
public static boolean testRestaurantOrdersAdd() {
 return false;
}

/**
* This method checks for the correctness of the RestaurantOrders.removeOrder() considering at least the
* test scenarios provided in the detailed description of these javadocs. (1) Try removing an order
* from an empty list or pass a negative index to RestaurantOrders.removeOrder() method; (2) Try removing an
* order (at position index 0) from a list which contains only one order; (3) Try to remove an order
* (position index 0) from a list which contains at least 2 orders; (4) Try to remove an order from
* the middle of a non-empty list containing at least 3 orders; (5) Try to remove the order at the
* end of a list containing at least two orders. For each of those scenarios, make sure that the 
* size of the list is appropriately updated after a call without errors of the add() method, 
* and that the contents of the list is as expected whatever if list is read in the forward or 
* backward directions.
* 
* @return true when this test verifies a correct functionality, and false otherwise
*/
public static boolean testRestaurantOrdersRemove() {
 return false;
}


/**
* This method calls all the test methods defined and implemented in your RestaurantOrdersTester
* class.
* 
* @return true if all the test methods defined in this class pass, and false otherwise.
*/
public static boolean runAllTests() {
 return false;
}

/**
* Driver method defined in this RestaurantOrdersTester class
* 
* @param args input arguments if any.
*/
public static void main(String[] args) {

}
}