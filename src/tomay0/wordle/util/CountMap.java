package tomay0.wordle.util;

import java.util.HashMap;

public class CountMap<T> extends HashMap<T, Integer> {

  public void increment(T t) {
    if (!containsKey(t)) put(t, 0);

    put(t, get(t) + 1);
  }

  public int getCount(T t) {
    if (!containsKey(t)) return 0;

    return get(t);
  }
}
