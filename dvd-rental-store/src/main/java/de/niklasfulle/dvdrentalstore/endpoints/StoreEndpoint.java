package de.niklasfulle.dvdrentalstore.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import de.niklasfulle.dvdrentalstore.serviceses.StoreService;

/**
 * Endpoint for stores. The stores are stored in the database.
 */
@Path("/stores")
public class StoreEndpoint {

  // Services
  @Inject
  StoreService storeService;

  // Endpoints

  /**
   * Gets the store with the given id.
   *
   * @param storeId The id of the store.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getStoreById(@PathParam("id") int storeId) {
    return storeService.getStoreById(storeId);
  }

  /**
   * Returns the number of stores.
   *
   * @return The number of stores.
   */
  @GET
  @Path("/count")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getStoreCount() {
    return storeService.getStoreCount();
  }
}