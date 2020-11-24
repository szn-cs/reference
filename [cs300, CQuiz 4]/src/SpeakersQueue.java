///////////////////////// CUMULATIVE QUIZ FILE HEADER //////////////////////////
// Full Name: Safi Nassar
// Campus ID: 9082786709
// WiscEmail: nassar2@wisc.edu
////////////////////////////////////////////////////////////////////////////////

/**
 * This class models a circular queue which stores the names of speakers who take turns to
 * contribute in a conversation.
 *
 */
public class SpeakersQueue {

  private String[] names; // circular-indexing array which stores names of speakers
  private int headIndex; // index of the name of the speaker at the front of this circular queue
  private int tailIndex; // index of the next available position in this queue (next
                         // available position with respect to the back of this queue)
  private int size; // number of speakers stored in this queue

  // Note that when the queue is not empty, speakers are stored in the range of indexes
  // [headIndex .. tailIndex] excluding tailIndex in the circular-indexing array names.

  /**
   * Creates an empty array-based queue of speaker names.
   * 
   * BE CAREFUL: in this queue implementation, the tail index indicates the next available position
   * rather than the index of the element stored at the back of the queue.
   * 
   * @param capacity the maximum number of speakers that can be in the queue at once
   */
  public SpeakersQueue(int capacity) {
    // if the provided queue capacity is 0 or negative, throw an IllegalArgumentException
    if (capacity <= 0)
      throw new IllegalArgumentException();

    // initialize the private data fields for this Speakers object to represent an empty queue
    this.names = new String[capacity];
    this.headIndex = -1;
    this.tailIndex = 0;
    this.size = 0;
  }

  /**
   * Determines whether the queue is currently empty
   * 
   * @return true if there are no names in the queue, false otherwise
   */
  public boolean isEmpty() {
    if (this.size == 0)
      return true;
    else
      return false;
  }

  /**
   * Returns the next speaker in the queue without removing the name
   * 
   * @return null if nothing in the queue, otherwise the speaker name at the front of the queue
   */
  public String peek() {
    if (isEmpty())
      return null;
    return names[headIndex];
  }

  /**
   * Adds a new speaker to the back of the queue
   * 
   * @param newName the name to add
   */
  public void enqueue(String newName) {
    // if there is no more space to add a name, throw an IllegalStateException
    if (size >= names.length)
      throw new IllegalStateException();
    if (isEmpty()) // reset both to position 0
      headIndex = tailIndex = 0;

    // add the new name to the back of the queue and update your indexes and size
    // remember to wrap the indexes around the end of the array if necessary!
    names[tailIndex] = newName;
    tailIndex = (tailIndex + 1) % names.length;
    size++;
  }

  /**
   * Removes and returns the next speaker in the queue
   * 
   * @return null if nothing in the queue, otherwise the speaker name at the front of the queue
   */
  public String dequeue() {
    if (isEmpty())
      return null;
    // remove and return the name at the front of the queue, and update your indexes and size
    // remember to wrap the indexes around the end of the array if necessary!
    String s = names[headIndex];
    names[headIndex] = null;
    headIndex = (headIndex + 1) % names.length;
    size--;
    return s;
  }

  /**
   * Creates and returns a string representation of the queue from front to back
   * 
   * Examples: Queue: [ ] // an empty queue Queue: [ Deb ] // a queue with one name Queue: [ Deb Jim
   * Beck ] // the same queue after two more names were added
   */
  @Override
  public String toString() {
    String repr = "Queue: [ ";

    // add the names in the queue, ordered from head to tail, and separated by spaces
    for (int i = size, h = headIndex; i > 0; i--, h = (h + 1) % names.length)
      repr += names[h] + " ";

    repr += "]";
    return repr;
  }

  /**
   * A basic method demonstrating some functionality of the queue.
   * 
   * Expected output for each method call is given in the inline comments.
   */
  public static void demo() {
    // basic functionality:
    SpeakersQueue test = new SpeakersQueue(5);
    System.out.println(test); // should be: [ ]
    System.out.println("Is Empty? " + test.isEmpty()); // should be: true

    // test the enqueue method:
    test.enqueue("Deb");
    System.out.println("Is Empty? " + test.isEmpty()); // should be: false
    test.enqueue("Jim");
    test.enqueue("Beck");
    System.out.println(test); // should be: [ Deb Jim Beck ]

    // test the dequeue method:
    System.out.println("Next: " + test.dequeue()); // should be: Deb
    test.enqueue("Hobbes");
    test.enqueue("Mouna");
    System.out.println(test); // should be: [ Jim Beck Hobbes Mouna ]
    System.out.println("Next: " + test.dequeue()); // should be: Jim
    System.out.println("Next: " + test.dequeue()); // should be: Beck

    // the queue only has two names, this should not throw an exception!
    test.enqueue("Gary");
    System.out.println(test); // should be: [ Hobbes Mouna Gary ]

    // empty out the queue:
    System.out.println("Next: " + test.dequeue()); // should be: Hobbes
    System.out.println("Next: " + test.dequeue()); // should be: Mouna
    System.out.println("Next: " + test.dequeue()); // should be: Gary
    System.out.println("Is Empty? " + test.isEmpty()); // should be: true
  }

  public static void main(String[] args) {
    demo();
  }

}
