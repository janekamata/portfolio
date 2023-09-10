package cs3500.pa04.mockmodels;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.io.Serializable;

/**
 * The type Mock record.
 */
public record MockRecord(String name, int age) implements Serializable {

  /**
   * The constant serialVersionUID.
   */
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Write object.
   *
   * @param out the out
   * @throws NotSerializableException the not serializable exception
   */
  private void writeObject(ObjectOutputStream out) throws NotSerializableException {
    throw new NotSerializableException("MyRecord cannot be serialized");
  }

  /**
   * Read object.
   *
   * @param in the in
   * @throws NotSerializableException the not serializable exception
   */
  private void readObject(ObjectInputStream in) throws NotSerializableException {
    throw new NotSerializableException("MyRecord cannot be deserialized");
  }
}
