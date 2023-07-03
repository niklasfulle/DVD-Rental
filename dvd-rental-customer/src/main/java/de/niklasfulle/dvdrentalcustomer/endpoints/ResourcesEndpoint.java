package de.niklasfulle.dvdrentalcustomer.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

/**
 * The entry point for the API.
 */
@Path("/")
public class ResourcesEndpoint {

  // Endpoints
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public Response resources() {
    return Response.status(Response.Status.OK)
        .entity("Returns array of primary resources")
        .build();
  }
}