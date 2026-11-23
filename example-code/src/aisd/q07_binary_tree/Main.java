
package aisd.q07_binary_tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // ==========================================================================
        System.out.println("=== Binary Search Tree Example ===");

        BinaryTree tree = new BinaryTree();
        int[] values = {4, 2, 6, 1, 3, 5, 7};
        for (int value : values) tree.insert(value);

        System.out.println("Inserted: " + Arrays.toString(values));
        System.out.println("Preorder:  " + tree.preorder());
        System.out.println("Inorder:   " + tree.inorder());
        System.out.println("Postorder: " + tree.postorder());

        // ==========================================================================
        System.out.println("=== Search Example ===");

        System.out.println("search(5): " + tree.search(5));
        System.out.println("search(9): " + tree.search(9));

        // ==========================================================================
        System.out.println("=== Delete Example ===");

        tree.delete(1); // Leaf
        System.out.println("After delete(1), leaf:         " + tree.inorder());

        tree.delete(2); // One child (3)
        System.out.println("After delete(2), one child:    " + tree.inorder());

        tree.delete(4); // Two children, including deletion of the root
        System.out.println("After delete(4), two children: " + tree.inorder());
        // ==========================================================================
        System.out.println("=== Bamboo Example ===");

        BinaryTree bamboo = new BinaryTree();
        for (int i = 1; i <= 10; i++) bamboo.insert(i);

        System.out.println("Height after inserting 1..10: " + bamboo.height());
    }
}

class BinaryTree {
    private class Node {
        Node left = null;
        Node right = null;
        final int value;

        public Node(int value) {
            this.value = value;
        }
    }

    private Node root = null;

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
        if (root == null) {
            root = new Node(value);
            return;
        }

        Node cur = root;
        Node prev = null;

        while (cur != null) {
            prev = cur;
            if (value <= cur.value) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        if (value <= prev.value) {
            prev.left = new Node(value);
        } else {
            prev.right = new Node(value);
        }
    }

    public void delete(int value) {
        Node cur = root;
        Node prev = null;

        while (cur != null && cur.value != value) {
            prev = cur;
            if (value < cur.value) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }

        if (cur == null) return;

        Node replacement;
        if (cur.left == null) {
            replacement = cur.right; // No left child (includes leaf)
        } else if (cur.right == null) {
            replacement = cur.left; // Only left child
        } else {
            // Find the leftmost node in the right subtree
            Node successor = cur.right;
            Node successorParent = cur;

            while (successor.left != null) {
                successorParent = successor;
                successor = successor.left;
            }

            if (successorParent != cur) {
                successorParent.left = successor.right;
                successor.right = cur.right;
            }

            successor.left = cur.left;
            replacement = successor;
        }

        if (prev == null) {
            root = replacement;
        } else if (prev.left == cur) {
            prev.left = replacement;
        } else {
            prev.right = replacement;
        }
    }

    public List<Integer> preorder() {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private void preorder(Node cur, List<Integer> result) {
        if (cur == null) return;
        result.add(cur.value);
        preorder(cur.left, result);
        preorder(cur.right, result);
    }

    public List<Integer> inorder() {
        List<Integer> result = new ArrayList<>();
        inorder(root, result);
        return result;
    }

    private void inorder(Node cur, List<Integer> result) {
        if (cur == null) return;
        inorder(cur.left, result);
        result.add(cur.value);
        inorder(cur.right, result);
    }

    public List<Integer> postorder() {
        List<Integer> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private void postorder(Node cur, List<Integer> result) {
        if (cur == null) return;
        postorder(cur.left, result);
        postorder(cur.right, result);
        result.add(cur.value);
    }

    public int height() {
        return height(root);
    }

    private int height(Node cur) {
        if (cur == null) return 0;
        return 1 + Math.max(height(cur.left), height(cur.right));
    }
}
