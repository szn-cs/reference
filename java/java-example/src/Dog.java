class Dog extends Animal {
  public void bark() {
    System.out.println("Bark.");
  }

  private void speak() {
    System.out.println("Hello, I'm a dog!");
  }

  public static void main(String[] args) {
    Dog x = new Dog();
  }
}
