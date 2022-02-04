package tomay0.wordle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WordleSolver {
  public static void main(String[] args) throws IOException {
    WordList allWords = WordList.getWordList("valid_words.txt");
    WordList possibleSolutions = WordList.getWordList("all_solutions.txt");

    GuessNode rootNode = new GuessNode(null, new FastMetric(), allWords, possibleSolutions);
    String json = rootNode.generateTree();

    PrintWriter writer = new PrintWriter(new FileWriter("wordle-solver-app/src/tree.json"));
    writer.println(json);
    writer.close();

  }
}
