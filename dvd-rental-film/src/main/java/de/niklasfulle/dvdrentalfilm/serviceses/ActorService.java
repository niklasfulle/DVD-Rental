package de.niklasfulle.dvdrentalfilm.serviceses;

import java.util.List;
import jakarta.json.Json;
import java.time.Instant;
import java.sql.Timestamp;
import java.util.LinkedList;
import jakarta.inject.Inject;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.niklasfulle.dvdrentalfilm.entities.Actor;
import de.niklasfulle.dvdrentalfilm.entities.Film;
import de.niklasfulle.dvdrentalfilm.entities.FilmActor;

/**
 * Service for Actor entity.
 */
@Stateless
public class ActorService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Services
  @Inject
  FilmService filmService;

  @Inject
  FilmActorService filmActorService;

  // Methods

  /**
   * Creates a new Actor object and persists it to the database.
   *
   * @param jsonActorObject JsonObject containing the actor data
   * @return Response with status code and message
   */
  public Response createActor(JsonObject jsonActorObject) {
    int filmId;
    // checks if film exists
    try {
      String[] filmStringArray = jsonActorObject.getJsonObject("films").getString("href")
          .split("/");
      filmId = Integer.parseInt(filmStringArray[filmStringArray.length - 1]);
    } catch (NumberFormatException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no FilmId found")
          .build();
    }

    // checks if film exists
    Film film = filmService.getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found")
          .build();
    }

    try {
      Actor actor = new Actor(jsonActorObject.getString("firstName"),
          jsonActorObject.getString("lastName"),
          Timestamp.from(Instant.now()));

      em.persist(actor);
      em.flush();

      // creates FilmActor
      List<FilmActor> filmActors = new LinkedList<>();
      FilmActor filmActor = filmActorService.createFilmActor(film, actor);
      if (filmActor != null) {
        filmActors.add(filmActor);
      }

      return Response.status(Response.Status.CREATED)
          .entity("Actor created")
          .build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Couldn't add Film to Database.")
          .build();
    }
  }

  /**
   * Updates an existing Actor object in the database.
   *
   * @param actorId         Id of the Actor to update
   * @param jsonActorObject JsonObject containing the actor data
   * @return Response with status code and message
   */
  public Response updateActor(int actorId, JsonObject jsonActorObject) {
    Actor actor = getActor(actorId);

    if (actor == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Actor not found.")
          .build();
    }

    actor.setFirstName(jsonActorObject.getString("firstName"));
    actor.setLastName(jsonActorObject.getString("lastName"));

    // checks if film exists
    int filmId;
    try {
      String[] filmStringArray = jsonActorObject.getJsonObject("films").getString("href")
          .split("/");
      filmId = Integer.parseInt(filmStringArray[filmStringArray.length - 1]);
    } catch (NumberFormatException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no ActorId found")
          .build();
    }

    Film film = filmService.getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Actor not found")
          .build();
    }

    em.merge(actor);

    // creates FilmActor
    List<FilmActor> filmActors = new LinkedList<>();
    FilmActor filmActor = filmActorService.createFilmActor(film, actor);
    if (filmActor != null) {
      filmActors.add(filmActor);
    }

    actor.setFilmActors(filmActors);

    return Response.ok().entity("Actor updated").build();
  }

  /**
   * Deletes an existing Actor object from the database.
   *
   * @param actorId Id of the Actor to delete
   * @return Response with status code and message
   */
  public Response deleteActorById(int actorId) {
    Actor actor = getActor(actorId);
    if (actor == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Actor not found.")
          .build();
    }

    em.remove(em.contains(actor) ? actor : em.merge(actor));
    em.flush();
    return Response.status(Response.Status.NO_CONTENT).build();

  }

  /**
   * Returns a Actor object.
   *
   * @param actorId Id of the Actor to get
   * @return an actor object
   */
  public Actor getActor(int actorId) {
    return em.find(Actor.class, actorId);
  }

  /**
   * Returns a JsonObject containing the actor data.
   *
   * @param actorId Id of the Actor to get
   * @return JsonObject containing the actor data
   */
  public Response getActorById(int actorId) {
    Actor actor = em.find(Actor.class, actorId);
    if (actor == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Actor not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectActorBuilder(actor)).build();
  }

  /**
   * Returns the last actorId in the database.
   *
   * @return Response with status code and message
   */
  public Response getLastActor() {
    return Response.ok().entity(em.createNamedQuery("Actor.getLastActor", Actor.class)
            .setMaxResults(1)
            .getSingleResult()
            .getActorId())
        .build();
  }

  /**
   * Get all actors. The actors are returned as a JSON array. The limit and offset can be used to
   * limit the number of actors returned.
   *
   * @param limit  The maximum number of actors to return.
   * @param offset The number of actors to skip.
   * @return A response with the status code and a message.
   */
  public Response getAllActorsLimit(int limit, int offset) {
    return Response.ok(em.createNamedQuery("Actor.getAll", Actor.class)
            .setFirstResult(offset)
            .setMaxResults(limit > 100 ? 100 : limit)
            .getResultList())
        .build();
  }

  /**
   * Gets a list of all Film objects of the Actor.
   *
   * @param actorId Id of the Actor
   * @return a list of all Film objects of the Actor
   */
  public Response getFilmListOfActorById(int actorId) {
    Actor actor = getActor(actorId);
    if (actor == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Actor not found.")
          .build();
    }

    List<FilmActor> filmActorList = actor.getFilmActors();
    List<Film> filmList = new LinkedList<>();

    for (FilmActor filmActor : filmActorList) {
      filmList.add(filmActor.getFilm());
    }

    if (filmList.isEmpty()) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    return Response.ok(filmList).build();
  }

  /**
   * Get the number of all Actor objects in the database.
   *
   * @return the number of all Actor objects in the database
   */
  public Response getActorCount() {
    return Response.ok(em.createNamedQuery("Actor.getAll", Actor.class).getResultList().size())
        .build();
  }

  /**
   * Builds a JsonObject containing the actor data.
   *
   * @param actor Actor object
   * @return JsonObject containing the actor data
   */
  public JsonObject jsonObjectActorBuilder(Actor actor) {
    return Json.createObjectBuilder()
        .add("actor", Json.createObjectBuilder()
            .add("films", Json.createObjectBuilder()
                .add("href",
                    "http://localhost:8080/dvd-rental-film/resources/films/"
                        + actor.getFilmActors().get(0).getFilm().getFilmId())
                .build())
            .add("firstName", actor.getFirstName())
            .add("id", actor.getActorId())
            .add("lastName", actor.getLastName()))
        .build();
  }
}