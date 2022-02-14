package tomay0.wordle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WordleSolver {
  public static void main(String[] args) throws IOException {
    WordList allWords = WordList.getWordList("valid_words.txt");
    WordList possibleSolutions = WordList.getWordList("all_solutions.txt");

    RecursiveGuessNode rootNode = new RecursiveGuessNode(0, allWords, possibleSolutions);
    rootNode.setDebug(true);
    rootNode.setParallel(false);
    String bestGuess = rootNode.getBestGuessString();

    System.out.println("\n-----------");
    System.out.println(bestGuess);
    System.out.println(rootNode.getExpectedGuesses());

    PrintWriter writer = new PrintWriter(new FileWriter("wordle-solver-app/src/tree.json"));
    writer.println(rootNode.getTree());
    writer.close();

  }
}
