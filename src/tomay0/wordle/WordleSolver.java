package tomay0.wordle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WordleSolver {
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      return;
    }

    CheckpointHandler.setPath(args[0] + ".txt");

    WordList allWords = WordList.getWordList("valid_words.txt");
    WordList possibleSolutions = WordList.getWordList("all_solutions.txt");

    RecursiveGuessNode rootNode = new RecursiveGuessNode(0, allWords, possibleSolutions);
    rootNode.setDebug(true);
    rootNode.setParallel(false);
    rootNode.setBestWordAndDebug(args[0]);
    String bestGuess = rootNode.getBestGuessString();

    System.out.println("\n-----------");
    System.out.println(bestGuess);
    System.out.println(rootNode.getExpectedGuesses());
    System.out.println(rootNode.getWorstCaseGuesses());

    PrintWriter writer = new PrintWriter(new FileWriter("wordle-solver-app/src/tree.json"));
    writer.println(rootNode.getTree());
    writer.close();

  }
}
