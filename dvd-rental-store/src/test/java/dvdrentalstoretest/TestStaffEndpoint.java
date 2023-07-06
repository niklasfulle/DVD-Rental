package dvdrentalstoretest;

import org.junit.jupiter.api.Test;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

/**
 * This class is used to test the StaffEndpoint class.
 */
public class TestStaffEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-store/resources/staff";

  /**
   * Tests if the staff with the id 1 is returned. The method should return a 200 status code.
   */
  @Test
  public void testGetStaffByIdOK() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String staffJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (staffJson.contains("\"id\":1"));
  }

  /**
   * Tests if the staff with the id 666 is returned. The method should return a 404 status code.
   */
  @Test
  public void testGetStaffByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/666");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }
}