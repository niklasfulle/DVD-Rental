package de.niklasfulle.dvdrentalfilm.serviceses;

import java.util.List;
import java.time.Instant;
import java.sql.Timestamp;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.niklasfulle.dvdrentalfilm.entities.Category;

/**
 * Service for Category entity.
 */
@Stateless
public class CategoryService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Creates a new category object and persists it to the database.
   *
   * @param jsonCategoryObject JsonObject containing the category data
   * @return Response with status code and message
   */
  public Response createCategory(JsonObject jsonCategoryObject) {
    String categoryName = jsonCategoryObject.getString("category");
    Category category = getCategoryByName(categoryName);
    if (category != null) {
      return Response.status(Response.Status.CONFLICT)
          .entity("Language already exists")
          .build();
    }

    category = new Category(categoryName, Timestamp.from(Instant.now()));

    em.persist(category);

    return Response.status(Response.Status.CREATED)
        .entity("Category created")
        .build();
  }

  /**
   * Gets all categories from the database.
   *
   * @return List of all categories
   */
  public Response getAllCategories() {
    return Response.ok(em.createNamedQuery("Category.getAll", Category.class)
            .getResultList())
        .build();
  }

  /**
   * Gets a category by its name.
   *
   * @param categoryName Name of the category
   * @return Category object
   */
  public Category getCategoryByName(String categoryName) {
    List<Category> list = em.createNamedQuery("Category.getCategoryByName", Category.class)
        .setParameter(1, categoryName)
        .setMaxResults(1)
        .getResultList();

    return !list.isEmpty() ? list.get(0) : null;
  }
}