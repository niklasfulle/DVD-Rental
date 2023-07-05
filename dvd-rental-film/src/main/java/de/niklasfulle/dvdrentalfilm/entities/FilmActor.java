package de.niklasfulle.dvdrentalfilm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The Entity FilmActor represents the filmactor table in the database. FilmActor is responsible for
 * the processing of the data of the FilmActor objects in the database.
 */
@Entity
@NamedQuery(name = "FilmActor.getFilmActor", query = "SELECT fc from FilmActor fc where film = ?1 and actor = ?2")
@Table(name = "film_actor")
public class FilmActor implements Serializable {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FilmActorPK id;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @ManyToOne
  @JoinColumn(name = "actor_id", insertable = false, updatable = false)
  private Actor actor;

  @ManyToOne
  @JoinColumn(name = "film_id", insertable = false, updatable = false)
  private Film film;

  public FilmActor() {
  }

  public FilmActor(FilmActorPK id, Timestamp lastUpdate) {
    this.id = id;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public FilmActorPK getId() {
    return this.id;
  }

  public void setId(FilmActorPK id) {
    this.id = id;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Actor getActor() {
    return this.actor;
  }

  public void setActor(Actor actor) {
    this.actor = actor;
  }

  public Film getFilm() {
    return this.film;
  }

  public void setFilm(Film film) {
    this.film = film;
  }
}