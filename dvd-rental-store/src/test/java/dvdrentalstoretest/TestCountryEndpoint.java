package dvdrentalstoretest;

import org.junit.jupiter.api.Test;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.ClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

/**
 * This class is used to test the CountryEndpoint class.
 */
public class TestCountryEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-store/resources/countries";

  /**
   * Tests the createCountry method of the CountriesEndpoint class. The method should return a 201
   * status code.
   */
  @Test
  public void testCreateCountryCreated() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ \"country\": \"Test34523\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 201);
  }

  /**
   * Tests the createCountry method of the CountriesEndpoint class. The method should return a 400
   * status code.
   */
  @Test
  public void testCreateCountryBadRequest() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ \"country\": \"Test\", }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * Tests if the country with the id 1 is returned. The method should return a 200 status code.
   */
  @Test
  public void testGetCountryByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String customerJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (customerJson.contains("\"id\":1"));
  }

  /**
   * Tests if the country with the id 666 is returned. The method should return a 404 status code.
   */
  @Test
  public void testGetCountryByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }
}