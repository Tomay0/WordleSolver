package tomay0.wordle;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.stream.Collectors;

public class CheckpointHandler {
  private static CheckpointHandler checkpointHandler;

  public static CheckpointHandler getInstance() {
    if (checkpointHandler == null) throw new Error("no checkpoint handler?");
    return checkpointHandler;
  }

  public static void setPath(String path) {
    checkpointHandler = new CheckpointHandler(path);
  }

  private HashMap<String, String> checkpoints = new HashMap<>();
  private String path;

  public CheckpointHandler(String path) {
    this.path = path;
    File file = new File(path);

    try {
      if (!file.exists()) {
        file.createNewFile();
      } else {
        // read from file
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while ((line = reader.readLine()) != null) {
          String[] s = line.split("/");
          checkpoints.put(s[0], s[1]);
        }
      }
    } catch (IOException e) {
      throw new Error(e);
    }
  }

  public synchronized String getBestWord(WordList possibleWords) {
    String s = possibleWords.stream().sorted().collect(Collectors.joining(""));

    if (checkpoints.containsKey(s)) {
      return checkpoints.get(s);
    }

    return null;
  }

  public synchronized void saveBestWord(WordList possibleWords, String bestWord) {
    String s = possibleWords.stream().sorted().collect(Collectors.joining(""));
    try {
      Files.write(Paths.get(path), (s + "/" + bestWord + "\n").getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new Error(e);
    }
  }
}
