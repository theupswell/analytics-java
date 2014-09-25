package analytics.converter;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Convert a byte stream to and from a concrete type.
 *
 * @param <T> Object type.
 */
public interface Converter<T> {
  /** Converts bytes to an object. */
  T from(byte[] bytes) throws IOException;

  /** Converts o to bytes written to the specified stream. */
  void toStream(T o, OutputStream bytes) throws IOException;
}