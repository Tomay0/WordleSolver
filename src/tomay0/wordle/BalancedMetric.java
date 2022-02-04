package tomay0.wordle;

import java.util.*;

/**
 * Always 5 guesses or less
 */
public class BalancedMetric extends GuessMetric {
  @Override
  public float getMetric(Map<String, GuessLogic> allLogic, String guess) {
    Collection<Integer> branches = getSizes(allLogic).values();
    return branches.size() - Collections.max(branches);
  }
}
