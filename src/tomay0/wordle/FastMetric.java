package tomay0.wordle;

import java.util.*;
import java.util.stream.IntStream;

/**
 * Highest chance of guessing quickly
 */
public class FastMetric extends GuessMetric{
  @Override
  public float getMetric(Map<String, GuessLogic> allLogic, String guess) {
    Collection<Integer> sizes = getAllSizes(allLogic).values();

    float metric = sizes.stream().filter(x -> x == 1).count() * 100;
    return metric - sizes.stream().mapToInt(Integer::intValue).sum();
  }
}
