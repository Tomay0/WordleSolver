package tomay0.wordle;

import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class GuessMetric {
   protected final Map<GuessLogic, Integer> getSizes(Map<String, GuessLogic> m) {
      return new HashSet<>(m.values()).stream().collect(Collectors.toMap(x->x, l -> l.getPossibilities(m.keySet()).size()));
   }

   protected final Map<String, Integer> getAllSizes(Map<String, GuessLogic> m) {
      Map<GuessLogic, Integer> sizes = getSizes(m);

      return m.keySet().stream().collect(Collectors.toMap(x->x, x->sizes.get(m.get(x))));
   }

   public abstract float getMetric(Map<String, GuessLogic> allLogic, String guess);

}
