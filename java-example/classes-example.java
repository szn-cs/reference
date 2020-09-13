package sample;

//you can extend from, but not instantiate an abstract class
public abstract class Shape {   //implicitly extends Object

	abstract public double getArea();  //abstract since no method body
}


public class Rectangle extends Shape {  
    //instance variables aka fields and attributes
    private double height;
    private double width;
    private static int count = 0;  //class variable aka class fields and attributes
    
    public Rectangle() {  //no-arg constructor
        this( 1.0, 1.0); //call the 2-arg constructor
    }
    public Rectangle( double size) { //1-arg constructor
        this( size, size); //call the 2-arg constructor
    }
    public Rectangle(double height, double width) {  //2-arg constructor
        this.height = height;
        this.width = width;
        count++;  //class (static) variables can be accessed from instance methods and constructors
    }
    
    public double getArea() {  //derived field
        return height * width;
    }
    
    public double getPerimeter() { //another derived field
        return height * 2 + width * 2;
    }
    
    public double getHeight() {
        return this.height;
    }
    
    //setter/mutator for height that validates any changes to height
    public void setHeight(double newHeight) {
        if ( newHeight > 0.0) {
            this.height = newHeight;
        }
    }
    
    //accessor for class field
    public static int getCount() {
        //instance fields cannot be accessed from class (static) methods
        //as which instance isn't defined - no implicit this parameter
        return count;
    }

    @Override
    public String toString() {
        return "Rectangle height=" + height + " width=" + width;
    }
}


public class Circle extends Shape {
	private double radius;
	
    public Circle( double radius) { 
    	this.radius = radius;
    }

    public double getCircumference() {
    	return Math.PI * radius * 2;
    }
    
	@Override
	public double getArea() {
		return Math.PI * radius * radius;
	}
	
	@Override
	public String toString() {
		return "Circle radius:" + this.radius;
	}
}

import java.util.ArrayList;
import java.util.Scanner;

public class Shapes {

	public static void main(String[] args) {
		System.out.println("Welcome to Shapes");
		final String PROMPT = "Enter\n  rectangle width height\n  circle radius\n  list";

		Scanner input = new Scanner(System.in);
		ArrayList<Shape> list = new ArrayList<>();
		System.out.println(PROMPT);

		while (input.hasNext()) {
			String command = input.next().toLowerCase();

			// since using .next() there will be at least 1 character
			switch (command.charAt(0)) {
			case 'r':
				if (input.hasNextDouble()) {
					double width = input.nextDouble();
					if (input.hasNextDouble()) {
						double height = input.nextDouble();
						list.add(new Rectangle(width, height));
					} else {
						list.add(new Rectangle(width));
					}
				}
				break;
			case 'c':
				if ( input.hasNextDouble()) {
					double radius = input.nextDouble();
					list.add( new Circle( radius));
				} 
				break;
			case 'l':
				for (Shape shape : list) {
					System.out.printf("area:%.2f\ttoString:%s\n", shape.getArea(), shape);
				}
				break;
			default:
				System.out.println(PROMPT);
				break;
			}

		}
		input.close();
	}
}



