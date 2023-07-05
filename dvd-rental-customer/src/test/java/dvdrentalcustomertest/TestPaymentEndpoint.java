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
 * This class is used to test the PaymentEndpoint class.
 */
public class TestPaymentEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-customer/resources/payments";

  /**
   * Tests the createPayment method of the PaymentsEndpoint class.
   * The method should return a 201 status code.
   */
  @Test
  public void testCreatePaymentCreated() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH + "/");
    String json = "{\"amount\":33.3,\"rental\":1520,\"customer\":341,"
        + "\"staff\":2,\"date\":\"2020-04-06 15:09\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    System.out.println(response.getStatus());
    assert (response.getStatus() == 201);
  }

  /**
   * Tests the createPayment method of the PaymentsEndpoint class.
   * The method should return a 404 status code.
   */
  @Test
  public void testCreatePaymentNotFound() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH + "/");
    String json = "{\"amount\":33.3,\"rental\":1520,\"customer\":666,"
        + "\"staff\":666,\"date\":\"2020-04-06 15:09\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  /**
   * Tests the createPayment method of the PaymentsEndpoint class.
   * The method should return a 400 status code.
   */
  @Test
  public void testCreatePaymentBadRequest() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH + "/");
    String json = "{\"amount\":,\"rental\":1520,\"customer\":341,"
        + "\"staff\":2,\"date\":\"06.04.2020 15:09\"}";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * Tests if the payment with the id 17503 is returned.
   * The method should return a 200 status code.
   */
  @Test
  public void testGetPaymentByIdOk() {
    final ResteasyClient client = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = client.target(PATH + "/17503");
    int response = target.request().get().getStatus();
    String jsonPayments = target.request().get(String.class);
    client.close();
    assert (response == 200);
    assert (jsonPayments.contains("\"id\":17503"));
  }

  /**
   * Tests if the payment with the id 666 is returned.
   * The method should return a 404 status code.
   */
  @Test
  public void testGetPaymentByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

  /**
   * Tests if the created payment can be deleted.
   * The method should return a 204 status code.
   */
  @Test
  public void testDeletePaymentsById() {
    Client client = ClientBuilder.newBuilder().build();
    final WebTarget target = client.target(PATH + "/");
    String json = "{\"amount\":33.3,\"rental\":1520,\"customer\":341,"
        + "\"staff\":2,\"date\":\"2020-04-06 15:09\"}";
    target.request().post(Entity.entity(json, "application/json"));

    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/test/delete");
    Integer lastPaymentId = Integer.parseInt(targetRest.request().get(String.class));

    // Test ob das Payment existiert
    final ResteasyWebTarget target1 = resteasyClient.target(PATH + "/" + lastPaymentId);
    int response1 = target1.request().get().getStatus();
    assert (response1 == 200);

    // Payment wird gelöscht
    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + lastPaymentId);
    int response2 = target2.request().delete().getStatus();
    assert (response2 == 204);

    // Test ob das Payment gelöscht wurde
    final ResteasyWebTarget target3 = resteasyClient.target(PATH + "/" + lastPaymentId);
    int response3 = target3.request().get().getStatus();
    resteasyClient.close();
    assert (response3 == 404);
  }
}
