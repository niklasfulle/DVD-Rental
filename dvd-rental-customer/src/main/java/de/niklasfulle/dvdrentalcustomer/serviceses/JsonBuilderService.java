package de.niklasfulle.dvdrentalcustomer.serviceses;

import java.io.InputStream;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonException;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

/**
 * Service for building JSON objects.
 */
@Stateless
public class JsonBuilderService {

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