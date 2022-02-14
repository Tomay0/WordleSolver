package tomay0.wordle;

import java.util.HashSet;
import java.util.Map;

public class InformationMetric extends GuessMetric {
  private static final double log2 = Math.log(2);

  @Override
  public float getMetric(Map<String, GuessLogic> allLogic, String guess) {
    double x = 0;

    for (GuessLogic l : new HashSet<>(allLogic.values())) {
      double numMatches = l.getPossibilities(allLogic.keySet()).size();
      double px = numMatches / allLogic.keySet().size();

      x += px * Math.log(1.0 / px) / log2;
    }

    return (float) x;
  }
}
