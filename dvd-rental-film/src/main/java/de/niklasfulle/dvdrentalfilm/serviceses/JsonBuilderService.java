package de.niklasfulle.dvdrentalfilm.serviceses;

import jakarta.json.Json;
import java.io.InputStream;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonException;

/**
 * Service for building JSON objects.
 */
@Stateless
public class JsonBuilderService {

  /**
   * Creates a JsonObject from an InputStream. Returns null if the JsonObject is empty.
   *
   * @param stream InputStream containing the JSON data
   * @return JsonObject or null if empty
   */
  public JsonObject createJsonObjectFromStream(InputStream stream) {
    JsonReader jsonReader;
    JsonObject jsonObject;

    try {
      jsonReader = Json.createReader(stream);
      jsonObject = jsonReader.readObject();

    } catch (JsonException e) {
      return null;
    }

    if (jsonObject.isEmpty()) {
      return null;
    }

    return jsonObject;
  }
}