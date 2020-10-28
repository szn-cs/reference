class Circle extends Shape{
  
  private static final double PI = 3.14;
  private int radius; // radius of this circle
  
  /**
   * Creates a new Circle
   * @param radius radius of the circle to be created
   */
  public Circle(int radius) {
    this.radius = radius;
    this.setArea((int) (radius * radius *PI));
  }
  
  
}

class Rectangle extends Shape{

  private int width; // width of this rectangle
  private int height; // height of this rectangle
  
  /**
   * Creates a new rectangle
   * @param width width of the rectangle to be created
   * @param height height of the rectangle to be created
   */
  public Rectangle(int width, int height) {
    this.width = width;
    this.height = height;
    this.setArea(width * height);
  }
}

public class Shape implements Comparable<Shape>{

  private int area;// area of the shape


  /**
   * Returns the area of this shape
   * 
   * @return the area
   */
  public int getArea() {
    return area;
  }

  /**
   * Sets the area of this shape
   * 
   * @param area to set
   */
  public void setArea(int area) {
    this.area = area;
  }

  /**
   * ⭐ Compares this shape to another shape
   * 
   * @param otherShape other shape to be compared to this shape
   * @return negative integer if this shape has a smaller area than the otherShape's area, zero if
   *         this shape and otherShape have equal areas, and a positive integer if this shape has a
   *         greater area than the otherShape.
   * @throws NullPointerException if otherShape is null
   */
  @Override
  public int compareTo(Shape otherShape) {
    return this.area - otherShape.area;
  }

  public static void main(String[] args) {
    Rectangle rect = new Rectangle(5, 3); // area of rect is 15
    Circle c = new Circle(4); // area of c: 50.24 -> 50
    System.out.println("rect is greater than c: " + (rect.compareTo(c) > 0));
  }
  
}