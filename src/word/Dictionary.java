package word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

/**
 * A dictionary of correctly spelled words, stored in a hand-written
 * Binary Search Tree (no java.util collections are used for storage).
 * Words are normalized to lower case, so "Apple" and "apple" are the same word.
 */
public class Dictionary {

    /** A single BST node. Package-private so the tester can inspect pointers. */
    static class Node {
        String word;
        Node left;
        Node right;

        Node(String word) {
            this.word = word;
        }
    }

    private Node root;
    private int size;

    // Part I: required methods

    /**
     * Inserts a word into the tree as a new leaf, if it is not already present.
     * @return true if the word was inserted, false if it was a duplicate
     *         or invalid (null / empty)
     */
    public boolean insertWordNode(String word) {
        String w = normalize(word);
        if (w == null) {
            return false;
        }

        Node newNode = new Node(w);
        if (root == null) {
            root = newNode;
            size++;
            return true;
        }

        Node current = root;
        while (true) {
            int cmp = w.compareTo(current.word);
            if (cmp == 0) {
                return false; // duplicate: do not insert
            } else if (cmp < 0) {
                if (current.left == null) {
                    current.left = newNode; // attach at leaf
                    size++;
                    return true;
                }
                current = current.left;
            } else {
                if (current.right == null) {
                    current.right = newNode; // attach at leaf
                    size++;
                    return true;
                }
                current = current.right;
            }
        }
    }

    /**
     * Removes the specified word from the tree (the assignment names this
     * removal method checkWord). Handles all four scenarios:
     * (a) word not in tree, (b) leaf, (c) one child, (d) two children.
     * @return true if the word was found and removed, false if it was not in the tree
     */
    public boolean checkWord(String word) {
        String w = normalize(word);
        if (w == null || !contains(w)) {
            return false; // scenario (a)
        }
        root = removeNode(root, w);
        size--;
        return true;
    }

    /**
     * @return true if the word is spelled correctly (found in the BST),
     *         false otherwise
     */
    public boolean spellCheck(String word) {
        String w = normalize(word);
        return w != null && contains(w);
    }

    // Helper methods

    /** Recursive removal; returns the new root of the subtree. */
    private Node removeNode(Node node, String w) {
        if (node == null) {
            return null;
        }
        int cmp = w.compareTo(node.word);
        if (cmp < 0) {
            node.left = removeNode(node.left, w);
        } else if (cmp > 0) {
            node.right = removeNode(node.right, w);
        } else {
            // Found the node to delete
            if (node.left == null && node.right == null) {
                return null;                 // (b) no children
            }
            if (node.left == null) {
                return node.right;           // (c) one child (right)
            }
            if (node.right == null) {
                return node.left;            // (c) one child (left)
            }
            // (d) two children: replace with in-order successor
            Node successor = node.right;
            while (successor.left != null) {
                successor = successor.left;
            }
            node.word = successor.word;
            node.right = removeNode(node.right, successor.word);
        }
        return node;
    }

    /** Iterative search for an already-normalized word. */
    private boolean contains(String w) {
        Node current = root;
        while (current != null) {
            int cmp = w.compareTo(current.word);
            if (cmp == 0) {
                return true;
            }
            current = (cmp < 0) ? current.left : current.right;
        }
        return false;
    }

    /** Lower-cases and trims; returns null for null/blank input. */
    private static String normalize(String word) {
        if (word == null) {
            return null;
        }
        String w = word.trim().toLowerCase();
        return w.isEmpty() ? null : w;
    }

    /**
     * Builds the dictionary from a block of text. Every run of letters
     * (apostrophes allowed inside words) becomes a dictionary word.
     * @return number of new words added
     */
    public int buildFromText(String text) {
        int added = 0;
        for (String token : tokenize(text)) {
            if (insertWordNode(token)) {
                added++;
            }
        }
        return added;
    }

    /**
     * Spellchecks every word in a block of text.
     * @return the words that are NOT in the dictionary (empty if all are correct)
     */
    public List<String> findMisspelledWords(String text) {
        List<String> misspelled = new ArrayList<>();
        for (String token : tokenize(text)) {
            if (!spellCheck(token)) {
                misspelled.add(token);
            }
        }
        return misspelled;
    }

    /** Splits text into words: letters only, apostrophes allowed inside a word. */
    private static List<String> tokenize(String text) {
        List<String> words = new ArrayList<>();
        if (text == null) {
            return words;
        }
        String cleaned = text.replace('\u2019', '\'').replace('\u2018', '\''); // curly quotes
        for (String token : cleaned.split("[^A-Za-z']+")) {
            token = token.replaceAll("^'+|'+$", ""); // strip stray quotes
            if (!token.isEmpty()) {
                words.add(token);
            }
        }
        return words;
    }

    // Inspection / validation

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    /** Height of the tree (empty = 0, single node = 1). */
    public int height() {
        return height(root);
    }

    private int height(Node n) {
        return n == null ? 0 : 1 + Math.max(height(n.left), height(n.right));
    }

    /** All words in sorted (in-order) order. */
    public List<String> getSortedWords() {
        List<String> out = new ArrayList<>();
        inOrder(root, out);
        return Collections.unmodifiableList(out);
    }

    private void inOrder(Node n, List<String> out) {
        if (n == null) {
            return;
        }
        inOrder(n.left, out);
        out.add(n.word);
        inOrder(n.right, out);
    }

    /** Prints every word in sorted order, one per line. */
    public void printSorted() {
        for (String w : getSortedWords()) {
            System.out.println(w);
        }
    }

    /** Package-private root accessor for the tester. */
    Node getRoot() {
        return root;
    }

    /**
     * Verifies the BST invariants:
     *  - no cycles / shared nodes (each node is reachable by exactly one path)
     *  - every node lies strictly between its ancestors' bounds
     *    (left child smaller, right child larger, no duplicates)
     *  - the stored size matches the number of nodes
     */
    public boolean isValidBST() {
        Set<Node> visited = java.util.Collections.newSetFromMap(new IdentityHashMap<>());
        int[] count = {0};
        boolean ok = validate(root, null, null, visited, count);
        return ok && count[0] == size;
    }

    private boolean validate(Node n, String min, String max, Set<Node> visited, int[] count) {
        if (n == null) {
            return true;
        }
        if (!visited.add(n)) {
            return false; // node seen twice -> cycle or shared child
        }
        count[0]++;
        if (min != null && n.word.compareTo(min) <= 0) {
            return false;
        }
        if (max != null && n.word.compareTo(max) >= 0) {
            return false;
        }
        return validate(n.left, min, n.word, visited, count)
            && validate(n.right, n.word, max, visited, count);
    }
}

