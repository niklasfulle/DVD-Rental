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
 * This class is used to test the CustomerEndpoint class.
 */
public class TestCustomerEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-customer/resources/customers";

  @Test
  public void testCreateCustomerCreated() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH + "/?address=1&store=2");
    String json = "{ \"active\": 1, \"activebool\": true, "
        + "\"address\": { \"href\": \"http://localhost:8080/dvd-rental-customer/resources/addresses/1\" }, \"createDate\": \"2021-12-22 10:10\", "
        + "\"email\": \"torben@bruhns.de\", \"firstName\": \"Torben\", \"id\": 1, "
        + "\"lastName\": \"Bruhns\", \"store\": { \"href\": \"http://localhost:8080/dvd-rental-store/resources/stores/2\" } }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    System.out.println(response.getStatus());
    assert (response.getStatus() == 201);
  }

  @Test
  public void testCreateCustomerNotFound() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH + "/?address=66666666&store=6666");
    String json = "{ \"active\": 1, \"activebool\": true, "
        + "\"address\": { \"href\": \"http://localhost:8080/dvd-rental-customer/resources/addresses/66666666\" }, \"createDate\": \"2021-12-22 10:10\", "
        + "\"email\": \"torben@bruhns.de\", \"firstName\": \"Torben\", \"id\": 1, "
        + "\"lastName\": \"Bruhns\", \"store\": { \"href\": \"http://localhost:8080/dvd-rental-store/resources/stores/6666\" } }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  @Test
  public void testCreateCustomerBadRequest() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH + "/?address=1&store=2");
    String json = "{ \"active\": 1, \"activebool\": true, "
        + "\"address\": \"Address 1\", \"createDate\": \"2021-12-22\", "
        + "\"email\":, \"firstName\": \"Torben\", \"id\": 1, "
        + "\"lastName\": \"Bruhns\", \"store\": { \"href\": \"http://localhost:8080/dvd-rental-store/resources/stores/2\" } }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  @Test
  public void testGetCustomersCount() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/count");
    int response = target.request().get().getStatus();
    Integer numCustomers = Integer.parseInt(target.request().get(String.class));
    resteasyClient.close();
    assert (response == 200);
    assert (numCustomers > 0);
  }

  @Test
  public void testGetCustomerByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String customerJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (customerJson.contains("\"id\":1"));
  }

  @Test
  public void testGetCustomerByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

  @Test
  public void testGetPaymentsByCustomerIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1/payments");
    int response = target.request().get().getStatus();
    String customerPaymentsJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!customerPaymentsJson.contains("[]"));
  }

  @Test
  public void testGetPaymentsByCustomerIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666/payments");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }
}
