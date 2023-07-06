package dvdrentalstoretest;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.jupiter.api.Test;

public class TestInventoryEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-store/resources/inventories";

  /**
   * 
   */
  @Test
  public void testGetInventorieByIdOK() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String inventoryJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (inventoryJson.contains("\"id\":1"));
  }

  /**
   * 
   */
  @Test
  public void testGetInventorieByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/5000");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

  /**
   * 
   */
  @Test
  public void testGetInventorieListByFilmIdOK() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/film/1");
    int response = target.request().get().getStatus();
    String inventoriesJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!inventoriesJson.contains("[]"));
  }

  /**
   * 
   */
  @Test
  public void testGetInventorieListByFilmIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/film/10000");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

}
