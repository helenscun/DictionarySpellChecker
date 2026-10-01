package word;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Dictionarytest {

    private Dictionary dict;

    /** Builds this tree:        m
     *                         /   \
     *                        f     t
     *                       / \   / \
     *                      b   h p   w
     */
    @BeforeEach
    void setUp() {
        dict = new Dictionary();
        for (String w : new String[] {"m", "f", "t", "b", "h", "p", "w"}) {
            assertTrue(dict.insertWordNode(w));
        }
        assertValid(dict);
    }

    private static void assertValid(Dictionary d) {
        assertTrue(d.isValidBST(), "BST invariants violated");
        List<String> sorted = new ArrayList<>(d.getSortedWords());
        List<String> expected = new ArrayList<>(sorted);
        Collections.sort(expected);
        assertEquals(expected, sorted, "in-order traversal must be sorted");
        assertEquals(d.size(), sorted.size(), "size must match node count");
        assertEquals(sorted.size(), new java.util.HashSet<>(sorted).size(), "no duplicates");
    }

    // ---------------- insertWordNode ----------------

    @Test
    void insertIntoEmptyTreeCreatesRoot() {
        Dictionary d = new Dictionary();
        assertTrue(d.isEmpty());
        assertTrue(d.insertWordNode("apple"));
        assertNotNull(d.getRoot());
        assertEquals("apple", d.getRoot().word);
        assertNull(d.getRoot().left);
        assertNull(d.getRoot().right);
        assertEquals(1, d.size());
        assertValid(d);
    }

    @Test
    void insertedNodesAreLeavesWithCorrectPointers() {
        assertTrue(dict.insertWordNode("a")); // left of b
        assertTrue(dict.insertWordNode("c")); // right of b
        assertTrue(dict.insertWordNode("z")); // right of w

        Dictionary.Node b = dict.getRoot().left.left;
        assertEquals("b", b.word);
        assertEquals("a", b.left.word);
        assertEquals("c", b.right.word);
        assertNull(b.left.left);
        assertNull(b.left.right);
        assertNull(b.right.left);
        assertNull(b.right.right);

        Dictionary.Node w = dict.getRoot().right.right;
        assertEquals("z", w.right.word);
        assertNull(w.left);
        assertEquals(10, dict.size());
        assertValid(dict);
    }

    @Test
    void existingNodesKeepTheirPointersAfterInsert() {
        Dictionary.Node root = dict.getRoot();
        Dictionary.Node left = root.left;
        Dictionary.Node right = root.right;
        dict.insertWordNode("k");
        assertSame(root, dict.getRoot());
        assertSame(left, root.left);
        assertSame(right, root.right);
        assertValid(dict);
    }

    @Test
    void duplicatesAreRejected() {
        int before = dict.size();
        assertFalse(dict.insertWordNode("m"));
        assertFalse(dict.insertWordNode("b"));
        assertEquals(before, dict.size());
        assertValid(dict);
    }

    @Test
    void insertIsCaseInsensitiveAndTrimmed() {
        assertTrue(dict.insertWordNode("Apple"));
        assertFalse(dict.insertWordNode("APPLE"));
        assertFalse(dict.insertWordNode("  apple "));
        assertTrue(dict.spellCheck("aPpLe"));
        assertValid(dict);
    }

    @Test
    void invalidInputIsRejected() {
        int before = dict.size();
        assertFalse(dict.insertWordNode(null));
        assertFalse(dict.insertWordNode(""));
        assertFalse(dict.insertWordNode("   "));
        assertEquals(before, dict.size());
        assertValid(dict);
    }

    @Test
    void sortedInsertionsFormAChainWithoutCycles() {
        Dictionary d = new Dictionary();
        for (char c = 'a'; c <= 'z'; c++) {
            assertTrue(d.insertWordNode(String.valueOf(c)));
            assertValid(d);
        }
        assertEquals(26, d.size());
        assertEquals(26, d.height());
    }

    @Test
    void everyParentIsConsistentWithItsChildren() {
        assertChildOrdering(dict.getRoot());
    }

    private void assertChildOrdering(Dictionary.Node n) {
        if (n == null) return;
        if (n.left != null) {
            assertTrue(n.left.word.compareTo(n.word) < 0, "left child must be smaller");
        }
        if (n.right != null) {
            assertTrue(n.right.word.compareTo(n.word) > 0, "right child must be larger");
        }
        assertChildOrdering(n.left);
        assertChildOrdering(n.right);
    }

    // ---------------- checkWord (removal) ----------------

    @Test
    void removeWordNotInTree() {
        int before = dict.size();
        assertFalse(dict.checkWord("zebra"));
        assertFalse(dict.checkWord(null));
        assertEquals(before, dict.size());
        assertEquals(Arrays.asList("b", "f", "h", "m", "p", "t", "w"), dict.getSortedWords());
        assertValid(dict);
    }

    @Test
    void removeFromEmptyTree() {
        Dictionary d = new Dictionary();
        assertFalse(d.checkWord("a"));
        assertTrue(d.isValidBST());
    }

    @Test
    void removeLeafNode() {
        assertTrue(dict.checkWord("b"));
        assertNull(dict.getRoot().left.left);
        assertNotNull(dict.getRoot().left.right);
        assertFalse(dict.spellCheck("b"));
        assertEquals(6, dict.size());
        assertValid(dict);
    }

    @Test
    void removeNodeWithOnlyLeftChild() {
        dict.checkWord("h");          // f now has only left child b
        assertTrue(dict.checkWord("f"));
        assertEquals("b", dict.getRoot().left.word);
        assertNull(dict.getRoot().left.left);
        assertNull(dict.getRoot().left.right);
        assertEquals(5, dict.size());
        assertValid(dict);
    }

    @Test
    void removeNodeWithOnlyRightChild() {
        dict.checkWord("b");          // f now has only right child h
        assertTrue(dict.checkWord("f"));
        assertEquals("h", dict.getRoot().left.word);
        assertEquals(5, dict.size());
        assertValid(dict);
    }

    @Test
    void removeNodeWithTwoChildren() {
        assertTrue(dict.checkWord("f")); // successor is h
        Dictionary.Node replaced = dict.getRoot().left;
        assertEquals("h", replaced.word);
        assertEquals("b", replaced.left.word);
        assertNull(replaced.right);
        assertFalse(dict.spellCheck("f"));
        assertEquals(6, dict.size());
        assertValid(dict);
    }

    @Test
    void removeRootWithTwoChildren() {
        assertTrue(dict.checkWord("m")); // successor is p
        assertEquals("p", dict.getRoot().word);
        assertEquals("f", dict.getRoot().left.word);
        assertEquals("t", dict.getRoot().right.word);
        assertNull(dict.getRoot().right.left);
        assertEquals(6, dict.size());
        assertValid(dict);
    }

    @Test
    void removeRootWithOneChildAndOnlyNode() {
        Dictionary d = new Dictionary();
        d.insertWordNode("b");
        d.insertWordNode("a");
        assertTrue(d.checkWord("b"));
        assertEquals("a", d.getRoot().word);
        assertValid(d);
        assertTrue(d.checkWord("a"));
        assertNull(d.getRoot());
        assertTrue(d.isEmpty());
        assertEquals(0, d.size());
        assertValid(d);
    }

    @Test
    void removeEveryWordOneAtATime() {
        List<String> words = new ArrayList<>(dict.getSortedWords());
        for (String w : words) {
            assertTrue(dict.checkWord(w));
            assertFalse(dict.spellCheck(w));
            assertValid(dict);
        }
        assertTrue(dict.isEmpty());
    }

    @Test
    void removeThenReinsert() {
        assertTrue(dict.checkWord("m"));
        assertTrue(dict.insertWordNode("m"));
        assertTrue(dict.spellCheck("m"));
        assertEquals(7, dict.size());
        assertValid(dict);
    }

    @Test
    void doubleRemoveFailsTheSecondTime() {
        assertTrue(dict.checkWord("t"));
        assertFalse(dict.checkWord("t"));
        assertValid(dict);
    }

    // ---------------- spellCheck ----------------

    @Test
    void spellCheckFindsEveryInsertedWord() {
        for (String w : new String[] {"m", "f", "t", "b", "h", "p", "w"}) {
            assertTrue(dict.spellCheck(w), w);
        }
    }

    @Test
    void spellCheckRejectsUnknownAndInvalidWords() {
        assertFalse(dict.spellCheck("zebra"));
        assertFalse(dict.spellCheck("mm"));
        assertFalse(dict.spellCheck(""));
        assertFalse(dict.spellCheck(null));
    }

    // ---------------- buildFromText / sorted display ----------------

    @Test
    void buildFromParagraphAndVerifySortedOrder() {
        Dictionary d = new Dictionary();
        int added = d.buildFromText(Main.SAMPLE_PARAGRAPH);
        assertEquals(29, added); // "Sunny" appears twice but is stored once
        assertEquals(Arrays.asList(
            "a", "angels", "be", "behind", "burger", "buy", "chocolate", "donut",
            "dubai", "edition", "finna", "girl", "in", "labubu", "limited", "mama",
            "me", "nintendo", "onika", "pistachio", "pit", "sonny", "sunny", "switch",
            "the", "there's", "vanilla", "we", "you"), d.getSortedWords());
        assertValid(d);
    }

    @Test
    void sampleParagraphDictionaryIsValidAndSorted() {
        Dictionary d = new Dictionary();
        int added = d.buildFromText(Main.SAMPLE_PARAGRAPH);
        assertEquals(29, added);
        assertEquals(29, d.size());
        assertValid(d);
        // Spot-check sorted order at both ends
        assertEquals("a", d.getSortedWords().get(0));
        assertEquals("you", d.getSortedWords().get(28));
        // Curly apostrophe is normalized, so "there's" is one word
        assertTrue(d.spellCheck("there's"));
    }

    @Test
    void originalSentenceHasNoMisspellings() {
        Dictionary d = new Dictionary();
        d.buildFromText(Main.SAMPLE_PARAGRAPH);
        assertTrue(d.findMisspelledWords(Main.SAMPLE_PARAGRAPH).isEmpty());
    }

    @Test
    void misspelledWordsAreDetected() {
        Dictionary d = new Dictionary();
        d.buildFromText(Main.SAMPLE_PARAGRAPH);
        List<String> misspelled = d.findMisspelledWords(Main.INCORRECT_SENTENCE);
        assertEquals(Arrays.asList("Labooboo", "Pistacheerio"), misspelled);
        assertFalse(d.spellCheck("Labooboo"));
        assertFalse(d.spellCheck("Pistacheerio"));
        assertTrue(d.spellCheck("Labubu"));
        assertTrue(d.spellCheck("Pistachio"));
    }
}

