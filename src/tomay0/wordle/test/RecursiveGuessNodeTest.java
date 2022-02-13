package tomay0.wordle.test;

import org.junit.jupiter.api.Test;
import tomay0.wordle.BalancedMetric;
import tomay0.wordle.GuessNode;
import tomay0.wordle.RecursiveGuessNode;
import tomay0.wordle.WordList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RecursiveGuessNodeTest {

  @Test
  public void testTwoWords() {
    WordList possible = new WordList();
    possible.add("mummy");
    possible.add("fuzzy");

    WordList allWords = WordList.getWordList("valid_words.txt");

    assertTrue(allWords.contains("mummy"));
    assertTrue(allWords.contains("fuzzy"));

    RecursiveGuessNode node = new RecursiveGuessNode(0,allWords, possible);

    String best = node.getBestGuessString();

    // When there's only two options, the best option is always to pick one of them
    assertTrue("mummy".equals(best) || "fuzzy".equals(best));
    assertEquals(1.5, node.getExpectedGuesses());
    assertEquals(2, node.getWorstCaseGuesses());
  }

  @Test
  public void testThreeWords() {
    WordList possible = new WordList();
    possible.add("guppy");
    possible.add("gappy");
    possible.add("goppy");

    WordList allWords = new WordList();
    allWords.addAll(possible);
    allWords.add("uaozz");

    RecursiveGuessNode node = new RecursiveGuessNode(0, allWords, possible);

    // this is the best because you can guess the correct answer immediately after in all circumstances
    assertEquals("uaozz", node.getBestGuessString());
    assertEquals(2, node.getExpectedGuesses());
    assertEquals(2, node.getWorstCaseGuesses());
  }
}
