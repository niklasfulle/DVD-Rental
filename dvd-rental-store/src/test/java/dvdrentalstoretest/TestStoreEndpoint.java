package dvdrentalstoretest;

import org.junit.jupiter.api.Test;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

/**
 * This class is used to test the StoreEndpoint class.
 */
public class TestStoreEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-store/resources/stores";

  /**
   * Tests if the count of stores is greater than 0. The method should return a 200 status code.
   */
  @Test
  public void testGetStoresCountOK() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/count");
    int response = target.request().get().getStatus();
    Integer numStores = Integer.parseInt(target.request().get(String.class));
    resteasyClient.close();
    assert (response == 200);
    assert (numStores > 0);
  }

  /**
   * Tests if the store with the id 1 is returned. The method should return a 200 status code.
   */
  @Test
  public void testGetStoreByIdOK() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String storeJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (storeJson.contains("\"id\":1"));
  }

  /**
   * Tests if the store with the id 666 is returned. The method should return a 404 status code.
   */
  @Test
  public void testGetStoreByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }
}