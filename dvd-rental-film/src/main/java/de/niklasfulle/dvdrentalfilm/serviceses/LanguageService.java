package de.niklasfulle.dvdrentalfilm.serviceses;

import de.niklasfulle.dvdrentalfilm.entities.Language;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Service for Language entity.
 */
@Stateless
public class LanguageService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Creates a new Language object and persists it to the database.
   *
   * @param jsonLanguageObject JsonObject containing the language data
   * @return Response with status code and message
   */
  public Response createLanguage(JsonObject jsonLanguageObject) {
    String languageName = jsonLanguageObject.getString("language");
    Language language = getLanguageByName(languageName);
    if (language != null) {
      return Response.status(Response.Status.CONFLICT)
          .entity("Language already exists")
          .build();
    }

    language = new Language(languageName, Timestamp.from(Instant.now()));

    em.persist(language);

    return Response.ok(language)
        .build();
  }

  /**
   * Returns all languages.
   *
   * @return Response with status code and message
   */
  public Response getAllLanguages() {
    return Response.ok(em.createNamedQuery("Language.getAll", Language.class)
            .getResultList())
        .build();
  }

  /**
   * Gets a language by its name.
   *
   * @param languageName Name of the language
   * @return Language object
   */
  public Language getLanguageByName(String languageName) {
    List<Language> languageList = em.createNamedQuery("Language.getLanguageByName", Language.class)
        .setParameter(1, languageName)
        .setMaxResults(1)
        .getResultList();

    return languageList.size() == 1 ? languageList.get(0) : null;
  }
}
