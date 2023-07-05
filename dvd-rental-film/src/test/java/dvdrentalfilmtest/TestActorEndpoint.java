package dvdrentalfilmtest;

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
 * This class is used to test the ActorEndpoint class.
 */
public class TestActorEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-film/resources/actors";

  /**
   *
   */
  @Test
  public void testCreateActorCreated() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"films\": { \"href\": \"http://localhost:8080/dvd-rental-film/resources/films/1\" }, "
        + "\"firstName\": \"Torben\", "
        + "\"id\": 0,"
        + "\"lastName\": \"Bruhns\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    System.out.println(response.getStatus());
    assert (response.getStatus() == 201);
  }

  /**
   *
   */
  @Test
  public void testCreateActorNotFound() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"films\": { \"href\": \"http://localhost:8080/dvd-rental-film/resources/films/1234234234\" }, "
        + "\"firstName\": \"Torben\", "
        + "\"id\": 0,"
        + "\"lastName\": \"Bruhns\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  /**
   *
   */
  @Test
  public void testCreateActorBadRequest() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"films\": 1{ \"href\": \"http://localhost:8080/dvd-rental-film/resources/films/1\" }, "
        + "\"firstName\": \"Torben\", "
        + "\"id\": 0,"
        + "\"lastName\": \"Bruhns\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   *
   */
  @Test
  public void testGetActors() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH);
    int response = target.request().get().getStatus();
    String actorsJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!actorsJson.contains("[]"));
  }

  /**
   *
   */
  @Test
  public void testGetActorsCount() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/count");
    int response = target.request().get().getStatus();
    Integer numActors = Integer.parseInt(target.request().get(String.class));
    resteasyClient.close();
    assert (response == 200);
    assert (numActors > 0);
  }

  /**
   *
   */
  @Test
  public void testGetActorByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String actorJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (actorJson.contains("\"id\":1"));
  }

  /**
   *
   */
  @Test
  public void testGetActorByIdNotFound() {
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
  public void testDeleteActorByIdOk() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"films\": { \"href\": \"http://localhost:8080/dvd-rental-film/resources/films/1\" }, "
        + "\"firstName\": \"Torben\", "
        + "\"id\": 0,"
        + "\"lastName\": \"Bruhns\" }";
    target.request().post(Entity.entity(json, "application/json"));
    client.close();
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target1 = resteasyClient.target(PATH + "/test/lastactor");
    Integer lastActorId = Integer.parseInt(target1.request().get(String.class));

    // Test ob der Actor existiert
    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + lastActorId);
    int response2 = target2.request().get().getStatus();
    assert (response2 == 200);

    // Actor wird gelöscht
    final ResteasyWebTarget target3 = resteasyClient.target(PATH + "/" + lastActorId);
    int response3 = target3.request().delete().getStatus();
    assert (response3 == 204);

    // Test ob das Actor gelöscht wurde
    final ResteasyWebTarget target4 = resteasyClient.target(PATH + "/" + lastActorId);
    int response4 = target4.request().get().getStatus();
    resteasyClient.close();
    assert (response4 == 404);
  }

  /**
   *
   */
  @Test
  public void testUpdateActorByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 200);
  }

  /**
   *
   */
  @Test
  public void testUpdateActorByIdNotFound() {
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
  public void testGetFilmsListByActorIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1/films");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 200);
  }

  /**
   *
   */
  @Test
  public void testGetFilmsListByActorIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/10000/films");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }
}
