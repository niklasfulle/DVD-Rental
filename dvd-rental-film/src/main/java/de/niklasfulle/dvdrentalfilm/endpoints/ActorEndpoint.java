package de.niklasfulle.dvdrentalfilm.endpoints;

import de.niklasfulle.dvdrentalfilm.serviceses.ActorService;
import de.niklasfulle.dvdrentalfilm.serviceses.FilmActorService;
import de.niklasfulle.dvdrentalfilm.serviceses.FilmService;
import de.niklasfulle.dvdrentalfilm.serviceses.JsonBuilderService;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Endpoint for actors. The actors are stored in the database.
 */
@Path("/actors")
public class ActorEndpoint {

  // Services
  @Inject
  ActorService actorService;

  @Inject
  FilmService filmService;

  @Inject
  FilmActorService filmActorService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Create a new actor. The actor data is passed as a JSON object in the request body.
   *
   * @param actorStream The actor data as a JSON object.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createActor(InputStream actorStream) {
    JsonObject jsonActorObject = jsonBuilderService.createJsonObjectFromStream(actorStream);
    if (jsonActorObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no valid json")
          .build();
    }

    return actorService.createActor(jsonActorObject);
  }

  /**
   * Update a actor by its Id. The actor data is passed as a JSON object in the
   *
   * @param actorId     The Id of the actor.
   * @param actorStream The actor data as a JSON object.
   * @return A response with the status code and a message.
   */
  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response updateActorById(@PathParam("id") int actorId, InputStream actorStream) {
    JsonObject jsonActorObject = jsonBuilderService.createJsonObjectFromStream(actorStream);
    if (jsonActorObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no valid json")
          .build();
    }

    return actorService.updateActor(actorId, jsonActorObject);
  }

  /**
   * Delete a actor by its Id. The actor is deleted from the database.
   *
   * @param actorId The Id of the actor.
   * @return A response with the status code and a message.
   */
  @DELETE
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response deleteActorById(@PathParam("id") int actorId) {
    return actorService.deleteActorById(actorId);
  }

  /**
   * Get a actor by its Id. The actor is returned as a JSON object.
   *
   * @param actorId The Id of the actor.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getActorById(@PathParam("id") int actorId) {
    return actorService.getActorById(actorId);
  }

  /**
   * Get all actors. The actors are returned as a JSON array. The limit and offset can be used to
   * limit the number of actors returned.
   *
   * @param limit  The maximum number of actors to return.
   * @param offset The number of actors to skip.
   * @return A response with the status code and a message.
   */
  public Response getActorListLimit(@QueryParam("limit") @DefaultValue("100") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return actorService.getAllActorsLimit(limit, offset);
  }

  /**
   * Get all films of a actor by its Id. The films are returned as a JSON array.
   *
   * @param actorId The Id of the actor.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}/films")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFilmsByActorId(@PathParam("id") int actorId) {
    return actorService.getFilmListOfActorById(actorId);
  }

  /**
   * Returns the number of actors.
   *
   * @return The number of actors.
   */
  @GET
  @Path("/count")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getActorsCount() {
    return actorService.getActorCount();
  }

  @GET
  @Path("/test/lastactor")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTestPaymentDelete() {
    return actorService.getLastActor();
  }
}