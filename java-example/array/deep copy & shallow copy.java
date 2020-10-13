import java.util.Arrays;

public class Review2 {
   
   public static int[] deepCopy(int[] original){
      int[] deep = new int[original.length];
      for(int i=0; i < original.length; i++)
         deep[i] = original[i];
      return deep;
   }
   
   public static void main(String[] args) {
      // This is an example of a perfect size array
      int[] data = new int[]{1, 10, 20, 30, 0, 0};
      // shallow copy of data
      int[] shallow = data;
      // create a deep copy of data
      int[] deepCopy = Arrays.copyOf(data, data.length);
      int[] deep = deepCopy(data);
      
      // Arrays are mutable objects
      data[0] = 100;
      data[4] = 40;
      shallow[5] = 50;
      
      // String objects are immutable
      String msg = "Hello";
      // shallow copy of msg
      String msgCopy = msg;
      msg = msg.toUpperCase(); // msg refers to a new String object 
                               // returned by toUpperCase() method.
                               // msgCopy is no longer a shallow copy
                               // of msg
      
      int[] data = new int[] {10, 20, 30, 40};
      int[] copy = data; // shallowest copy of data
      copy[1] = 100;

      // create a deep copy of the original array data
      int[] deep = new int[data.length];
      // copy the contents of data into deep
      for (int i = 0; i < data.length; i++)
      deep[i] = data[i];

      deep[0] = 50;
      data[2] = 300;
      int[] deepCopy = Arrays.copyOf(data, data.length); // create a deep copy of data
      data = Arrays.copyOf(data, data.length * 2); // expand the capacity of data array
                             
   }
}
