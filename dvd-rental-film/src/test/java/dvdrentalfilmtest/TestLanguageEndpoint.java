package dvdrentalfilmtest;

import java.util.Random;
import org.junit.jupiter.api.Test;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.ClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

/**
 * This class is used to test the LanguageEndpoint class.
 */
public class TestLanguageEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-film/resources/languages";

  /**
   * Tests the createLanguage method of the LanguageEndpoint class. The method should return a 201
   * status code.
   */
  @Test
  public void testCreateLanguageCreated() {
    Random random = new Random();
    int randomNumber = random.nextInt(10000) + 1;
    String language = "language" + randomNumber;

    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"language\": \"" + language + "\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 201);
  }

  /**
   * Tests the createLanguage method of the LanguageEndpoint class. The method should return a 400
   * status code.
   */
  @Test
  public void testCreateLanguageBadRequest() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * Tests the createLanguage method of the LanguageEndpoint class. The method should return a 404
   * status code.
   */
  @Test
  public void testCreateCategoryConflict() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"language\": \"English             \"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 409);
  }

  /**
   * Tests the getAllLanguages method of the LanguageEndpoint class. It shows if the array is not
   * empty. The method should return a 200
   */
  @Test
  public void testGetAllLanguages() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH);
    int response = target.request().get().getStatus();
    String languagesJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!languagesJson.contains("[]"));
  }
}