package de.niklasfulle.dvdrentalfilm.serviceses;

import de.niklasfulle.dvdrentalfilm.entities.Actor;
import de.niklasfulle.dvdrentalfilm.entities.Film;
import de.niklasfulle.dvdrentalfilm.entities.FilmActor;
import de.niklasfulle.dvdrentalfilm.entities.FilmActorPK;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

/**
 * Service for FilmActor entity.
 */
@Stateless
public class FilmActorService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Creates a new FilmActor object and persists it to the database.
   *
   * @param film  Film object
   * @param actor Actor object
   * @return FilmActor object
   */
  public FilmActor createFilmActor(Film film, Actor actor) {
    if (checkFilmActor(film, actor)) {
      return null;
    }

    FilmActorPK filmActorPK = new FilmActorPK();
    filmActorPK.setFilmId(film.getFilmId());
    filmActorPK.setActorId(actor.getActorId());

    FilmActor filmActor = new FilmActor();
    filmActor.setId(filmActorPK);
    filmActor.setFilm(film);
    filmActor.setActor(actor);
    filmActor.setLastUpdate(Timestamp.from(Instant.now()));

    em.persist(filmActor);
    em.flush();

    return filmActor;
  }

  /**
   * Checks if a FilmActor object already exists.
   *
   * @param film  Film object
   * @param actor Actor object
   * @return true if FilmActor object exists, false if not
   */
  public boolean checkFilmActor(Film film, Actor actor) {
    List<FilmActor> filmActorList = em.createNamedQuery("FilmActor.getFilmActor", FilmActor.class)
        .setParameter(1, film)
        .setParameter(2, actor)
        .setMaxResults(1)
        .getResultList();

    return filmActorList.size() != 0;
  }
}
