package de.niklasfulle.dvdrentalcustomer.endpoints;

import de.niklasfulle.dvdrentalcustomer.entities.City;
import de.niklasfulle.dvdrentalcustomer.serviceses.AddressService;
import de.niklasfulle.dvdrentalcustomer.serviceses.CityService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Endpoint for addresses. The addresses are stored in the database.
 */
@Path("/addresses")
public class AddressesEndpoint {

  // Services
  @Inject
  AddressService addressService;

  @Inject
  CityService cityService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Create a new address. The address data is passed as a JSON object in the request body. The city
   * and Country must exist.
   *
   * @param addressStream The address data as a JSON object.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createAddress(InputStream addressStream) {
    JsonObject jsonAddressObject = jsonBuilderService.createJsonObjectFromStream(addressStream);
    if (jsonAddressObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Bad address data.")
          .build();
    }

    City city = cityService.getCityByName(jsonAddressObject.getString("city"));

    if (city == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Country and/or City do not exist.")
          .build();
    }

    return addressService.createAddress(jsonAddressObject, city);
  }

  /**
   * Get an address by its Id. The address is returned as a JSON object.
   *
   * @param addressId The Id of the address.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAddressById(@PathParam("id") int addressId) {
    return addressService.getAddressById(addressId);
  }

  /**
   * Get all addresses. The addresses are returned as a JSON array. The limit and offset can be used
   * to limit the number of addresses returned.
   *
   * @param limit  The maximum number of addresses to return.
   * @param offset The number of addresses to skip.
   * @return A response with the status code and a message.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAddressesLimit(@QueryParam("limit") @DefaultValue("20") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return addressService.getAllAddressesLimit(limit, offset);
  }

  /**
   * Get the Id of the last address.
   *
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/lastaddress")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLastAddressId() {
    return addressService.getLastAddressId();
  }
}
