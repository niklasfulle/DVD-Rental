package de.niklasfulle.dvdrentalfilm.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PATCH;
import java.io.InputStream;
import jakarta.ws.rs.DELETE;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.json.JsonObject;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import de.niklasfulle.dvdrentalfilm.serviceses.FilmService;
import de.niklasfulle.dvdrentalfilm.serviceses.ActorService;
import de.niklasfulle.dvdrentalfilm.serviceses.LanguageService;
import de.niklasfulle.dvdrentalfilm.serviceses.CategoryService;
import de.niklasfulle.dvdrentalfilm.serviceses.FilmActorService;
import de.niklasfulle.dvdrentalfilm.serviceses.JsonBuilderService;
import de.niklasfulle.dvdrentalfilm.serviceses.FilmCategoryService;

/**
 * Endpoint for films. The films are stored in the database.
 */
@Path("/films")
public class FilmEndpoint {

  // Services
  @Inject
  FilmService filmService;

  @Inject
  ActorService actorService;

  @Inject
  LanguageService languageService;

  @Inject
  CategoryService categoryService;

  @Inject
  FilmActorService filmActorService;

  @Inject
  FilmCategoryService filmCategoryService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Create a new filmStream. The filmStream data is passed as a JSON object in the request body.
   * The city and Country must exist.
   *
   * @param filmStream The film data as a JSON object.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createFilm(InputStream filmStream) {
    JsonObject jsonFilmObject = jsonBuilderService.createJsonObjectFromStream(filmStream);
    if (jsonFilmObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no valid json")
          .build();
    }

    return filmService.createFilm(jsonFilmObject);
  }

  /**
   * Patchs a film by its Id. The film data is passed as a JSON object in the
   *
   * @param filmId     The Id of the film.
   * @param filmStream The film data as a JSON object.
   * @return A response with the status code and a message.
   */
  @PATCH
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response patchFilmByID(@PathParam("id") int filmId, InputStream filmStream) {
    JsonObject jsonFilmObject = jsonBuilderService.createJsonObjectFromStream(filmStream);
    if (jsonFilmObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no vaild json")
          .build();
    }

    return filmService.patchFilmById(filmId, jsonFilmObject);
  }

  /**
   * Delete a film by its Id. The film is deleted from the database.
   *
   * @param filmId The Id of the film.
   * @return A response with the status code and a message.
   */
  @DELETE
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response deleteFilmByID(@PathParam("id") int filmId) {
    return filmService.deleteFilmeById(filmId);
  }

  /**
   * Get a film by its Id. The film is returned as a JSON object.
   *
   * @param filmId The Id of the film.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFilmById(@PathParam("id") int filmId) {
    return filmService.getFilmById(filmId);
  }

  /**
   * Get all films. The films are returned as a JSON array. The limit and offset can be used to
   * limit the number of films returned.
   *
   * @param limit  The maximum number of films to return.
   * @param offset The number of films to skip.
   * @return A response with the status code and a message.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllFilmsLimit(@QueryParam("limit") @DefaultValue("100") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return filmService.getAllFilmsLimit(limit, offset);
  }

  /**
   * Adds a category to a film by its Id.
   *
   * @param filmId       The Id of the film.
   * @param categoryName The name of the category.
   * @return A response with the status code and a message.
   */
  @PUT
  @Path("/{id}/categories/{category}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response addCategoryToFilm(@PathParam("id") int filmId,
      @PathParam("category") String categoryName) {
    return filmService.addCategoryToFilm(filmId, categoryName);
  }

  /**
   * Adds an actor to a film by its Id.
   *
   * @param filmId  The Id of the film.
   * @param actorId The Id of the actor.
   * @return A response with the status code and a message.
   */
  @PUT
  @Path("/{id}/actors/{actorId}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response addActorToFilm(@PathParam("id") int filmId, @PathParam("actorId") int actorId) {
    return filmService.addActorToFilm(filmId, actorId);
  }

  /**
   * Get the categories of a film by its Id.
   *
   * @param filmId The Id of the film.
   * @return The categories of the film.
   */
  @GET
  @Path("/{id}/categories")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCategoriesByFilmId(@PathParam("id") int filmId) {
    return filmService.getCategoriesByFilmId(filmId);
  }

  /**
   * Get the actors of a film by its Id.
   *
   * @param filmId The Id of the film.
   * @return The actors of the film.
   */
  @GET
  @Path("/{id}/actors")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getActorsByFilmId(@PathParam("id") int filmId) {
    return filmService.getActorsByFilmId(filmId);
  }

  /**
   * Returns the number of films.
   *
   * @return The number of films.
   */
  @GET
  @Path("/count")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getFilmCount() {
    return filmService.getFilmCount();
  }

  @GET
  @Path("/test/lastfilm")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTestPaymentDelete() {
    return filmService.getLastFilm();
  }
}