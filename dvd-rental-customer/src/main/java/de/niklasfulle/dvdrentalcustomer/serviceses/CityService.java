package de.niklasfulle.dvdrentalcustomer.serviceses;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import de.niklasfulle.dvdrentalcustomer.entities.City;
import de.niklasfulle.dvdrentalcustomer.entities.Country;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;

/**
 * Service for City entity.
 */
@Stateless
public class CityService {

  @PersistenceContext
  EntityManager em;

  public Response createCity(JsonObject jsonCityObject, Country country) {
    try {
      City city = new City(
          jsonCityObject.getString("city"),
          country,
          Timestamp.from(Instant.now())
      );

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

  public City getCityByName(String cityName) {
    List<City> cityList = em.createNamedQuery("City.getCityByName", City.class)
        .setParameter(1, cityName)
        .setMaxResults(1)
        .getResultList();

    return cityList.size() == 1 ? cityList.get(0) : null;
  }

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