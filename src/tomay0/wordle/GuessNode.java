package tomay0.wordle;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GuessNode {
  private final WordList wordList;
  private final WordList possibleSolutions;

  private Guess bestGuess = null;
  private String parent;
  private GuessMetric metric;

  public GuessNode(String parent, GuessMetric metric, WordList wordList, WordList possibleSolutions) {
    this.parent = parent;
    this.metric = metric;
    this.wordList = wordList;
    this.possibleSolutions = possibleSolutions;
  }

  public boolean isLeaf() {
    return possibleSolutions.size() == 1;
  }

  public String getBestGuessString() {
    return getBestGuess().guess;
  }

  private Guess getBestGuess() {
    if (bestGuess == null) {
      bestGuess = wordList.parallelStream().map(this::getGuess).max(Guess::compareTo).get();
    }
    return bestGuess;
  }

  public GuessNode fromLogic(GuessLogic logic) {
    WordList words = logic.getPossibilities(possibleSolutions);
    return new GuessNode(logic.fileSafeString(), metric, wordList, words);
  }

  public Collection<GuessNode> getChildNodes() {
    return getBestGuess().possibilities.parallelStream().map(this::fromLogic).collect(Collectors.toSet());
  }

  private Guess getGuess(String guess) {
    Map<String, GuessLogic> allLogic = possibleSolutions.stream()
        .collect(Collectors.toMap(x->x, solution->GuessLogic.generate(guess, solution)));

    return new Guess(guess, metric.getMetric(allLogic, guess),
        possibleSolutions.contains(guess), new HashSet<>(allLogic.values()));
  }

  private float getMetric(String guess) {
    Map<GuessLogic, Integer> possibilities = new HashMap<>();

    for (String solution : possibleSolutions) {
      GuessLogic logic = GuessLogic.generate(guess, solution);

      if (!possibilities.containsKey(logic)) {
        WordList solutions = logic.getPossibilities(possibleSolutions);
        possibilities.put(logic, solutions.size());
      }
    }

    return possibilities.size() - Collections.max(possibilities.values());
  }


  public String generateTree() {
    if (isLeaf())
      return '"' + possibleSolutions.iterator().next() + '"';

    StringBuilder sb = new StringBuilder();

    String guess = getBestGuess().guess;
    sb.append("{\"guess\": \"" + guess + "\"");

    for (GuessNode child : getChildNodes()) {
      String pattern = child.parent;
      String tree = child.generateTree();

      sb.append(", \"" + pattern + "\": " + tree);
    }

    sb.append('}');

    return sb.toString();
  }

  private record Guess(String guess, float metric, boolean isInPossibilities,
                       Collection<GuessLogic> possibilities) implements Comparable<Guess> {
    @Override
    public int compareTo(Guess o) {
      int compare = Float.compare(metric, o.metric);

      if (compare != 0) return compare;

      return Boolean.compare(isInPossibilities, o.isInPossibilities);
    }
  }
}
