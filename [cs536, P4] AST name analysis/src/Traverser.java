import java.util.*;

/**
 * Traversal class: used to traverse through the AST nodes, visiting each and
 * iterating over their child nodes. It manipulates a symTable used to track
 * valid symbols in the program.
 * 
 * Note: I have chosen to decouple the logic of the tree traversal from the
 * implementation of the visit methods to better control the process.
 */
public class Traverser {
    /**
     * Traverse root node
     * 
     * @param node
     */
    public static void traverse(ASTnode node) throws EmptySymTableException {
        // tracks current scopes state during traveral of the AST (list of
        // symbol hashtables each corresponding to a scope level)
        SymTable state = new SymTable(1);
        traverse(node, state);
    }

    /**
     * Traverse a list of ASTnode trees
     */
    public static void traverse(List<? extends ASTnode> nodeList,
            SymTable state) throws EmptySymTableException {
        for (ASTnode n : nodeList)
            if (n != null)
                traverse(n, state);
    }

    /**
     * Traverse ASTnode tree
     */
    public static void traverse(ASTnode n, SymTable state)
            throws EmptySymTableException {
        // visit each node and manipulate the state of the scope chain and
        // symbols hashtables as needed.
        if (n instanceof Visitable)
            try {
                ((Visitable) n).visit(state);
            } catch (Exception e) {
                System.out.println(e.toString());
                System.exit(-1);
            }


        // traverse children
        if (n instanceof Iterable && n.traverseSubtree)
            traverseConfig(((Iterable) n).getChildren(), state);

        // System.out.println(n.toString() + " visited");
        // state.print();
    }

    /**
     * Traverse over node list in scope configurations
     * 
     * @param iterator scope configurations holding a list of nodes to traverse
     * @throws EmptySymTableException
     */
    public static void traverseConfig(Iterator<Config> iterator, SymTable state)
            throws EmptySymTableException {

        while (iterator.hasNext()) {
            Config config = iterator.next();

            if (config.newScope)
                state.addScope(); // create new scope for children nodes

            if (config.list != null && !config.list.isEmpty())
                traverse(config.list, state);

            if (config.newScope)
                state.removeScope(); // discard scope
        }
    }

    /**
     * Iteration configurations for list of AST nodes involving scoping
     */
    public static class Config {
        public boolean newScope = false; // if should create a new scope
        public List<? extends ASTnode> list = new ArrayList<>(); // nodes list

        public Config(List<? extends ASTnode> l, boolean scope) {
            this.newScope = scope;
            this.list = l;
        }

        public Config() {
        }
    }
}


