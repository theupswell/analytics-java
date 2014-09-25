package analytics.queue;

import java.util.ArrayDeque;
import java.util.Queue;

public interface QueueProvider {
  <T> Queue<T> get();

  /** A {@link QueueProvider} implementation which returns an in-memory queue. */
  QueueProvider DEFAULT = new QueueProvider() {
    @Override public <T> Queue<T> get() {
      return new ArrayDeque<T>();
    }
  };
}
