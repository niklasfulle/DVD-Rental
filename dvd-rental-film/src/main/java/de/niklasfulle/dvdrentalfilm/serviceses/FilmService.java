package de.niklasfulle.dvdrentalfilm.serviceses;

import de.niklasfulle.dvdrentalfilm.entities.Actor;
import de.niklasfulle.dvdrentalfilm.entities.Category;
import de.niklasfulle.dvdrentalfilm.entities.Film;
import de.niklasfulle.dvdrentalfilm.entities.FilmActor;
import de.niklasfulle.dvdrentalfilm.entities.FilmCategory;
import de.niklasfulle.dvdrentalfilm.entities.Language;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Service for Film entity.
 */
@Stateless
public class FilmService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Services
  @Inject
  ActorService actorService;

  @Inject
  LanguageService languageService;

  @Inject
  FilmActorService filmActorService;

  @Inject
  FilmCategoryService filmCategoryService;

  @Inject
  CategoryService categoryService;

  // Methods

  /**
   * Creates a new Film object and persists it to the database.
   *
   * @param jsonFilmObject JsonObject containing the film data
   * @return Response with status code and message
   */
  public Response createFilm(JsonObject jsonFilmObject) {
    // checks if language exists
    Language language = languageService.getLanguageByName(jsonFilmObject.getString("language"));
    if (language == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("no Language found")
          .build();
    }

    // checks if actor exists
    int actorId;
    try {
      String[] actorStringArray = jsonFilmObject.getJsonObject("actors").getString("href")
          .split("/");
      actorId = Integer.parseInt(actorStringArray[actorStringArray.length - 1]);

    } catch (NumberFormatException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no ActorId found")
          .build();
    }

    // checks if actor exists
    Actor actor = actorService.getActor(actorId);
    if (actor == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Actor not found")
          .build();
    }

    try {
      Film film = new Film(jsonFilmObject.getString("description"), language,
          Integer.valueOf(jsonFilmObject.getInt("length")).shortValue(),
          jsonFilmObject.getString("rating"),
          Integer.valueOf(jsonFilmObject.getInt("releaseYear")).shortValue(),
          Integer.valueOf(jsonFilmObject.getInt("rentalDuration")).shortValue(),
          new BigDecimal(jsonFilmObject.getInt("rentalRate")),
          new BigDecimal(jsonFilmObject.getInt("replacementCost")),
          jsonFilmObject.getString("title"),
          Timestamp.from(Instant.now()));

      em.persist(film);
      em.flush();

      List<FilmActor> filmActors = new LinkedList<>();
      FilmActor filmActor = filmActorService.createFilmActor(film, actor);
      if (filmActor != null) {
        filmActors.add(filmActor);
      }

      film.setFilmActors(filmActors);

      JsonArray jsonCategories = jsonFilmObject.getJsonArray("categories");

      List<Category> categories = new LinkedList<>();
      for (int i = 0; i < jsonCategories.size(); i++) {
        categories.add(categoryService.getCategoryByName(jsonCategories.getString(i)));
      }

      // creates FilmCategory
      List<FilmCategory> filmCategories = new LinkedList<>();
      for (Category category : categories) {
        FilmCategory filmCategory = filmCategoryService.createFilmCategory(film, category);
        if (filmCategory != null) {
          filmCategories.add(filmCategory);
        }
      }

      film.setFilmCategories(filmCategories);
      return Response.status(Response.Status.CREATED)
          .entity("Film created.")
          .build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Couldn't add Film to Database.")
          .build();
    }
  }

  /**
   * Patch a film by id. Only the fields that are present in the json object will be updated.
   *
   * @param filmId         id of the film to patch
   * @param jsonFilmObject json object containing the fields to update
   * @return Response with status code and message
   */
  public Response patchFilmById(int filmId, JsonObject jsonFilmObject) {
    Film film = getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    film.setRentalDuration(Integer.valueOf(jsonFilmObject.getInt("rentalDuration")).shortValue());
    film.setRentalRate(new BigDecimal(jsonFilmObject.getInt("rentalRate")));
    film.setReplacementCost(new BigDecimal(jsonFilmObject.getInt("replacementCost")));
    film.setTitle(jsonFilmObject.getString("title"));
    film.setLastUpdate(Timestamp.from(Instant.now()));

    em.merge(film);

    return Response.status(Response.Status.NO_CONTENT)
        .entity("Film updated")
        .build();
  }

  /**
   * Deletes a film by id. If the film is not found, a 404 response is returned.
   *
   * @param filmId id of the film to delete
   * @return Response with status code and message
   */
  public Response deleteFilmeById(int filmId) {
    Film film = getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    em.remove(em.contains(film) ? film : em.merge(film));
    em.flush();

    return Response.status(Response.Status.NO_CONTENT)
        .entity("Film was deleted.")
        .build();
  }

  /**
   * Adds an actor to a film. If the actor or the film is not found, a 404 response is returned.
   *
   * @param filmId  id of the film
   * @param actorId id of the actor
   * @return Response with status code and message
   */
  public Response addActorToFilm(int filmId, int actorId) {
    Actor actor = actorService.getActor(actorId);
    if (actor == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Actor not found.")
          .build();
    }

    Film film = getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    FilmActor filmActor = filmActorService.createFilmActor(film, actor);
    if (filmActor == null) {
      return Response.status(Response.Status.NOT_ACCEPTABLE)
          .entity("could not execute statement, key already exists")
          .build();
    }

    film.addFilmActor(filmActor);

    return Response.status(Response.Status.CREATED)
        .entity("Actor added to film.")
        .build();
  }

  /**
   * Adds a category to a film. If the category or the film is not found, a 404 response is
   * returned.
   *
   * @param filmId       id of the film
   * @param categoryName name of the category
   * @return Response with status code and message
   */
  public Response addCategoryToFilm(int filmId, String categoryName) {
    Category category = categoryService.getCategoryByName(categoryName);
    if (category == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Category not found.")
          .build();
    }

    Film film = getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    FilmCategory filmCategory = filmCategoryService.createFilmCategory(film, category);
    if (filmCategory == null) {
      return Response.status(Response.Status.NOT_ACCEPTABLE)
          .entity("could not execute statement, key already exists")
          .build();
    }

    film.addFilmCategory(filmCategory);

    return Response.status(Response.Status.CREATED)
        .entity("Category added to film.")
        .build();
  }

  /**
   * Gets a film by id. If the film is not found, a 404 response is returned.
   *
   * @param filmId id of the film
   * @return film or null
   */
  public Film getFilm(int filmId) {
    Film film = em.find(Film.class, filmId);
    return film;
  }

  /**
   * Gets a film by id. If the film is not found, a 404 response is returned.
   *
   * @param filmId id of the film
   * @return Response with status code and message
   */
  public Response getFilmById(int filmId) {
    Film film = getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectFilmBuilder(film)).build();
  }

  /**
   * Gets all films. If the limit is greater than 100, the limit is set to 100.
   *
   * @param limit  limit of films
   * @param offset offset of films
   * @return Response with status code and message
   */
  public Response getAllFilmsLimit(int limit, int offset) {
    List<Film> flimList = em.createNamedQuery("Film.getAll", Film.class)
        .setFirstResult(offset)
        .setMaxResults(limit > 100 ? 100 : limit)
        .getResultList();

    List<JsonObject> response = new LinkedList<>();
    for (Film film : flimList) {
      response.add(jsonObjectFilmBuilder(film));
    }

    return Response.ok().entity(response).build();
  }

  /**
   * Gets all actors of a film. If the film is not found, a 404 response is
   *
   * @param filmId id of the film
   * @return all actors of a film
   */
  public Response getActorsByFilmId(int filmId) {
    Film film = getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    List<JsonObject> response = new LinkedList<>();
    for (FilmActor filmActor : film.getFilmActors()) {
      response.add(jsonObjectActorBuilder(filmActor));
    }

    return Response.status(Response.Status.OK).entity(response).build();
  }

  /**
   * Gets all categories of a film. If the film is not found, a 404 response is
   *
   * @param filmId id of the film
   * @return all categories of a film
   */
  public Response getCategoriesByFilmId(int filmId) {
    Film film = getFilm(filmId);
    if (film == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Film not found.")
          .build();
    }

    List<JsonObject> response = new LinkedList<>();
    for (FilmCategory filmCategory : film.getFilmCategories()) {
      response.add(jsonObjectCategoryBuilder(filmCategory));
    }

    return Response.status(Response.Status.OK).entity(response).build();
  }

  /**
   * Gets the count of all films.
   *
   * @return Response with status code and message
   */
  public Response getFilmCount() {
    return Response.ok().entity(em.createNamedQuery("Film.getAll", Film.class)
            .getResultList()
            .size())
        .build();
  }

  /**
   * Gets the last film.
   *
   * @return last film
   */
  public Response getLastFilm() {
    return Response.ok().entity(em.createNamedQuery("Film.getLastFilm", Film.class)
            .setMaxResults(1)
            .getSingleResult()
            .getFilmId())
        .build();
  }

  /**
   * Builds a JsonObject of an film.
   *
   * @param film film object
   * @return JsonObject of film
   */
  public JsonObject jsonObjectFilmBuilder(Film film) {
    List<String> categories = new LinkedList<>();
    for (FilmCategory filmCategory : film.getFilmCategories()) {
      categories.add(filmCategory.getCategory().getName());
    }

    return Json.createObjectBuilder()
        .add("actors", Json.createObjectBuilder()
            .add("href",
                "http://localhost:8080/dvd-rental-film/resources/actors/"
                    + film.getFilmActors().get(0).getActor().getActorId())
            .build())
        .add("categories", Json.createArrayBuilder(categories).build())
        .add("description", film.getDescription())
        .add("id", film.getFilmId())
        .add("language", film.getLanguage().getName())
        .add("length", film.getLength())
        .add("rating", film.getRating())
        .add("releaseYear", film.getReleaseYear())
        .add("rentalDuration", film.getRentalDuration())
        .add("rentalRate", film.getRentalRate())
        .add("replacementCost", film.getReplacementCost())
        .add("title", film.getTitle())
        .build();
  }

  /**
   * Builds a JsonObject of a category.
   *
   * @param filmCategory filmCategory object
   * @return JsonObject of category
   */
  public JsonObject jsonObjectCategoryBuilder(FilmCategory filmCategory) {
    return Json.createObjectBuilder()
        .add("id", filmCategory.getCategory().getCategoryId())
        .add("name", filmCategory.getCategory().getName())
        .build();
  }

  /**
   * Builds a JsonObject of an actor.
   *
   * @param filmActor filmActor object
   * @return JsonObject of actor
   */
  public JsonObject jsonObjectActorBuilder(FilmActor filmActor) {
    return Json.createObjectBuilder()
        .add("id", filmActor.getActor().getActorId())
        .add("firstname", filmActor.getActor().getFirstName())
        .add("lastname", filmActor.getActor().getLastName())
        .build();
  }
}
