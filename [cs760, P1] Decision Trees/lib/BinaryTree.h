
template <typename T>
class example {
public:
  // // Traverse Preorder
  // void traversePreOrder(shared_ptr<Node> temp) {
  //   cout << " " << temp->data;
  //   traversePreOrder(temp->left);
  //   traversePreOrder(temp->right);
  // }

  // // Traverse Inorder
  // void traverseInOrder(shared_ptr<Node> temp) {
  //   traverseInOrder(temp->left);
  //   cout << " " << temp->data;
  //   traverseInOrder(temp->right);
  // }

  // // Traverse Postorder
  // void traversePostOrder(shared_ptr<Node> temp) {
  //   traversePostOrder(temp->left);
  //   traversePostOrder(temp->right);
  //   cout << " " << temp->data;
  // }
};

// template <>
// void Node<T>::test() {
//   /*create root*/
//   Node root{};
//   /* following is the tree after above statement
//                   1
//                   / \
//           nullptr nullptr
//   */
//   root.left = make_shared<Node<int>>(2);
//   root.right = make_shared<Node<int>>(3);
//   /* 2 and 3 become left and right children of 1
//                           1
//                           / \
//                           2	 3
//                   / \	 / \
//                   nullptr nullptr nullptr nullptr
//   */
//   root.left->left = make_shared<Node<int>>(4);
//   /* 4 becomes left child of 2
//                   1
//                   / \
//           2	 3
//           / \	 / \
//           4 nullptr nullptr nullptr
//           / \
//   nullptr nullptr
//   */
//   cout << "preorder traversal: ";
//   traversePreOrder(make_shared<Node<int>>(root));
//   cout << "\nInorder traversal: ";
//   traverseInOrder(make_shared<Node<int>>(root));
//   cout << "\nPostorder traversal: ";
//   traversePostOrder(make_shared<Node<int>>(root));
// }
