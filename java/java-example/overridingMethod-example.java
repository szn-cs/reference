package sample;


public class PictureList {
	public static void main(String []args) {
		Picture p1 = new Picture(8, 11, "family");
		Picture p2 = new Picture(4, 6, "kids");
		Picture p3 = new Picture(4, 6, "vacation");
		
		if ( p1.equals(p2)) {
			System.out.println( p1 + " equals " + p2);
		} else {
			System.out.println( p1 + " Not equals " + p2);
		}
		if ( p2.equals(p3)) {
			System.out.println( p2 + " equals " + p3);
		} else {
			System.out.println( p2 + " Not equals " + p3);
		}
		
	}
}

class Picture {
	private int width;
	private int height;
	private String content;

	public Picture(int width, int height, String content) {
		this.width = width;
		this.height = height;
		this.content = content;
	}

	// what does it mean for 2 Picture objects/instances to be equal?
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Picture) {
			Picture p = (Picture) obj;
			return p.width == this.width && p.height == this.height;
		} else {
			return false;
		}
	}
	
	@Override  //compiler directive 
	public String toString() {
		//return "Picture: " +super.toString();
		return this.content + " " + width +"x"+height;
	}
}
