/**
 * DuplicateSymException checked exception defintion: duplicate entries in the
 * symbol HashMap
 * 
 * <p>
 * used by SymTable class
 * </p>
 * 
 * @author Safi
 */
public class DuplicateSymException extends Exception {
  /**
   * constructor: create exception without a message
   */
  public DuplicateSymException() {
    super();
  }

  /**
   * constructor: create exception with custom error message
   * 
   * @param message error message
   */
  public DuplicateSymException(String message) {
    super(message);
  }
}
