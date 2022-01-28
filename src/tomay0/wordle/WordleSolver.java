package tomay0.wordle;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class WordleSolver {
  private static void deleteDirectoryRecursion(Path path) throws IOException {
    if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
      try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
        for (Path entry : entries) {
          deleteDirectoryRecursion(entry);
        }
      }
    }
    Files.delete(path);
  }

  public static void main(String[] args) throws IOException {
    File dir = new File("tree");
    if (dir.exists()) {
      deleteDirectoryRecursion(dir.toPath());
    }

    WordList allWords = WordList.getWordList("valid_words.txt");
    WordList possibleSolutions = WordList.getWordList("all_solutions.txt");

    // create folder
    GuessNode rootNode = new GuessNode(dir, allWords, possibleSolutions);

    rootNode.generateTree();

    TreeWalker.main(args);
  }
}
