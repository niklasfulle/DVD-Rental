package de.niklasfulle.dvdrentalfilm.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * The Entity Language represents the language table in the database. Language is responsible for
 * the processing of the data of the Language objects in the database.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Language.getAll", query = "SELECT s from Language s"),
    @NamedQuery(name = "Language.getLanguageByName", query = "SELECT l from Language l WHERE name = ?1")
})
public class Language implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "language_id")
  private Integer languageId;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @Column(columnDefinition = "bpchar")
  private String name;

  @OneToMany(mappedBy = "language")
  @JsonbTransient
  private List<Film> films;

  public Language() {
  }

  public Language(String name, Timestamp lastUpdate) {
    this.name = name;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public Integer getLanguageId() {
    return this.languageId;
  }

  public void setLanguageId(Integer languageId) {
    this.languageId = languageId;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Film> getFilms() {
    return this.films;
  }

  public void setFilms(List<Film> films) {
    this.films = films;
  }

  public Film addFilm(Film film) {
    getFilms().add(film);
    film.setLanguage(this);

    return film;
  }

  public Film removeFilm(Film film) {
    getFilms().remove(film);
    film.setLanguage(null);

    return film;
  }
}