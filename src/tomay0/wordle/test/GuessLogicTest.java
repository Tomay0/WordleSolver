package tomay0.wordle.test;

import org.junit.jupiter.api.Test;
import tomay0.wordle.GuessLogic;
import tomay0.wordle.WordList;
import tomay0.wordle.util.ArrayMap;
import tomay0.wordle.util.CountMap;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class GuessLogicTest {

  @Test
  public void testMecca() {
    GuessLogic logic = GuessLogic.generate("mecca", "cocks");

    Set<Integer> correct = new HashSet<>();
    correct.add(2);

    CountMap<Character> characterCountMap = new CountMap<>();
    characterCountMap.increment('c');

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();
    wrongPlace.add('c', 3);

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('m', 0);
    exactCounts.put('e', 0);
    exactCounts.put('a', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(0, 0, 2, 1, 0));

    String toString = "mecca\n--+*-";

    assertEquals(correct, logic.getCorrect());
    assertEquals(characterCountMap, logic.getCorrectCounts());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testFoods() {
    GuessLogic logic = GuessLogic.generate("foods", "cocks");

    Set<Integer> correct = new HashSet<>();
    correct.add(1);
    correct.add(4);
    
    CountMap<Character> characterCountMap = new CountMap<>();
    characterCountMap.increment('o');
    characterCountMap.increment('s');

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('f', 0);
    exactCounts.put('o', 1);
    exactCounts.put('d', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(0, 2, 0, 0, 2));

    String toString = "foods\n-+--+";

    assertEquals(correct, logic.getCorrect());
    assertEquals(characterCountMap, logic.getCorrectCounts());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testKicks() {
    GuessLogic logic = GuessLogic.generate("kicks", "cocks");

    Set<Integer> correct = new HashSet<>();
    correct.add(2);
    correct.add(3);
    correct.add(4);

    CountMap<Character> characterCountMap = new CountMap<>();
    characterCountMap.increment('c');
    characterCountMap.increment('k');
    characterCountMap.increment('s');

    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();

    Map<Character, Integer> exactCounts = new HashMap<>();
    exactCounts.put('k', 1);
    exactCounts.put('i', 0);

    ArrayList<Integer> displayArray = new ArrayList<>(Arrays.asList(0, 0, 2, 2, 2));

    String toString = "kicks\n--+++";

    assertEquals(correct, logic.getCorrect());
    assertEquals(characterCountMap, logic.getCorrectCounts());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testScore() {
    GuessLogic logic = GuessLogic.generate("score", "cocks");

    Set<Integer> correct = new HashSet<>();
    CountMap<Character> characterCountMap = new CountMap<>();

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
    assertEquals(characterCountMap, logic.getCorrectCounts());
    assertEquals(wrongPlace, logic.getWrongPlace());
    assertEquals(exactCounts, logic.getExactCharacterCount());
    assertEquals(displayArray, logic.getDisplayArray());
    assertEquals(toString, logic.toString());
  }

  @Test
  public void testAcock() {
    GuessLogic logic = GuessLogic.generate("acock", "cocks");

    Set<Integer> correct = new HashSet<>();
    CountMap<Character> characterCountMap = new CountMap<>();

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
    assertEquals(characterCountMap, logic.getCorrectCounts());
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

  @Test
  public void testMummyPossibilities() {
    GuessLogic logic = GuessLogic.generate("mummy", "fizzy");

    WordList wl = new WordList();
    wl.add("mummy");
    wl.add("fizzy");

    WordList expected = new WordList();
    expected.add("fizzy");

    assertEquals(expected, logic.getPossibilities(wl));
  }

  @Test
  public void testBreer() {
    GuessLogic logic = GuessLogic.generate("breer", "purer");

    assertFalse(logic.isPossible("ulcer"));
    assertFalse(logic.isPossible("udder"));
    assertFalse(logic.isPossible("utter"));
    assertFalse(logic.isPossible("upper"));
    assertTrue(logic.isPossible("purer"));
  }

  @Test
  public void testBreer2() {
    GuessLogic logic = GuessLogic.generate("breer", "perch");

    assertFalse(logic.isPossible("ulcer"));
    assertFalse(logic.isPossible("threw"));
    assertFalse(logic.isPossible("utter"));
    assertFalse(logic.isPossible("demur"));
    assertTrue(logic.isPossible("perch"));
    assertFalse(logic.isPossible("femur"));
    assertFalse(logic.isPossible("udder"));
    assertFalse(logic.isPossible("upper"));
    assertFalse(logic.isPossible("lemur"));
  }
}
