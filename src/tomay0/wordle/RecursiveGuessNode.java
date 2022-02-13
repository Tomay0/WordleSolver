package tomay0.wordle;

import tomay0.wordle.util.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RecursiveGuessNode {
  private static final int CHECKPOINT_THRESHOLD = 15;

  private WordList wordList;
  private WordList possibleSolutions;
  private double expectedGuesses = 0;
  private int worstCaseGuesses = 0;
  private Guess bestGuess = null;
  private List<Guess> guesses = null;
  private boolean debug = false;
  private boolean parallel = true;
  private int depth;

  public RecursiveGuessNode(int depth, WordList wordList, WordList possibleSolutions) {
    this.wordList = wordList;
    this.possibleSolutions = possibleSolutions;
    this.depth = depth;
  }

  private Pair<String, Integer[]> getEstimatedMetric(String guess) {
    Map<String, GuessLogic> allLogic = possibleSolutions.stream()
        .collect(Collectors.toMap(x -> x, solution -> GuessLogic.generate(guess, solution)));

    Map<GuessLogic, Integer> possibilities = new HashSet<>(allLogic.values()).stream().collect(Collectors.toMap(x -> x, l -> l.getPossibilities(possibleSolutions).size()));

    return new Pair(guess, new Integer[]{possibilities.size(), Collections.max(possibilities.values())});
  }

  public String getBestGuessString() {
    return getBestGuess().guess();
  }

  private Pair<String, Integer> getGuessExpectedGuesses(String guess) {
    Map<String, GuessLogic> allLogic = possibleSolutions.stream()
        .collect(Collectors.toMap(x -> x, solution -> GuessLogic.generate(guess, solution)));

    Map<GuessLogic, RecursiveGuessNode> nodes = new HashSet<>(allLogic.values()).stream()
        .collect(Collectors.toMap(x -> x, logic -> new RecursiveGuessNode(depth + 1, wordList, logic.getPossibilities(possibleSolutions))));

    double expectedGuesses = calculateExpectedGuesses(new Guess(guess, allLogic, nodes));

    return new Pair(guess, expectedGuesses);
  }

  public void setBestWordAndDebug(String word) {
    bestGuess = getGuess(word);

    for (RecursiveGuessNode rgn : bestGuess.nodes().values()) {
      rgn.setDebug(true);
      rgn.setParallel(true);
    }
  }

  public Guess getBestGuess() {
    if (bestGuess != null) return bestGuess;

    if (debug) {
      System.out.println(depthPrefix() + "possible solutions: " + possibleSolutions);
    }

    if (possibleSolutions.size() < 3) {
      bestGuess = getGuess(possibleSolutions.iterator().next());
      return bestGuess;
    }
    else if(possibleSolutions.size() < 8) {
      // try see if there's a word you can guess that will reduce to 2 guesses
      Map<String, Integer[]> estimatedMetrics = possibleSolutions.stream().map(this::getEstimatedMetric).collect(Collectors.toMap(Pair::a, Pair::b));
      Set<String> words2 = possibleSolutions.stream().filter(word -> estimatedMetrics.get(word)[1] == 1).collect(Collectors.toSet());
      if (words2.size() > 0) {
        bestGuess = getGuess(words2.iterator().next());

        return bestGuess;
      }
    } else if(possibleSolutions.size() > CHECKPOINT_THRESHOLD) {
      // load checkpoint
      String tryBestWord = CheckpointHandler.getInstance().getBestWord(possibleSolutions);
      if(tryBestWord != null) {
        bestGuess = getGuess(tryBestWord);

        if (debug) {
          System.out.println(depthPrefix() + "Best: " + bestGuess.guess());
        }

        return bestGuess;
      }
    }

    // calculate what words should be recursively checked
    Map<String, Integer[]> estimatedMetrics = stream(wordList).map(this::getEstimatedMetric).collect(Collectors.toMap(Pair::a, Pair::b));

    WordList words = new WordList();

    // if any of the branches are flat - return any as the best
    Set<String> words2 = wordList.stream().filter(word -> estimatedMetrics.get(word)[1] == 1).collect(Collectors.toSet());
    if (words2.size() > 0) {
      words.addAll(words2);
      words2 = words2.stream().filter(word -> possibleSolutions.contains(word)).collect(Collectors.toSet());

      if (words2.size() > 0) { // prefer word in the possible solutions
        bestGuess = getGuess(words2.iterator().next());
      } else {
        bestGuess = getGuess(words.iterator().next());
      }

      return bestGuess;
    }

    String bestByNumBranches = estimatedMetrics.keySet().stream().min(comparing(estimatedMetrics::get, true)).get();
    String bestByMaxBranchSize = estimatedMetrics.keySet().stream().min(comparing(estimatedMetrics::get, false)).get();

    int minBranches = estimatedMetrics.get(bestByMaxBranchSize)[0];
    int maxBranchSize = estimatedMetrics.get(bestByNumBranches)[1];

    wordList.stream().filter(w -> estimatedMetrics.get(w)[0] >= minBranches).forEach(w -> words.add(w));
    wordList.stream().filter(w -> estimatedMetrics.get(w)[1] <= maxBranchSize).forEach(w -> words.add(w));

    if (debug) {
      System.out.println(depthPrefix() + "Testing starter words: " + words);
    }

    bestGuess = getGuess(stream(words).map(this::getGuessExpectedGuesses).min(Comparator.comparing(Pair::b)).get().a());

    if (debug) {
      System.out.println(depthPrefix() + "Best: " + bestGuess.guess());
    }

    if (possibleSolutions.size() > CHECKPOINT_THRESHOLD) {
      CheckpointHandler.getInstance().saveBestWord(possibleSolutions, bestGuess.guess());
    }

    return bestGuess;
  }

  public Guess getGuess(String guess) {
    Map<String, GuessLogic> allLogic = possibleSolutions.stream()
        .collect(Collectors.toMap(x -> x, solution -> GuessLogic.generate(guess, solution)));

    Map<GuessLogic, RecursiveGuessNode> nodes = new HashSet<>(allLogic.values()).stream()
        .collect(Collectors.toMap(x -> x, logic -> new RecursiveGuessNode(depth + 1, wordList, logic.getPossibilities(possibleSolutions))));

    return new Guess(guess, allLogic, nodes);
  }

  private double calculateExpectedGuesses(Guess guess) {
    double numSolutions = possibleSolutions.size();
    return 1 + guess.allLogic().keySet().stream().filter(solution -> !solution.equals(guess.guess()))
        .mapToDouble(solution -> guess.nodes().get(guess.allLogic().get(solution)).getExpectedGuesses() / numSolutions).sum();
  }

  public double getExpectedGuesses() {
    if (expectedGuesses != 0) return expectedGuesses;

    // base case
    if (possibleSolutions.size() == 1) {
      expectedGuesses = 1;
    } else if (possibleSolutions.size() == 2) {
      expectedGuesses = 1.5;
    } else {
      expectedGuesses = calculateExpectedGuesses(getBestGuess());
    }

    return expectedGuesses;
  }

  private int calculateWorstCaseGuesses(Guess guess) {
    return 1 + guess.allLogic().keySet().stream().filter(solution -> !solution.equals(guess.guess()))
        .mapToInt(solution -> guess.nodes().get(guess.allLogic().get(solution)).getWorstCaseGuesses()).max().getAsInt();
  }

  public int getWorstCaseGuesses() {
    if (worstCaseGuesses != 0) return worstCaseGuesses;

    // base case
    if (possibleSolutions.size() == 1) worstCaseGuesses = 1;
    else if (possibleSolutions.size() == 2) worstCaseGuesses = 2;
    else {
      worstCaseGuesses = calculateWorstCaseGuesses(getBestGuess());
    }


    return worstCaseGuesses;
  }

  public boolean isLeaf() {
    return possibleSolutions.size() == 1;
  }

  public void setDebug(boolean b) {
    this.debug = b;
  }

  public void setParallel(boolean b) {
    this.parallel = b;
  }

  public String getTree() {
    if (isLeaf())
      return '"' + possibleSolutions.iterator().next() + '"';

    StringBuilder sb = new StringBuilder();

    String guess = getBestGuess().guess;
    sb.append("{\"guess\": \"" + guess + "\"");

    for (Map.Entry<GuessLogic, RecursiveGuessNode> entry : getBestGuess().nodes().entrySet()) {
      String pattern = entry.getKey().fileSafeString();
      String tree = entry.getValue().getTree();

      sb.append(", \"" + pattern + "\": " + tree);
    }

    sb.append('}');

    return sb.toString();
  }

  private <T> Stream<T> stream(Collection<T> c) {
    return parallel && c.size() > 50 ? c.parallelStream() : c.stream();
  }
  
  private String depthPrefix() {
    StringBuilder sb = new StringBuilder();
    
    for(int i = 0; i < depth; i++) {
      sb.append("  ");
    }
    return sb.toString();
  }

  private static Comparator<String> comparing(Function<String, Integer[]> f, boolean preferFirst) {
    return (String s1, String s2) -> {
      Integer[] i1 = f.apply(s1);
      Integer[] i2 = f.apply(s2);

      int c = preferFirst ? i2[0].compareTo(i1[0]) : i1[1].compareTo(i2[1]);

      if (c != 0) return c;

      return preferFirst ? i1[1].compareTo(i2[1]) : i2[0].compareTo(i1[0]);
    };
  }

  private static record Guess(String guess, Map<String, GuessLogic> allLogic,
                              Map<GuessLogic, RecursiveGuessNode> nodes) {
  }
}
