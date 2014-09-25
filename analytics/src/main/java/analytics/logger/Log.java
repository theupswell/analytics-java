package analytics.logger;

/** Abstraction for logging messages. */
public interface Log {
  void debug(String format, Object... args);

  void error(Throwable throwable, String format, Object... args);

  /** A {@link Log} implementation which does not log anything. */
  Log NONE = new Log() {
    @Override public void debug(String format, Object... args) {
    }

    @Override public void error(Throwable throwable, String format, Object... args) {
    }
  };
}
