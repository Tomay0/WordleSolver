package tomay0.wordle;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GuessNode {
  private final WordList wordList;
  private final WordList possibleSolutions;
  private final File dir;

  private Guess bestGuess = null;

  public GuessNode(File dir, WordList wordList, WordList possibleSolutions) {
    this.dir = dir;
    this.wordList = wordList;
    this.possibleSolutions = possibleSolutions;
  }

  public boolean isLeaf() {
    return possibleSolutions.size() == 1;
  }

  public Guess getBestGuess() {
    if (bestGuess == null) {
      bestGuess = wordList.parallelStream().map(this::getGuess).min(Guess::compareTo).get();
    }
    return bestGuess;
  }

  public GuessNode fromLogic(GuessLogic logic) {
    WordList words = logic.getPossibilities(possibleSolutions);
    File childDir = new File(dir, logic.fileSafeString());

    return new GuessNode(childDir, wordList, words);
  }

  public Collection<GuessNode> getChildNodes() {
    return getBestGuess().possibilities.parallelStream().map(this::fromLogic).collect(Collectors.toSet());
  }

  private Guess getGuess(String guess) {
    int totalSolutions = 0;

    Map<GuessLogic, Integer> cache = new HashMap<>();

    for (String solution : possibleSolutions) {
      GuessLogic logic = GuessLogic.generate(guess, solution);

      if (!cache.containsKey(logic)) {
        WordList solutions = logic.getPossibilities(possibleSolutions);
        cache.put(logic, solutions.size());
      }

      totalSolutions += cache.get(logic);
    }

    return new Guess(guess, totalSolutions, cache.keySet());
  }


  public void generateTree() throws IOException {
    if (possibleSolutions.size() < 2)
      throw new IllegalStateException("This state is not meant to occur");

    dir.mkdir();

    String guess = getBestGuess().guess;

    Map<String, String> immediateGuesses = new HashMap<>();

    for (GuessNode child : getChildNodes()) {
      if (child.isLeaf()) {
        String pattern = child.dir.getName();
        immediateGuesses.put(pattern, child.possibleSolutions.iterator().next());
      } else {
        child.generateTree();
      }
    }

    writeToFile(guess, immediateGuesses);
  }
  private void writeToFile(String guess, Map<String, String> immediateGuesses) throws IOException {
    PrintWriter writer = new PrintWriter(new FileWriter(new File(dir, "guess.yml")));

    writer.println("guess: " + guess);
    writer.println("results:");

    for (Map.Entry<String, String> entries : immediateGuesses.entrySet()) {
      writer.println("  " + entries.getKey() + ": " + entries.getValue());
    }

    writer.close();
  }

  private record Guess(String guess, int totalSolutions,
                       Collection<GuessLogic> possibilities) implements Comparable<Guess> {
    @Override
    public int compareTo(Guess o) {
      return Integer.compare(totalSolutions, o.totalSolutions);
    }
  }
}
