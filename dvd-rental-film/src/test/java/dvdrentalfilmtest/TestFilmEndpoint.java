package dvdrentalfilmtest;

import org.junit.jupiter.api.Test;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.client.ClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

/**
 * This class is used to test the FilmEndpoint class.
 */
public class TestFilmEndpoint {

  // URL of the endpoint
  private static final String PATH = "http://localhost:8080/dvd-rental-film/resources/films";

  private static final String DEFAULT_FILM = "{ \"actors\": { \"href\": \"http://localhost:8080/dvd-rental-film/resources/actors/1\" }, "
      + "\"categories\": [ \"Comedy\" ], \"description\": \"A funny movie\", "
      + "\"id\": 0, \"language\": \"English\", \"length\": 90, "
      + "\"rating\": \"P18\", \"releaseYear\": 2009, \"rentalDuration\": 4, "
      + "\"rentalRate\": 4.3, \"replacementCost\": 9.99, \"title\": \"Jakarta 2077\" }";

  /**
  * 
  */
  @Test
  public void testCreateFilmCreated() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = DEFAULT_FILM;
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 201);
  }

  /**
   * 
   */
  @Test
  public void testCreateFilmBadRequest() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"actors\": { \"href\": \"\" }, "
        + "\"categories\": [ \"Comedy\" ], \"description\": \"A funny movie\", "
        + "\"id\": 0, \"language\": \"English\", \"length\": 90, "
        + "\"rating\": \"P18\", \"releaseYear\": 2009, \"rentalDuration\": 4, "
        + "\"rentalRate\": 4.3, \"replacementCost\": 9.99, \"title\": \"Jakarta 2077\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 400);
  }

  /**
   * 
   */
  @Test
  public void testCreateFilmActorNotFound() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = "{ \"actors\": { \"href\": \"http://localhost:8080/dvd-rental-film/resources/actors/1123123123\" }, "
        + "\"categories\": [ \"Comedy\" ], \"description\": \"A funny movie\", "
        + "\"id\": 0, \"language\": \"asdw\", \"length\": 90, "
        + "\"rating\": \"P18\", \"releaseYear\": 2009, \"rentalDuration\": 4, "
        + "\"rentalRate\": 4.3, \"replacementCost\": 9.99, \"title\": \"Jakarta 2077\" }";
    Response response = target.request().post(Entity.entity(json, "application/json"));
    client.close();
    assert (response.getStatus() == 404);
  }

  /**
   * 
   */
  @Test
  public void testGetListFilms100() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "?limit=100&offset=0");
    int response = target.request().get().getStatus();
    String filmsJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!filmsJson.contains("[]"));
  }

  /**
   * 
   */
  @Test
  public void testGetListFilms0() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "?limit=0&offset=0");
    int response = target.request().get().getStatus();
    String filmsJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (filmsJson.contains("[]"));
  }

  /**
   * 
   */
  @Test
  public void testGetFilmsCount() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/count");
    int response = target.request().get().getStatus();
    Integer numFilms = Integer.parseInt(target.request().get(String.class));
    resteasyClient.close();
    assert (response == 200);
    assert (numFilms > 0);
  }

  /**
   * 
   */
  @Test
  public void testGetFilmByIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/1");
    int response = target.request().get().getStatus();
    String filmJson = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (filmJson.contains("\"id\":1"));
  }

  /**
   * 
   */
  @Test
  public void testGetFilmByIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/7777777777");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

  /**
   * 
   */
  @Test
  public void testGetActorsByFilmIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/8/actors");
    int response = target.request().get().getStatus();
    String filmActors = target.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!filmActors.equals("[]"));
  }

  /**
   * 
   */
  @Test
  public void testGetActorsByFilmIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget target = resteasyClient.target(PATH + "/6666666/actors");
    int response = target.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

  /**
   * 
   */
  @Test
  public void testAddActorToFilmCreated() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = DEFAULT_FILM;
    target.request().post(Entity.entity(json, "application/json"));
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/test/lastfilm");
    Integer lastFilmId = Integer.parseInt(targetRest.request().get(String.class));

    final WebTarget target1 = client.target(PATH + "/" + lastFilmId + "/actors/77");
    int response = target1.request().put(Entity.entity("", "application/json")).getStatus();
    client.close();
    assert (response == 201);

    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + lastFilmId);
    target2.request().delete();
    resteasyClient.close();
  }

  /**
   * 
   */
  @Test
  public void testAddActorToFilmNotAcceptable() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = DEFAULT_FILM;
    target.request().post(Entity.entity(json, "application/json"));

    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/test/lastfilm");
    Integer lastFilmId = Integer.parseInt(targetRest.request().get(String.class));

    final WebTarget target1 = client.target(PATH + "/" + lastFilmId + "/actors/1");
    int response = target1.request().put(Entity.entity("", "application/json")).getStatus();
    client.close();
    assert (response == 406);

    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + lastFilmId);
    target2.request().delete();
    resteasyClient.close();
  }

  /**
   * 
   */
  @Test
  public void testAddActorToFilmNotFound() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH + "/6666666666/actors/77");
    int response = target.request()
        .put(Entity.entity("", "application/json")).getStatus();
    client.close();
    assert (response == 404);
  }

  /**
   * 
   */
  @Test
  public void testAddCategoriesToFilmCreated() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = DEFAULT_FILM;
    target.request().post(Entity.entity(json, "application/json"));

    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/test/lastfilm");
    Integer lastFilmId = Integer.parseInt(targetRest.request().get(String.class));

    final WebTarget target1 = client.target(PATH + "/" + lastFilmId + "/categories/New");
    int response = target1.request().put(Entity.entity("", "application/json")).getStatus();
    client.close();
    assert (response == 201);

    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + lastFilmId);
    target2.request().delete();
    resteasyClient.close();
  }

  /**
   * 
   */
  @Test
  public void testAddCategoriesToFilmNotAcceptable() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = DEFAULT_FILM;
    target.request().post(Entity.entity(json, "application/json"));

    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/test/lastfilm");
    Integer lastFilmId = Integer.parseInt(targetRest.request().get(String.class));

    final WebTarget target1 = client.target(PATH + "/" + lastFilmId + "/categories/Comedy");
    int response = target1.request().put(Entity.entity("", "application/json")).getStatus();
    client.close();
    assert (response == 406);

    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + lastFilmId);
    target2.request().delete();
    resteasyClient.close();
  }

  /**
   * 
   */
  @Test
  public void testAddCategoriesToFilmNotFound() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH + "/2342342342/categories/New");
    int response = target.request().put(Entity.entity("", "application/json")).getStatus();
    client.close();
    assert (response == 404);
  }

  /**
   * 
   */
  @Test
  public void testGetCategoryByFilmIdOk() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/1/categories");
    int response = targetRest.request().get().getStatus();
    String actors = targetRest.request().get(String.class);
    resteasyClient.close();
    assert (response == 200);
    assert (!actors.equals("[]"));
  }

  /**
   * 
   */
  @Test
  public void testGetCategoryByFilmIdNotFound() {
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/1010101010/categories");

    int response = targetRest.request().get().getStatus();
    resteasyClient.close();
    assert (response == 404);
  }

  /**
   * 
   */
  @Test
  public void testDelteFilmById() {
    Client client = ClientBuilder.newClient();
    final WebTarget target = client.target(PATH);
    String json = DEFAULT_FILM;
    target.request().post(Entity.entity(json, "application/json"));
    client.close();
    ResteasyClient resteasyClient = new ResteasyClientBuilderImpl().build();
    final ResteasyWebTarget targetRest = resteasyClient.target(PATH + "/test/lastfilm");
    Integer lastFilmId = Integer.parseInt(targetRest.request().get(String.class));

    // Tests if the film exists
    final ResteasyWebTarget target1 = resteasyClient.target(PATH + "/" + lastFilmId);
    int response1 = target1.request().get().getStatus();
    assert (response1 == 200);

    // delete the film
    final ResteasyWebTarget target2 = resteasyClient.target(PATH + "/" + lastFilmId);
    int response2 = target2.request().delete().getStatus();
    assert (response2 == 204);

    // Tests if the film does not exist
    final ResteasyWebTarget target3 = resteasyClient.target(PATH + "/" + lastFilmId);
    int response3 = target3.request().get().getStatus();
    resteasyClient.close();
    assert (response3 == 404);
  }

}
