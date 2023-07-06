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

import de.niklasfulle.dvdrentalcustomer.serviceses.CountryService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;

/**
 * Endpoint for coutries. The countries are stored in the database.
 */
@Path("/countries")
public class CountriesEndpoint {

  // Services
  @Inject
  CountryService countryService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Creates a new country. The country is stored in the database.
   *
   * @param countryStream The country data as JSON.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createCountry(InputStream countryStream) {
    JsonObject jsonCountryObject = jsonBuilderService.createJsonObjectFromStream(countryStream);
    if (jsonCountryObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Bad country data.")
          .build();
    }

    return countryService.createCountry(jsonCountryObject);
  }

  /**
   * Get a country by its Id. The country is returned as a JSON object.
   *
   * @param countryId The Id of the country.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCountryById(@PathParam("id") int countryId) {
    return countryService.getCountryById(countryId);
  }

  /**
   * Get all countries. The countries are returned as a JSON array. The limit and offset can be used
   * to limit the number of countries returned.
   *
   * @param limit  The maximum number of countries to return.
   * @param offset The number of countries to skip.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/countries")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCountriesLimit(@QueryParam("limit") @DefaultValue("600") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return countryService.getCountriesLimit(limit, offset);
  }
}