package de.niklasfulle.dvdrentalcustomer.serviceses;

import de.niklasfulle.dvdrentalcustomer.entities.Country;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;

/**
 * Service for Country entity.
 */
public class CountryService {

  @PersistenceContext
  EntityManager em;

  public Response createCity(JsonObject jsonCountryObject) {
    try {

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
   * Builds a JsonObject from a Country object.
   *
   * @param payment Country object
   * @return JsonObject of Country
   */
  public JsonObject jsonObjectCityBuilder(Country country) {
    return Json.createObjectBuilder()
        .add("id", country.getCountryId())
        .add("country", country.getCountry())
        .build();
  }
}
