package dvdrentalfilmtest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import java.util.Random;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.jupiter.api.Test;

/**
 * This class is used to test the CategoryEndpoint class.
 */
public class TestCategoryEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-film/resources/categories";

  /**
   * Tests the createCategory method of the CategoryEndpoint class. The method should return a 201
   * status code.
   */
  @Test
  public void testCreateCategoryCreated() {
    Random random = new Random();
    int randomNumber = random.nextInt(10000) + 1;
    String category = "category" + randomNumber;

    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"category\": \"" + category + "\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 201);
  }

  /**
   * Tests the createCategory method of the CategoryEndpoint class. The method should return a 404
   * status code.
   */
  @Test
  public void testCreateCategoryBadRequest() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * Tests the createCategory method of the CategoryEndpoint class. The method should return a 409
   * status code.
   */
  @Test
  public void testCreateCategoryConflict() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"category\": \"Action\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 409);
  }

  /**
   * Tests the getAllCategories method of the CategoryEndpoint class. It shows if the array is not
   * empty. The method should return a 200
   */
  @Test
  public void testGetAllCategories() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH);
    int response = target.request().get().getStatus();
    String categoriesJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!categoriesJson.contains("[]"));
  }
}
