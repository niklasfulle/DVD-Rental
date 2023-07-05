package de.niklasfulle.dvdrentalfilm.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * The Entity FilmCategory represents the filmcategory table in the database. FilmCategory is
 * responsible for the processing of the data of the FilmCategory objects in the database.
 */
@Entity
@NamedQuery(name = "FilmCategory.getFilmCategory", query = "SELECT fc from FilmCategory fc where film = ?1 and category = ?2")
@Table(name = "film_category")
public class FilmCategory implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private FilmCategoryPK id;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @ManyToOne
  @JoinColumn(name = "category_id", insertable = false, updatable = false)
  private Category category;

  @ManyToOne
  @JoinColumn(name = "film_id", insertable = false, updatable = false)
  private Film film;

  public FilmCategory() {
  }

  public FilmCategory(FilmCategoryPK id, Timestamp lastUpdate) {
    this.id = id;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public FilmCategoryPK getId() {
    return this.id;
  }

  public void setId(FilmCategoryPK id) {
    this.id = id;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Category getCategory() {
    return this.category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Film getFilm() {
    return this.film;
  }

  public void setFilm(Film film) {
    this.film = film;
  }
}