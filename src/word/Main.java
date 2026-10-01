package word;

import java.util.List;

/** Demo: builds the dictionary from the sample sentence from Twitch streamer Vanillamace's stream and spellchecks other sentences. */
public class Main {

    /** The dictionary is built from this text; every word in it is assumed correct. */
    static final String SAMPLE_PARAGRAPH =
        "Vanilla, buy me a Labubu, Sonny Angels, Sunny Dubai Chocolate, Onika Burger, "
      + "We finna be in the pit. Mama, there\u2019s a girl behind you! "
      + "Pistachio Donut Limited Edition Nintendo Switch.";

    /** The incorrect sentence: contains two misspellings ("Labooboo", "Pistacheerio"). */
    static final String INCORRECT_SENTENCE =
        "Vanilla buy me Labooboo Sunny Angels Sunny Dubai Chocolate Onika Burger "
      + "We finna be in the pit Mama a girl behind you Pistacheerio Donut Limited "
      + "Edition Nintendo Switch";

    public static void main(String[] args) {
        Dictionary dictionary = new Dictionary();
        int added = dictionary.buildFromText(SAMPLE_PARAGRAPH);

        System.out.println("Dictionary built with " + added + " unique words.");
        System.out.println("Tree height: " + dictionary.height());
        System.out.println("Valid BST: " + dictionary.isValidBST());
        System.out.println("\n--- Dictionary in sorted order ---");
        dictionary.printSorted();

        check(dictionary, "Correct sentence", SAMPLE_PARAGRAPH);
        check(dictionary, "Incorrect sentence", INCORRECT_SENTENCE);
    }

    private static void check(Dictionary dictionary, String label, String sentence) {
        List<String> misspelled = dictionary.findMisspelledWords(sentence);
        System.out.println("\n--- " + label + " ---");
        System.out.println(sentence);
        System.out.println(misspelled.isEmpty()
            ? "All words spelled correctly."
            : "Misspelled: " + misspelled);
    }
}

