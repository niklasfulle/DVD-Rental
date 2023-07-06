package de.niklasfulle.dvdrentalstore.endpoints;

import jakarta.ws.rs.PUT;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.io.InputStream;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.json.JsonObject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import de.niklasfulle.dvdrentalstore.serviceses.InventoryService;
import de.niklasfulle.dvdrentalstore.serviceses.JsonBuilderService;
import de.niklasfulle.dvdrentalstore.serviceses.RentalService;
import de.niklasfulle.dvdrentalstore.serviceses.StaffService;

/**
 * Endpoint for rentals. The rentals are stored in the database.
 */
@Path("/rentals")
public class RentalEndpoint {

  // Services
  @Inject
  RentalService rentalService;

  @Inject
  StaffService staffService;

  @Inject
  InventoryService inventoryService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Create a new rental. The address data is rental as a JSON object in the request body.
   *
   * @param rentalStream The rental data as a JSON object.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createRental(InputStream rentalStream) {
    JsonObject jsonRentalObject = jsonBuilderService.createJsonObjectFromStream(rentalStream);
    if (jsonRentalObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("no valid json")
          .build();
    }

    return rentalService.createRental(jsonRentalObject);
  }

  /**
   * Gets the rental with the given id.
   *
   * @param rentalId The id of the rental.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRentalsById(@PathParam("id") int rentalId) {
    return rentalService.getRentalsById(rentalId);
  }

  /**
   * Returns the rental with the given id.
   *
   * @param rentalId The id of the rental.
   * @return A response with the status code and a message.
   */
  @PUT
  @Path("/{id}/returned")
  @Produces(MediaType.TEXT_PLAIN)
  public Response putRentalsByIdReturned(@PathParam("id") int rentalId) {
    return rentalService.putRentalsByIdReturned(rentalId);
  }

  @GET
  @Path("/test/returned")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getTestRentalNoReturnDate() {
    return rentalService.getLastRentalNoReturnDate();
  }
}