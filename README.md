# Dictionary SpellChecker

A spellchecker backed by a hand-written Binary Search Tree (Java 17, JUnit 5).

## Files
- `src/main/java/Dictionary.java` – the BST: `insertWordNode()`, `checkWord()` (removes a word), `spellCheck()`, plus helpers (`buildFromText`, `findMisspelledWords`, `getSortedWords`, `isValidBST`, ...)
- `src/main/java/Main.java` – builds the dictionary from the sample sentence, prints it in sorted order, and spellchecks a correct and an incorrect sentence
- `src/test/java/DictionaryTest.java` – JUnit assertions for every method that changes the tree
- `pom.xml` – optional Maven configuration (only needed if you import as a Maven project)

## How It Works
- The dictionary is built from the sample sentence (all words assumed correct).
- Words are stored in lower case, so capitalization doesn't matter. Duplicates are not stored.
- `checkWord(word)` removes a word and handles: not found, leaf, one child, and two children.
- `isValidBST()` checks for cycles, correct left/right ordering, and that the size is accurate. The tests call it after every change to the tree.

## Running in Eclipse

### Option 1: Import as a Maven project (recommended)
1. Go to **File → Import → Maven → Existing Maven Projects**.
2. Browse to the `DictionarySpellChecker` folder and click **Finish**. Eclipse downloads JUnit automatically.
3. **Run the tests:** right-click `src/test/java/DictionaryTest.java` → **Run As → JUnit Test**.
4. **Run the demo:** right-click `src/main/java/Main.java` → **Run As → Java Application**.

### Option 2: Plain Java project (no Maven)
1. Go to **File → New → Java Project**, name it `DictionarySpellChecker`, and choose Java 17 or later.
2. Copy `Dictionary.java`, `Main.java`, and `DictionaryTest.java` into the project's `src` folder.
3. Open `DictionaryTest.java`. If the `@Test` lines show errors, hover over one and choose **Add JUnit 5 library to the build path**.
4. **Run the tests:** right-click `DictionaryTest.java` → **Run As → JUnit Test**.
5. **Run the demo:** right-click `Main.java` → **Run As → Java Application**.

## Expected Results
- **JUnit panel:** a green bar with every test passing. A red bar means at least one assertion failed; click the failed test to see which one.
- **Demo (`Main`) console output:**
  - the dictionary's unique word count, tree height, and `Valid BST: true`
  - all dictionary words in sorted order
  - the correct sentence: `All words spelled correctly.`
  - the incorrect sentence: `Misspelled: [Labooboo, Pistacheerio]`

## Test Sentences
- **Correct sentence (builds the dictionary):**
  `Vanilla, buy me a Labubu, Sonny Angels, Sunny Dubai Chocolate, Onika Burger, We finna be in the pit. Mama, there’s a girl behind you! Pistachio Donut Limited Edition Nintendo Switch.`
- **Incorrect sentence (contains misspellings):**
  `Vanilla buy me Labooboo Sunny Angels Sunny Dubai Chocolate Onika Burger We finna be in the pit Mama a girl behind you Pistacheerio Donut Limited Edition Nintendo Switch`

[<img width="460" height="667" alt="image" src="https://github.com/user-attachments/assets/878a3d4b-49bf-4dfa-8784-bad2e98cf1e4" />](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQT5E9IOARO4XbhsQe2V-OCVscVmjvXe3OYxjjHahkm9y58FoqQC7C5SuYw&s=10)
```
mvn test
```
