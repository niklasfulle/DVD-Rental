package de.niklasfulle.dvdrentalstore.serviceses;

import java.util.List;
import jakarta.json.Json;
import java.time.Instant;
import java.sql.Timestamp;
import java.util.LinkedList;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.niklasfulle.dvdrentalstore.entities.Country;

/**
 * Service for Country entity.
 */
@Stateless
public class CountryService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Creates a new Country object and persists it to the database.
   *
   * @param jsonCountryObject JsonObject containing the country data
   * @return Response with status code and message
   */
  public Response createCountry(JsonObject jsonCountryObject) {
    try {
      Country country = new Country(
          jsonCountryObject.getString("country"),
          Timestamp.from(Instant.now()));

      em.persist(country);
      em.flush();

      return Response.status(Response.Status.CREATED)
          .entity("Country created")
          .build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Couldn't add Country to Database.")
          .build();
    }
  }

  /**
   * Gets a Country object from the database and returns it as a JSON object.
   *
   * @param countryId Country id
   * @return Response with status code and message
   */
  public Response getCountryById(int countryId) {
    Country country = em.find(Country.class, countryId);
    if (country == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok().entity(jsonObjectCountryBuilder(country)).build();
  }

  /**
   * Gets a Country object from the database by name.
   *
   * @param countryName Country name
   * @return Country object
   */
  public Country getCountryByName(String countryName) {
    List<Country> countryList = em.createNamedQuery("Country.getCountryByName", Country.class)
        .setParameter(1, countryName)
        .setMaxResults(1)
        .getResultList();

    return countryList.size() == 1 ? countryList.get(0) : null;
  }

  /**
   * Get all countries. The countries are returned as a JSON array. The limit and
   * offset can be used
   * to limit the number of countries returned.
   *
   * @param limit  The maximum number of countries to return.
   * @param offset The number of countries to skip.
   * @return A response with the status code and a message.
   */
  public Response getCountriesLimit(int limit, int offset) {
    List<Country> countryList = em.createNamedQuery("Country.getAll", Country.class)
        .setFirstResult(offset)
        .setMaxResults(limit > 600 ? 600 : limit)
        .getResultList();

    List<JsonObject> response = new LinkedList<>();
    for (Country country : countryList) {
      response.add(jsonObjectCountryBuilder(country));
    }

    return Response.ok().entity(response).build();
  }

  /**
   * Builds a JsonObject from a Country object.
   *
   * @param country Country object
   * @return JsonObject of Country
   */
  public JsonObject jsonObjectCountryBuilder(Country country) {
    return Json.createObjectBuilder()
        .add("id", country.getCountryId())
        .add("country", country.getCountry())
        .build();
  }
}