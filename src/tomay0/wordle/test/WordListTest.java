package tomay0.wordle.test;

import org.junit.jupiter.api.Test;
import tomay0.wordle.WordList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordListTest {

  @Test
  public void testAllSolutions() {
    WordList wl = WordList.getWordList("all_solutions.txt");

    for (String w : wl) {
      assertEquals(5, w.length());
    }

    assertEquals(2315, wl.size());
  }

  @Test
  public void testValidWords() {
    WordList wl = WordList.getWordList("valid_words.txt");

    for (String w : wl) {
      assertEquals(5, w.length());
    }

    assertEquals(12972, wl.size());
  }
}
