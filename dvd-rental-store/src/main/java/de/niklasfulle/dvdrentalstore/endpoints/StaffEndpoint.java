package de.niklasfulle.dvdrentalstore.endpoints;

import de.niklasfulle.dvdrentalstore.serviceses.StaffService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint for staff. The staff is stored in the database.
 */
@Path("/staff")
public class StaffEndpoint {

  // Services
  @Inject
  StaffService staffService;

  // Endpoints

  /**
   * Gets the staff with the given id.
   * 
   * @param staffId The id of the staff.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getStaffById(@PathParam("id") int staffId) {
    return staffService.getStaffById(staffId);
  }

}
