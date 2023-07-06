package de.niklasfulle.dvdrentalstore.endpoints;

import de.niklasfulle.dvdrentalstore.serviceses.InventoryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint for inventories. The inventories are stored in the database.
 */
@Path("/inventories")
public class InventoryEndpoint {

  // Services
  @Inject
  InventoryService inventoryService;

  // Endpoints

  /**
   * 
   * @param inventoryId The id of the inventory.
   * @return
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getInventoryById(@PathParam("id") int inventoryId) {
    return inventoryService.getInventoryById(inventoryId);
  }

  /**
   * 
   * @param filmId The id of the film.
   * @return
   */
  @GET
  @Path("/film/{filmId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getInventoryListByFilmId(@PathParam("filmId") int filmId) {
    return inventoryService.getInventoryListByFilmId(filmId);
  }

}
