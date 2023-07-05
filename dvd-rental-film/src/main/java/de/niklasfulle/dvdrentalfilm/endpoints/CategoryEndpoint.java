package de.niklasfulle.dvdrentalfilm.endpoints;

import de.niklasfulle.dvdrentalfilm.serviceses.CategoryService;
import de.niklasfulle.dvdrentalfilm.serviceses.JsonBuilderService;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Endpoint for categories. The categories are stored in the database.
 */
@Path("/categories")
public class CategoryEndpoint {

  // Services
  @Inject
  CategoryService categoryService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Creates a new category. The category is stored in the database.
   *
   * @param categoryStream The category data as JSON.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createCategory(InputStream categoryStream) {
    JsonObject jsonCategoryObject = jsonBuilderService.createJsonObjectFromStream(categoryStream);
    if (jsonCategoryObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Bad Category data.")
          .build();
    }

    return categoryService.createCategory(jsonCategoryObject);
  }

  /**
   * Returns all categories.
   *
   * @return all categories
   */
  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllCategories() {
    return categoryService.getAllCategories();
  }
}
