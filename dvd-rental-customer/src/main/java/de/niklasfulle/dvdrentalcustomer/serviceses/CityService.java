package de.niklasfulle.dvdrentalcustomer.serviceses;

import de.niklasfulle.dvdrentalcustomer.entities.City;
import de.niklasfulle.dvdrentalcustomer.entities.Country;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Service for City entity.
 */
@Stateless
public class CityService {

  @PersistenceContext
  EntityManager em;

  /**
   * Creates a new City object and persists it to the database.
   *
   * @param jsonCityObject JsonObject containing the city data
   * @param country        Country object
   * @return Response with status code and message
   */
  public Response createCity(JsonObject jsonCityObject, Country country) {
    try {
      City city = new City(
          jsonCityObject.getString("city"),
          country,
          Timestamp.from(Instant.now()));

      em.persist(city);
      em.flush();

      return Response.status(Response.Status.CREATED)
          .entity("City created")
          .build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Couldn't add City to Database.")
          .build();
    }
  }

  /**
   * Gets a City object from the database and returns it as a JSON object.
   *
   * @param cityId City id
   * @return Response with status code and message
   */
  public Response getCityById(int cityId) {
    City city = em.find(City.class, cityId);
    if (city == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok().entity(jsonObjectCityBuilder(city)).build();
  }

  /**
   * Gets a City object from the database by name.
   *
   * @param cityName City name
   * @return City object
   */
  public City getCityByName(String cityName) {
    List<City> cityList = em.createNamedQuery("City.getCityByName", City.class)
        .setParameter(1, cityName)
        .setMaxResults(1)
        .getResultList();

    return cityList.size() == 1 ? cityList.get(0) : null;
  }

  /**
   * Get all cities. The cities are returned as a JSON array. The limit and offset can be used to
   * limit the number of cities returned.
   *
   * @param limit  The maximum number of cities to return.
   * @param offset The number of cities to skip.
   * @return A response with the status code and a message.
   */
  public Response getCitiesLimit(int limit, int offset) {
    List<City> cityList = em.createNamedQuery("City.getAll", City.class)
        .setFirstResult(offset)
        .setMaxResults(limit > 600 ? 600 : limit)
        .getResultList();

    List<JsonObject> response = new LinkedList<>();
    for (City city : cityList) {
      response.add(jsonObjectCityBuilder(city));
    }

    return Response.ok().entity(response).build();
  }

  /**
   * Builds a JsonObject from a City object.
   *
   * @param payment City object
   * @return JsonObject of City
   */
  public JsonObject jsonObjectCityBuilder(City city) {
    return Json.createObjectBuilder()
        .add("id", city.getCityId())
        .add("city", city.getCity())
        .add("country", city.getCountry().getCountry())
        .build();
  }
}