package cs3500.pa04.json;

import static cs3500.pa04.json.JsonUtils.serializeRecord;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.databind.JsonNode;
import cs3500.pa04.model.Coordinate;
import org.junit.jupiter.api.Test;

/**
 * Tests for the JsonUtils class.
 */
class JsonUtilsTest {

  @Test
  void testJsonUtilsConstructor() {
    assertDoesNotThrow(JsonUtils::new);
  }

  /**
   * Test serialize record.
   */
  @Test
  void testSerializeRecord() {
    // Arrange
    Coordinate coord = new Coordinate(1, 1);
    // Act
    JsonNode jsonNode = serializeRecord(coord);

    // Assert
    assertNotNull(jsonNode);
  }
}