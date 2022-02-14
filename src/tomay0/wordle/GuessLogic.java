package tomay0.wordle;

import tomay0.wordle.util.ArrayMap;
import tomay0.wordle.util.CountMap;

import java.util.*;

public class GuessLogic {
  private String guess;

  private Set<Integer> correct;
  private CountMap<Character> correctCounts;
  private ArrayMap<Character, Integer> wrongPlace;
  private Map<Character, Integer> exactCharacterCount;
  private List<Integer> displayArray;

  public String getGuess() {
    return guess;
  }

  public Set<Integer> getCorrect() {
    return correct;
  }

  public ArrayMap<Character, Integer> getWrongPlace() {
    return wrongPlace;
  }

  public Map<Character, Integer> getExactCharacterCount() {
    return exactCharacterCount;
  }

  public List<Integer> getDisplayArray() {
    return displayArray;
  }

  public CountMap<Character> getCorrectCounts() {
    return correctCounts;
  }

  private GuessLogic(String guess, Set<Integer> correct, CountMap<Character> correctCounts, ArrayMap<Character, Integer> wrongPlace,
                     Map<Character, Integer> exactCharacterCount, List<Integer> displayArray) {
    this.guess = guess;
    this.correct = correct;
    this.correctCounts = correctCounts;
    this.wrongPlace = wrongPlace;
    this.exactCharacterCount = exactCharacterCount;
    this.displayArray = displayArray;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;
    GuessLogic that = (GuessLogic) o;

    return guess.equals(that.guess) && displayArray.equals(that.displayArray);
  }

  @Override
  public int hashCode() {
    return Objects.hash(guess, displayArray);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    sb.append(guess + "\n");

    for (int i : displayArray) {
      switch (i) {
        case 0:
          sb.append('-');
          break;
        case 1:
          sb.append('*');
          break;
        case 2:
          sb.append('+');
      }
    }
    return sb.toString();
  }

  public String fileSafeString() {
    StringBuilder sb = new StringBuilder();

    for (int i : displayArray) {
      sb.append(i);
    }

    return sb.toString();
  }

  private static int countChars(String word, Character c) {
    int count = 0;
    for (int i = 0; i < 5; i++) {
      if (word.charAt(i) == c) count++;
    }

    return count;
  }

  public boolean isPossible(String word) {
    // check correct place
    for (int i = 0; i < 5; i++) {
      if (correct.contains(i)) {
        if (word.charAt(i) != guess.charAt(i)) return false;
      } else {
        if (word.charAt(i) == guess.charAt(i)) return false;
      }
    }

    // check wrong place
    for (Map.Entry<Character, List<Integer>> pair : wrongPlace.entrySet()) {
      for (int i : pair.getValue()) {
        if (word.charAt(i) == pair.getKey()) return false;
      }

      if (countChars(word, pair.getKey()) < pair.getValue().size() + correctCounts.getCount(pair.getKey()))
        return false;
    }

    // check exact count
    for (Map.Entry<Character, Integer> pair : exactCharacterCount.entrySet()) {
      if (countChars(word, pair.getKey()) != pair.getValue()) return false;
    }

    return true;
  }

  public WordList getPossibilities(Collection<String> inputWords) {
    WordList wl = new WordList();
    for (String s : inputWords) {
      if (isPossible(s)) wl.add(s);
    }

    return wl;
  }

  public static GuessLogic generate(String guess, String solution) {
    if (guess.length() != 5 || solution.length() != 5)
      throw new IllegalArgumentException("Invalid word length");


    Map<Integer, Character> correct = new HashMap<>();

    // get correct positions
    CountMap<Character> letterCounts = new CountMap<>();

    for (int i = 0; i < 5; i++) {
      char c = guess.charAt(i);
      if (c == solution.charAt(i)) {
        correct.put(i, c);

        letterCounts.increment(c);
      }
    }

    CountMap<Character> letterCounts2 = new CountMap<>();
    Map<Character, Integer> exactLetterCounts = new HashMap<>();
    ArrayMap<Character, Integer> wrongPlace = new ArrayMap<>();
    List<Integer> displayArray = new ArrayList<>();

    // get invalid positions
    for (int i = 0; i < 5; i++) {
      char c = guess.charAt(i);
      letterCounts2.increment(c);

      if (correct.containsKey(i)) {
        // correct place - ignore
        displayArray.add(2);
      } else {
        letterCounts.increment(c);

        int solutionCount = countChars(solution, c);

        if (solutionCount < letterCounts.getCount(c)) {
          // not in the word - we know the exact count of this letter
          displayArray.add(0);
          exactLetterCounts.put(c, solutionCount);
        } else {
          // wrong place
          wrongPlace.add(c, i);
          displayArray.add(1);
        }
      }
    }

    CountMap<Character> correctCounts = new CountMap<>();
    for (char c : correct.values()) {
      correctCounts.increment(c);
    }

    return new GuessLogic(guess, correct.keySet(), correctCounts, wrongPlace, exactLetterCounts, displayArray);
  }
}
