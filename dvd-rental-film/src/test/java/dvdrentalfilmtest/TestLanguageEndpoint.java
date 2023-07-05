package dvdrentalfilmtest;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;
import org.junit.jupiter.api.Test;

/**
 * This class is used to test the LanguageEndpoint class.
 */
public class TestLanguageEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-film/resources/languages";

  /**
   * 
   */
  @Test
  public void testGetAllLanguages() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH);
    int response = target.request().get().getStatus();
    String languagesJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!languagesJson.contains("[]"));
  }
}
