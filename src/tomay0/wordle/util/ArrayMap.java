package tomay0.wordle.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArrayMap<T, V> extends HashMap<T, List<V>> {

  public void add(T t, V v) {
    if (!containsKey(t)) put(t, new ArrayList<>());

    get(t).add(v);
  }

  public int getCount(T t) {
    if (!containsKey(t)) return 0;

    return get(t).size();
  }
}
