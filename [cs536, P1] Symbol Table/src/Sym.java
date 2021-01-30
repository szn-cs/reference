/**
 * Sym class: represents a symbol entry in the symbol-table data structure.
 * 
 * symbol instances store information about each identifier retlating to its declaration or scope
 * (e.g. type, location stored at runtime)
 * 
 * @author Safi
 */
public class Sym {
  private String type; // type of identifier (e.g. "int", "double", etc.)

  /**
   * constructor: initializes the Sym to have the given type.
   * 
   * @param type
   */
  public void Sym(String type) {
    this.type = type;
  }

  /**
   * getter: this Sym's type.
   * 
   * @return identifier declaration type
   */
  String getType() {
    return this.type;
  }

  /**
   * string representation: this Sym's type
   * 
   * @return identifier declaration type
   */
  @Override
  String toString() {
    return this.type;
  }

}
