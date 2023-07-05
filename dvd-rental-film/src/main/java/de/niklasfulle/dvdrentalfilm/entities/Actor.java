package de.niklasfulle.dvdrentalfilm.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * The Entity Actor represents the actor table in the database. Actor is responsible for the
 * processing of the data of the Actor objects in the database.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Actor.getAll", query = "SELECT a FROM Actor a"),
    @NamedQuery(name = "Actor.getLastActor", query = "SELECT a FROM Actor a ORDER BY actorId DESC")
})
public class Actor implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "actor_id")
  private Integer actorId;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @OneToMany(mappedBy = "actor", fetch = FetchType.EAGER, orphanRemoval = true)
  @JsonbTransient
  private List<FilmActor> filmActors;

  public Actor() {
  }

  public Actor(String firstName, String lastName, Timestamp LastUpdate) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.lastUpdate = LastUpdate;
  }

  // Getter and Setter
  public Integer getActorId() {
    return this.actorId;
  }

  public void setActorId(Integer actorId) {
    this.actorId = actorId;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public List<FilmActor> getFilmActors() {
    return this.filmActors;
  }

  public void setFilmActors(List<FilmActor> filmActors) {
    this.filmActors = filmActors;
  }

  public FilmActor addFilmActor(FilmActor filmActor) {
    getFilmActors().add(filmActor);
    filmActor.setActor(this);

    return filmActor;
  }

  public FilmActor removeFilmActor(FilmActor filmActor) {
    getFilmActors().remove(filmActor);
    filmActor.setActor(null);

    return filmActor;
  }
}