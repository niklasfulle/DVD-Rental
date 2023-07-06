package de.niklasfulle.dvdrentalfilm.entities;

import java.io.Serial;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * The Entity FilmActorPK represents the primary key of the filmactor table in the database.
 */
@Embeddable
public class FilmActorPK implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(name = "actor_id", insertable = false, updatable = false)
  private Integer actorId;

  @Column(name = "film_id", insertable = false, updatable = false)
  private Integer filmId;

  public FilmActorPK() {
  }

  public FilmActorPK(Integer actorId, Integer filmId) {
    this.actorId = actorId;
    this.filmId = filmId;
  }

  // Getter and Setter
  public Integer getActorId() {
    return this.actorId;
  }

  public void setActorId(Integer actorId) {
    this.actorId = actorId;
  }

  public Integer getFilmId() {
    return this.filmId;
  }

  public void setFilmId(Integer filmId) {
    this.filmId = filmId;
  }

  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof FilmActorPK castOther)) {
      return false;
    }
    return this.actorId.equals(castOther.actorId)
        && this.filmId.equals(castOther.filmId);
  }

  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.actorId.hashCode();
    hash = hash * prime + this.filmId.hashCode();

    return hash;
  }
}