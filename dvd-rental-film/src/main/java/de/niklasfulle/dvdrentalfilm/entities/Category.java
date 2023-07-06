package de.niklasfulle.dvdrentalfilm.entities;

import java.io.Serial;
import java.util.List;
import java.sql.Timestamp;
import java.io.Serializable;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.GenerationType;
import jakarta.persistence.GeneratedValue;
import jakarta.json.bind.annotation.JsonbTransient;
/**
 * The Entity Category represents the category table in the database. Category is responsible for
 * the processing of the data of the Category objects in the database.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Category.getAll", query = "SELECT s from Category s"),
    @NamedQuery(name = "Category.getCategoryByName", query = "SELECT c from Category c WHERE name = ?1")
})
public class Category implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "category_id")
  private Integer categoryId;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  private String name;

  @OneToMany(mappedBy = "category")
  @JsonbTransient
  private List<FilmCategory> filmCategories;

  public Category() {
  }

  public Category(String name, Timestamp lastUpdate) {
    this.name = name;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public Integer getCategoryId() {
    return this.categoryId;
  }

  public void setCategoryId(Integer categoryId) {
    this.categoryId = categoryId;
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

  public List<FilmCategory> getFilmCategories() {
    return this.filmCategories;
  }

  public void setFilmCategories(List<FilmCategory> filmCategories) {
    this.filmCategories = filmCategories;
  }

  public FilmCategory addFilmCategory(FilmCategory filmCategory) {
    getFilmCategories().add(filmCategory);
    filmCategory.setCategory(this);

    return filmCategory;
  }

  public FilmCategory removeFilmCategory(FilmCategory filmCategory) {
    getFilmCategories().remove(filmCategory);
    filmCategory.setCategory(null);

    return filmCategory;
  }
}