int main(int argc, char** argv) {
  int x{0};
  int y{0};

  int&& x = (x + y);  // extend lifetime of rvalue

  return 0;
}