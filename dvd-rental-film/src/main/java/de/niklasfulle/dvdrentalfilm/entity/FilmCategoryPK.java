package de.niklasfulle.dvdrentalfilm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

/**
 * The Entity FilmCategoryPK represents the primary key of the filmcategory table in the database.
 */
@Embeddable
public class FilmCategoryPK implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(name = "film_id", insertable = false, updatable = false)
  private Integer filmId;

  @Column(name = "category_id", insertable = false, updatable = false)
  private Integer categoryId;

  public FilmCategoryPK() {
  }

  // Getter and Setter
  public Integer getFilmId() {
    return this.filmId;
  }

  public void setFilmId(Integer filmId) {
    this.filmId = filmId;
  }

  public Integer getCategoryId() {
    return this.categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
  }

  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof FilmCategoryPK castOther)) {
      return false;
    }
    return this.filmId.equals(castOther.filmId)
        && this.categoryId.equals(castOther.categoryId);
  }

  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.filmId.hashCode();
    hash = hash * prime + this.categoryId.hashCode();

    return hash;
  }
}