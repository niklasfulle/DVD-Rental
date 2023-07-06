package de.niklasfulle.dvdrentalcustomer.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.io.InputStream;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.json.JsonObject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import de.niklasfulle.dvdrentalcustomer.entities.Country;
import de.niklasfulle.dvdrentalcustomer.serviceses.CityService;
import de.niklasfulle.dvdrentalcustomer.serviceses.CountryService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;

/**
 * Endpoint for cities. The cities are stored in the database.
 */
@Path("/cities")
public class CitiesEndpoint {

  // Services
  @Inject
  CityService cityService;

  @Inject
  CountryService countryService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Creates a new city. The city is stored in the database. The country must exist.
   *
   * @param cityStream The city data as JSON.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createCity(InputStream cityStream) {
    JsonObject jsonCityObject = jsonBuilderService.createJsonObjectFromStream(cityStream);
    if (jsonCityObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Bad city data.")
          .build();
    }

    Country country = countryService.getCountryByName(jsonCityObject.getString("country"));

    if (country == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Country does not exist.")
          .build();
    }

    return cityService.createCity(jsonCityObject, country);
  }

  /**
   * Get a city by its Id. The city is returned as a JSON object.
   *
   * @param cityId The Id of the city.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCityById(@PathParam("id") int cityId) {
    return cityService.getCityById(cityId);
  }

  /**
   * Get all cities. The cities are returned as a JSON array. The limit and offset can be used to
   * limit the number of cities returned.
   *
   * @param limit  The maximum number of cities to return.
   * @param offset The number of cities to skip.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/cities")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCitiesLimit(@QueryParam("limit") @DefaultValue("600") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return cityService.getCitiesLimit(limit, offset);
  }
}