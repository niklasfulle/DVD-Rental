package dvdrentalcustomertest;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.jupiter.api.Test;

/**
 * This class is used to test the CityEndpoint class.
 */
public class TestCityEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-customer/resources/cities";

  /**
   * Tests the createCity method of the CitiesEndpoint class. The method should return a 201 status
   * code.
   */
  @Test
  public void testCreateCityCreated() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ \"city\": \"Test\", "
        + "\"country\": \"Malaysia\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 201);
  }

  /**
   * Tests the createCity method of the CitiesEndpoint class. The method should return a 404 status
   * code.
   */
  @Test
  public void testCreateCityNotFound() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ \"city\": \"Test\", "
        + "\"country\": \"da\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  /**
   * Tests the createCity method of the CitiesEndpoint class. The method should return a 400 status
   * code.
   */
  @Test
  public void testCreateCityBadRequest() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ \"\": \"Test\", "
        + "\"country\": \"Malaysia\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * Tests if the city with the id 1 is returned. The method should return a 200 status code.
   */
  @Test
  public void testGetCityByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String customerJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (customerJson.contains("\"id\":1"));
  }

  /**
   * Tests if the city with the id 666 is returned. The method should return a 404 status code.
   */
  @Test
  public void testGetCityByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }
}
