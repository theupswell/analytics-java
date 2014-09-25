package analytics.util;

public final class Utils {
  private Utils() {
    throw new AssertionError("No instances");
  }

  /** Returns true if the string is null, or empty when trimmed. */
  public static boolean isNullOrEmpty(String text) {
    return text == null || (text.trim().length() == 0);
  }
}
