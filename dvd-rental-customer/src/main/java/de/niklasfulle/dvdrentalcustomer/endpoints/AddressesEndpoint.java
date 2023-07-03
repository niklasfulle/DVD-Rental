package de.niklasfulle.dvdrentalcustomer.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.io.InputStream;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.json.JsonObject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import de.niklasfulle.dvdrentalcustomer.entities.City;
import de.niklasfulle.dvdrentalcustomer.serviceses.CityService;
import de.niklasfulle.dvdrentalcustomer.serviceses.AddressService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;

/**
 * Endpoint for addresses.
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

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAddressesLimit(@QueryParam("limit") @DefaultValue("20") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return addressService.getAllAddressesLimit(limit, offset);
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAddressByID(@PathParam("id") int addressId) {
    return addressService.getAddressByID(addressId);
  }

  @GET
  @Path("/cities")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCitiesLimit(@QueryParam("limit") @DefaultValue("600") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return cityService.getCitiesLimit(limit, offset);
  }

  @GET
  @Path("/lastaddressid")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLastAddressID() {
    return addressService.getLastAddressID();
  }
}
