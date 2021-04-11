import java.util.*;

/**
 * Traversal class: used to traverse through the AST nodes, visiting each and
 * iterating over their child nodes.
 * 
 * Note: I have chosen to decouple the logic of the tree traversal from the
 * implementation of the visit methods to better control the process.
 */
public class Traverser {
    /**
     * Traverse root node
     * 
     * @param node root node of the AST, e.g. ProgramNode
     */
    public static void traverse(ASTnode node) {
        State state = new State(); // state during traversal
        traverse(node, state);
    }

    /**
     * Traverse a list of ASTnode trees
     */
    public static void traverse(List<? extends ASTnode> nodeList, State state) {
        if (nodeList == null || nodeList.isEmpty()) return;
        try {
            for (ASTnode n : nodeList)
                if (n != null) traverse(n, state);
        } catch (NoSuchElementException e) {
            System.err.println("Unexpected null exception; " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /** config traversal without a state */
    public static void traverse(Iterator<Config> iterator) {
        traverse(iterator, new State());
    }

    /**
     * Traverse over node list in scope configurations
     * 
     * @param iterator scope configurations holding a list of nodes to
     *                 traverse @
     */
    public static void traverse(Iterator<Config> iterator, State state) {
        while (iterator.hasNext()) {
            Config config = iterator.next();
            traverse(config.list, state);
        }
    }

    /**
     * Traverse ASTnode tree
     */
    public static void traverse(ASTnode n, State state) {
        if (n == null) return; // short-circuit

        // 🐞 print for debug TODO: uncomment
        System.out.println(n.getClass() + ": " + n.typeClass());
        // // System.out.println(state.toString());

        // traverse children
        if (n instanceof Node.Iterable && n.traverseSubtree)
            traverse(((Node.Iterable) n).getChildren(), state);

        // visit each node and manipulate the state when needed
        if (n instanceof Node.Visitable)
            n.typeClass = ((Node.Visitable) n).visit(state); // set result type
    }

    /**
     * Traversal state - Track states during traversal
     * 
     * implement if required to access/manipulate shared state during traversal.
     */
    public static class State {

        /**
         * state containing list of Types
         * 
         * used for expression list in a function invocation.
         */
        public static class TypeList extends State {
            List<Class<? extends Type>> typeList = new ArrayList<>();

            public TypeList(List<Type> l) {
                // convert to the a list of Type classes
                for (Type t : l)
                    typeList.add(t.classType());
            }
        }


        /**
         * state containing single type
         * 
         * used for return statement in a function body
         */
        public static class TypeValue extends State {
            Class<? extends Type> typeValue;

            public TypeValue(Type t) {
                typeValue = t.classType();
            }
        }

    }

    /**
     * Iteration configurations for list of AST nodes involving scoping
     */
    public static class Config {
        public List<? extends ASTnode> list = new ArrayList<>(); // nodes list

        public Config(List<? extends ASTnode> l) {
            this.list = l;
        }

        public Config() {
        }
    }

    /**
     * Class holding flags related to the interfaces defined for Visitable and
     * Iterable
     * 
     * (Note: I'm not sure how to share fields of interfaces using Java, so I
     * just extended the root class)
     */
    public static class Node {
        // flag to indicate whether to iterate over child nodes
        public boolean traverseSubtree = true;
        // result type of expression node. Fail if not reset by visiting
        // public Class<? extends Type> typeClass = null;
        public Class<? extends Type> typeClass = Type.class;

        // getter field
        public Class<? extends Type> typeClass() {
            return this.typeClass;
        }

        // check for type match with a Type class
        public boolean is(java.lang.Class<? extends Type> c) {
            return this.typeClass == c;

            // allows also super class, which would consider Type super class as
            // true for all subclasses
            // return this.typeClass.isAssignableFrom(c);
        }

        // check for type match with another Node
        public boolean is(Traverser.Node n) {
            return this.typeClass == n.typeClass;
        }

        /**
         * Get subtree children nodes - for further iteration over the sub-nodes
         */
        public static abstract interface Iterable {
            /**
             * Retrieve iteration configuration for children nodes of current
             * parent node
             * 
             * @return iterator of configurations for scoping for each list of
             *         child nodes.
             */
            public Iterator<Traverser.Config> getChildren();
        }

        /**
         * Nodes involved in the type analysis traversal
         */
        public static abstract interface Visitable {
            /**
             * Visit node with a state symbol table to track validity of
             * declarations
             * 
             * NOTE: for this assignment it implements a type checking
             * 
             * @param state shared state during traversal
             */
            public Class<? extends Type> visit(State state); // type checking
                                                             // implementation
        }
    }

}
