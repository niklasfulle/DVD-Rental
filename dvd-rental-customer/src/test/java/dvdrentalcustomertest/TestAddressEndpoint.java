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
 * This class is used to test the AddressEndpoint class.
 */
public class TestAddressEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-customer/resources/addresses";

  /**
   * Tests the createAddress method of the AddressEndpoint class. The method should return a 201
   * status code.
   */
  @Test
  public void testCreateAddressCreated() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ \"address\": \"Bruhnsway 2\", "
        + "\"address2\": \"\", \"city\": \"York\", "
        + "\"country\": \"England\", \"district\": \"York Central\", "
        + "\"id\": 42, \"phone\": \"018054646\", \"postalCode\": \"42069\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 201);
  }

  /**
   * Tests the createAddress method of the AddressEndpoint class. The method should return a 404
   * status code.
   */
  @Test
  public void testCreateAddressNotFound() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ \"address\": \"Bruhnsway 2\", "
        + "\"address2\": \"\", \"city\": \"BämCity\", "
        + "\"country\": \"England\", \"district\": \"Bäm Central\", "
        + "\"id\": 42, \"phone\": \"018054646\", \"postalCode\": \"42069\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  /**
   * Tests the createAddress method of the AddressEndpoint class. The method should return a 400
   * status code.
   */
  @Test
  public void testCreateAddressBadRequest() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH);
    String json = "{ : \"Bruhnsway 2\", "
        + "\"address2\": \"\", \"city\": \"York\", "
        + "\"country\": \"England\", \"district\": \"York Central\", "
        + "\"id\": 42, \"phone\": \"018054646\", \"postalCode\": \"42069\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * Tests getAddressesLimit method of the AddressEndpoint class. If the response array is not
   * empty, The method should return a 200 status code.
   */
  @Test
  public void testGetAddressesLimit100() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "?limit=100");
    int response = target.request().get().getStatus();
    String addressListJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!addressListJson.equals("[]"));
  }

  /**
   * Tests getAddressesLimit method of the AddressEndpoint class. If the response array is empty,
   * The method should return a 200 status code.
   */
  @Test
  public void testGetAddressesLimit0() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "?limit=0");
    int response = target.request().get().getStatus();
    String addressListJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (addressListJson.equals("[]"));
  }

  /**
   * Tests if the address with the id 1 is returned. The method should return a 200 status code.
   */
  @Test
  public void testGetAddressByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String addressJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (addressJson.contains("\"id\":1"));
  }

  /**
   * Tests if the address with the id 666666 is returned. The method should return a 404 status
   * code.
   */
  @Test
  public void testGetAddressByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666666");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }
}
