package tomay0.wordle;

import java.util.Collection;
import java.util.Map;

/**
 * Highest chance of guessing quickly
 */
public class ScoreMetric extends GuessMetric{
  @Override
  public float getMetric(Map<String, GuessLogic> allLogic, String guess) {
    return (float) allLogic.values().stream().flatMapToDouble(logic -> logic.getDisplayArray().stream().mapToDouble(x->x)).sum();
  }
}
