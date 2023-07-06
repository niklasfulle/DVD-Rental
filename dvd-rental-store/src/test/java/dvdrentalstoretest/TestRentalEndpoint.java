package dvdrentalstoretest;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.jupiter.api.Test;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * This class is used to test the RentalEndpoint class.
 */
public class TestRentalEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-store/resources/rentals";

  /**
   * 
   */
  @Test
  public void testCreateRentalCreated() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{\"inventory\":44,\"customer\":46,\"staff\":2,\"date\":\"2020-04-06 18:41\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 201);
  }

  /**
   * 
   */
  @Test
  public void testCreateRentalNotFound() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{\"inventory\":1111111,\"customer\":47,\"staff\":2,\"date\":\"2020-04-06 18:41\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  /**
   * 
   */
  @Test
  public void testCreateRentalBadRequest() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{\"inventory\":,\"customer\":2176,\"staff\":2,\"date\":\"2020-04-06 17:09\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * 
   */
  @Test
  public void testGetRantalByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String rentalJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (rentalJson.contains("\"rentalId\":1"));
  }

  /**
   * 
   */
  @Test
  public void testGetRentalByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/17000");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

  /**
   * 
   */
  @Test
  public void testReturnRentalOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/test/returned");
    String rentalId = targetRest.request().get(String.class);

    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH + "/" + rentalId + "/returned");
    Response response = target.request().put(Entity.entity("", "application/json"));
    client.close();
    assert (response.getStatus() == 200);

    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + rentalId);
    String response2 = target2.request().get(String.class);
    resteasyClient.close();
    assert (!response2.contains("\"returndate\":\"\""));
  }

  /**
   * 
   */
  @Test
  public void testReturnRentalNotFound() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH + "/666666/returned");
    Response response = target.request().put(Entity.entity("", "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  /**
   * 
   */
  @Test
  public void testReturnRentalAlreadyTerminated() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH + "/1/returned");
    Response response = target.request().put(Entity.entity("", "application/json"));
    client.close();
    assert (response.getStatus() == 422);
  }

}
