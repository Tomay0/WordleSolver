package tomay0.wordle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public record TreeWalker(File dir, WordList possibleSolutions) {


  private List<String> guesses(String solution) {
    File dir = dir();

    List<String> guesses = new ArrayList<>();

    String guess;
    do {
      ParsedNode node = getParsedNode(dir);
      guess = node.guess;
      guesses.add(guess);

      if (!guess.equals(solution)) {
        GuessLogic logic = GuessLogic.generate(guess, solution);

        if (node.immediate.containsKey(logic.fileSafeString())) {
          guess = node.immediate.get(logic.fileSafeString());
          guesses.add(guess);
        } else {
          dir = new File(dir, logic.fileSafeString());
        }
      }

    } while (!guess.equals(solution));

    return guesses;
  }

  public Map<String, List<String>> generateTreeStems() {
    return possibleSolutions.parallelStream().collect(Collectors.toMap(x -> x, this::guesses));
  }


  private record ParsedNode(String guess, Map<String, String> immediate, Collection<File> children) {
  }

  private ParsedNode getParsedNode(File dir) {
    try {
      File yml = new File(dir, "guess.yml");

      BufferedReader reader = new BufferedReader(new FileReader(yml));

      String guess = reader.readLine().trim().split(": ")[1];
      reader.readLine();
      Map<String, String> immediate = new HashMap<>();

      String line;
      while ((line = reader.readLine()) != null) {
        String[] split = line.trim().split(": ");
        immediate.put(split[0], split[1]);
      }

      Set<File> files = new HashSet<>();
      for (File f : dir.listFiles()) {
        if (f.getName().contains(".yml")) continue;

        files.add(f);
      }

      return new ParsedNode(guess, immediate, files);

    } catch (IOException e) {
      throw new Error(e);
    }
  }

  public static void main(String[] args) {
    File dir = new File("tree");
    WordList possibleSolutions = WordList.getWordList("all_solutions.txt");

    TreeWalker walker = new TreeWalker(dir, possibleSolutions);

    Map<String, List<String>> map = walker.generateTreeStems();

    System.out.println(map);

    int maxGuesses = map.values().stream().map(List::size).max(Integer::compare).get();
    float averageGuesses = map.values().stream().mapToInt(List::size).sum() / (float) possibleSolutions.size();

    System.out.println("Maximum number of guesses: " + maxGuesses);
    System.out.println("Average number of guesses: " + averageGuesses);
    for (int i = 0; i < maxGuesses; i++) {
      final int v = i + 1;
      float percent = 100.0f * (map.values().stream().filter(x -> x.size() == v).count() / (float) possibleSolutions.size());

      System.out.println(v + ": " + percent + "%");
    }
  }
}
