package tomay0.wordle.test;

import org.junit.jupiter.api.Test;
import tomay0.wordle.GuessLogic;
import tomay0.wordle.WordList;
import tomay0.wordle.util.ArrayMap;
import tomay0.wordle.util.CountMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GuessLogicTest {

  @Test
  public void testMecca() {
    GuessLogic logic = GuessLogic.generate("mecca", "cocks");

    Map<Integer, Character> correct = new HashMap<>();
    correct.put(2, 'c');

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();
    wrongPlace.add('c', 3);

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('m', 0);
    exactCounts.put('e', 0);
    exactCounts.put('a', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(0, 0, 2, 1, 0));

    String toString = "mecca\n--+*-";

    assertEquals(correct, logic.getCorrect());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testFoods() {
    GuessLogic logic = GuessLogic.generate("foods", "cocks");

    Map<Integer, Character> correct = new HashMap<>();
    correct.put(1, 'o');
    correct.put(4, 's');

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('f', 0);
    exactCounts.put('o', 1);
    exactCounts.put('d', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(0, 2, 0, 0, 2));

    String toString = "foods\n-+--+";

    assertEquals(correct, logic.getCorrect());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testKicks() {
    GuessLogic logic = GuessLogic.generate("kicks", "cocks");

    Map<Integer, Character> correct = new HashMap<>();
    correct.put(2, 'c');
    correct.put(3, 'k');
    correct.put(4, 's');

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('k', 1);
    exactCounts.put('i', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(0, 0, 2, 2, 2));

    String toString = "kicks\n--+++";

    assertEquals(correct, logic.getCorrect());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testScore() {
    GuessLogic logic = GuessLogic.generate("score", "cocks");

    Map<Integer, Character> correct = new HashMap<>();

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();
    wrongPlace.add('s', 0);
    wrongPlace.add('c', 1);
    wrongPlace.add('o', 2);

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('r', 0);
    exactCounts.put('e', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(1, 1, 1, 0, 0));

    String toString = "score\n***--";

    assertEquals(correct, logic.getCorrect());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testAcock() {
    GuessLogic logic = GuessLogic.generate("acock", "cocks");

    Map<Integer, Character> correct = new HashMap<>();

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();
    wrongPlace.add('c', 1);
    wrongPlace.add('o', 2);
    wrongPlace.add('c', 3);
    wrongPlace.add('k', 4);

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('a', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(0, 1, 1, 1, 1));

    String toString = "acock\n-****";

    assertEquals(correct, logic.getCorrect());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testAcockPossibilities() {
    GuessLogic logic = GuessLogic.generate("acock", "cocks");

    WordList wl = WordList.getWordList("valid_words.txt");

    WordList expected = new WordList();
    expected.add("cocky");
    expected.add("cocks");

    assertEquals(expected, logic.getPossibilities(wl));
  }

  @Test
  public void testAddilPossibilities() {
    GuessLogic logic = GuessLogic.generate("addil", "added");

    WordList wl = WordList.getWordList("valid_words.txt");

    WordList expected = new WordList();
    expected.add("added");
    expected.add("adder");
    expected.add("addax");

    assertEquals(expected, logic.getPossibilities(wl));
  }
}
