package de.niklasfulle.dvdrentalfilm.serviceses;

import java.util.List;
import java.time.Instant;
import java.sql.Timestamp;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.niklasfulle.dvdrentalfilm.entities.Category;
import de.niklasfulle.dvdrentalfilm.entities.Film;
import de.niklasfulle.dvdrentalfilm.entities.FilmCategory;
import de.niklasfulle.dvdrentalfilm.entities.FilmCategoryPK;

/**
 * Service for FilmCategory entity.
 */
@Stateless
public class FilmCategoryService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Creates a new FilmCategory object and persists it to the database.
   *
   * @param film     Film object
   * @param category Category object
   * @return FilmCategory object
   */
  public FilmCategory createFilmCategory(Film film, Category category) {
    if (checkFilmCategory(film, category)) {
      return null;
    }

    FilmCategoryPK filmCategoryPK = new FilmCategoryPK();
    filmCategoryPK.setFilmId(film.getFilmId());
    filmCategoryPK.setCategoryId(category.getCategoryId());

    FilmCategory filmCategory = new FilmCategory();
    filmCategory.setId(filmCategoryPK);
    filmCategory.setFilm(film);
    filmCategory.setCategory(category);
    filmCategory.setLastUpdate(Timestamp.from(Instant.now()));

    em.persist(filmCategory);
    em.flush();

    return filmCategory;
  }

  /**
   * Checks if a FilmCategory object already exists.
   *
   * @param film     Film object
   * @param category Category object
   * @return true if FilmCategory object exists, false if not
   */
  public boolean checkFilmCategory(Film film, Category category) {
    List<FilmCategory> filmCategoryList = em.createNamedQuery("FilmCategory.getFilmCategory",
            FilmCategory.class)
        .setParameter(1, film)
        .setParameter(2, category)
        .setMaxResults(1)
        .getResultList();

    return filmCategoryList.size() != 0;
  }
}