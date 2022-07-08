#include <ios>  // for <streamsize>
#include <iostream>
#include <limits>  // numeric_limits

using namespace std;

int main(int, char**) {
  int age{0};
  string name{"ABC"};
  string full_name{"ABC DFG"};

  // cin leaves space and \n after consumed character on the buffer
  cout << "name and age" << endl;
  cin >> name >> age;

  // discards the input buffer
  cin.ignore(numeric_limits<streamsize>::max(), '\n');

  cout << "full name" << endl;
  getline(cin, full_name);

  cout << "full name overwrite" << endl;
  getline(cin, full_name);

  cout << "Name: " << name << endl << "Age: " << age << endl;
  cout << "Full name: " << full_name << endl;

  return 0;
}