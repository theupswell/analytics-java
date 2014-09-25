package analytics.model;

import java.util.LinkedHashMap;
import java.util.Map;

import static analytics.util.Utils.isNullOrEmpty;

public abstract class Entity {
  Map<String, Object> extra = new LinkedHashMap<String, Object>();

  public Entity put(String key, Object value) {
    if (isNullOrEmpty(key)) {
      throw new IllegalArgumentException("key must not be null or empty");
    }
    extra.put(key, value);
    return this;
  }
}
