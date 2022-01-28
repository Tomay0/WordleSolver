package tomay0.wordle;

import java.io.*;
import java.util.HashSet;

public class WordList extends HashSet<String> {
  public static WordList getWordList(String fileName) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader(fileName));

      WordList wl = new WordList();

      for (String line : reader.lines().toList()) {
        wl.add(line.trim());
      }

      reader.close();

      return wl;
    } catch (FileNotFoundException e) {
      return new WordList();
    } catch (IOException e) {
      throw new Error(e);
    }
  }
}
