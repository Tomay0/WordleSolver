package tomay0.wordle.test;

import org.junit.jupiter.api.Test;
import tomay0.wordle.GuessNode;
import tomay0.wordle.BalancedMetric;
import tomay0.wordle.WordList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuessNodeTest {

  @Test
  public void testTwoWords() {
    WordList possible = new WordList();
    possible.add("mummy");
    possible.add("fuzzy");

    WordList allWords = WordList.getWordList("valid_words.txt");

    assertTrue(allWords.contains("mummy"));
    assertTrue(allWords.contains("fuzzy"));

    GuessNode node = new GuessNode(null, new BalancedMetric(), allWords, possible);

    String best = node.getBestGuessString();

    // When there's only two options, the best option is always to pick one of them
    assertTrue("mummy".equals(best) || "fuzzy".equals(best));
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

    GuessNode node = new GuessNode(null, new BalancedMetric(), allWords, possible);

    // this is the best because you can guess the correct answer immediately after in all circumstances
    assertEquals("uaozz", node.getBestGuessString());
  }

}
