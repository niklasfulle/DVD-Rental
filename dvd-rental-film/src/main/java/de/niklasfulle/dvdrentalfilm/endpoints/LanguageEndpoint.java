package de.niklasfulle.dvdrentalfilm.endpoints;

import java.io.InputStream;

import de.niklasfulle.dvdrentalfilm.serviceses.JsonBuilderService;
import de.niklasfulle.dvdrentalfilm.serviceses.LanguageService;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Endpoint for languages. The languages are stored in the database.
 */
@Path("/languages")
public class LanguageEndpoint {

  // Services
  @Inject
  LanguageService languageService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Creates a new language. The language is stored in the database.
   *
   * @param languageStream The language data as JSON.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createLanguage(InputStream languageStream) {
    JsonObject jsonLangugageObject = jsonBuilderService.createJsonObjectFromStream(languageStream);
    if (jsonLangugageObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Bad language data.")
          .build();
    }

    return languageService.createLanguage(jsonLangugageObject);
  }

  /**
   * Returns all languages.
   * 
   * @return all languages
   */
  @GET
  @Path("")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLanguage() {
    return languageService.getAllLanguages();
  }
}
