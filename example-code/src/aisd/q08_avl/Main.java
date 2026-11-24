package aisd.q08_avl;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // ==========================================================================
        System.out.println("=== AVL Tree Example ===");

        AVLTree tree = new AVLTree();
        int[] values = {4, 2, 6, 1, 3, 5, 7};
        for (int value : values) tree.insert(value);

        System.out.println("Inserted: " + Arrays.toString(values));
        tree.printTree();
        System.out.println("Height: " + tree.height());

        // ==========================================================================
        System.out.println("=== Search Example ===");

        System.out.println("search(5): " + tree.search(5));
        System.out.println("search(9): " + tree.search(9));

        // ==========================================================================
        System.out.println("=== Delete Example ===");

        tree.delete(1); // Leaf
        System.out.println("After delete(1), leaf:");
        tree.printTree();

        tree.delete(2); // One child (3)
        System.out.println("After delete(2), one child:");
        tree.printTree();

        tree.delete(4); // Two children, including deletion of the root
        System.out.println("After delete(4), two children:");
        tree.printTree();

        // ==========================================================================
        System.out.println("=== Insert Example ===");

        AVLTree sorted = new AVLTree();
        for (int i = 1; i <= 10; i++) sorted.insert(i);

        System.out.println("After inserting 1..10:");
        sorted.printTree();
        System.out.println("Height: " + sorted.height());

        // ==========================================================================
        System.out.println("=== Rebalance After Deletion Example ===");

        AVLTree example = new AVLTree();
        for (int value : new int[]{4, 2, 5, 1, 3}) example.insert(value);

        System.out.println("Before delete(5):");
        example.printTree();

        example.delete(5); // Single right rotation; left child's balance is 0
        System.out.println("After delete(5):");
        example.printTree();
        System.out.println("Height: " + example.height());
    }
}

class AVLTree {
    private class Node {
        Node left = null;
        Node right = null;
        int value;
        int height = 0;

        public Node(int value) {
            this.value = value;
        }
    }

    private Node root = null;

    public int height() {
        return getHeight(root);
    }

    private int getHeight(Node n) {
        return n == null ? -1 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
    }

    private int getBalance(Node n) {
        return getHeight(n.left) - getHeight(n.right);
    }

    private Node rotateRight(Node n) {
        Node nl = n.left;
        Node nlr = nl.right;
        nl.right = n;
        n.left = nlr;
        updateHeight(n);
        updateHeight(nl);
        return nl;
    }

    private Node rotateLeft(Node n) {
        Node nr = n.right;
        Node nrl = nr.left;
        nr.left = n;
        n.right = nrl;
        updateHeight(n);
        updateHeight(nr);
        return nr;
    }

    private Node rebalance(Node n) {
        updateHeight(n);
        int balance = getBalance(n);

        if (balance < -1) {
            if (getBalance(n.right) > 0) {
                n.right = rotateRight(n.right);
            }
            return rotateLeft(n);
        } else if (balance > 1) {
            if (getBalance(n.left) < 0) {
                n.left = rotateLeft(n.left);
            }
            return rotateRight(n);
        }
        return n;
    }

    public boolean search(int value) {
        Node cur = root;

        while (cur != null) {
            if (cur.value > value) {
                cur = cur.left;
            } else if (cur.value == value) {
                return true;
            } else {
                cur = cur.right;
            }
        }
        return false;
    }

    public void insert(int value) {
        root = insert(root, value);
    }

    private Node insert(Node cur, int value) {
        if (cur == null) return new Node(value);

        if (value < cur.value) {
            cur.left = insert(cur.left, value);
        } else if (value > cur.value) {
            cur.right = insert(cur.right, value);
        } else {
            return cur; // Ignore duplicates
        }
        return rebalance(cur);
    }

    public void delete(int value) {
        root = delete(root, value);
    }

    private Node delete(Node cur, int value) {
        if (cur == null) return null;

        if (value < cur.value) {
            cur.left = delete(cur.left, value);
        } else if (value > cur.value) {
            cur.right = delete(cur.right, value);
        } else {
            if (cur.left == null) return cur.right;
            if (cur.right == null) return cur.left;

            // Find the leftmost node in the right subtree
            Node successor = cur.right;
            while (successor.left != null) successor = successor.left;

            cur.value = successor.value;
            cur.right = delete(cur.right, successor.value);
        }
        return rebalance(cur);
    }

    public void printTree() {
        if (root == null) {
            System.out.println("(empty)");
            return;
        }
        System.out.println(root.value);
        printTree(root.left, "", "L: ", root.right == null);
        printTree(root.right, "", "R: ", true);
    }

    private void printTree(Node cur, String prefix, String side, boolean last) {
        if (cur == null) return;

        System.out.println(prefix + (last ? "\\-- " : "|-- ") + side + cur.value);

        String nextPrefix = prefix + (last ? "    " : "|   ");
        printTree(cur.left, nextPrefix, "L: ", cur.right == null);
        printTree(cur.right, nextPrefix, "R: ", true);
    }
}
