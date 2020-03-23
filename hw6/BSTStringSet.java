import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Implementation of a BST based String Set.
 * @author
 */
public class BSTStringSet implements StringSet, Iterable<String>, SortedStringSet{
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    @Override
    public void put(String s) {
        _root = putHelper(s, _root);
    }

    /** Helper method for put, add S into P's tree,
     * return the root node after adding. */
    public Node putHelper(String s, Node p) {
        if (p == null) {
            return new Node(s);
        }
        if (s.compareTo(p.s) < 0) {
            p.left = putHelper(s, p.left);
        } else {
            p.right = putHelper(s, p.right);
        }
        return p;
    }

    @Override
    public boolean contains(String s) {
        return containsHelper(s, _root);
    }

    /** Helper method for contains, return true if
     * the tree of P contains string S */
    public boolean containsHelper(String s, Node p) {
        if (p == null) {
            return false;
        }
        if (s.compareTo(p.s) == 0) {
            return true;
        } else if (s.compareTo(p.s) < 0) {
            return containsHelper(s, p.left);
        } else {
            return containsHelper(s, p.right);
        }
    }
    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (String s : this) {
            result.add(s);
        }
        return result;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    /** Inorder iterator over BSTs. */
    private static class inorderBSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();
        private String _low;
        private String _high;

        /** A new iterator over the labels in NODE. */
        inorderBSTIterator(String low, String high, Node node) {
            _low = low;
            _high = high;
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Node node = _toDo.pop();
            addTree(node.right);
            while (node.s.compareTo(_low) <0) {
                node = _toDo.pop();
                addTree(node.right);
            }
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            if (node != null) {
                if (node.s.compareTo(_high) >= 0) {
                    addTree(node.left);
                } else if (node.s.compareTo(_low) < 0) {
                    addTree(node.right);
                } else {
                    _toDo.push(node);
                    addTree(node.left);
                }
            }
        }
    }

    @Override
    public Iterator<String> iterator(String low, String high) {
        return new inorderBSTIterator(low, high, _root);
    }

    /** Root node of the tree. */
    private Node _root;

}