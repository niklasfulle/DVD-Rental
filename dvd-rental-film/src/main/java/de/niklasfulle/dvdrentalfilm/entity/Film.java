package de.niklasfulle.dvdrentalfilm.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * The Entity Film represents the film table in the database. Film is responsible for the processing
 * of the data of the Film objects in the database.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Film.getAll", query = "SELECT f FROM Film f"),
    @NamedQuery(name = "Film.getLastFilm", query = "SELECT r FROM Film r ORDER BY filmId DESC"),
    @NamedQuery(name = "Film.getAllFetch", query = "SELECT f FROM Film f JOIN FETCH f.filmActors a"),
    @NamedQuery(name = "Film.getFilmByIdFetch", query = "select f from Film f JOIN FETCH f.filmActors a where f.filmId = ?1")
})
public class Film implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "film_id")
  private Integer filmId;

  private String description;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  private Short length;

  private String rating;

  @Column(name = "release_year")
  private Short releaseYear;

  @Column(name = "rental_duration")
  private Short rentalDuration;

  @Column(name = "rental_rate")
  private BigDecimal rentalRate;

  @Column(name = "replacement_cost")
  private BigDecimal replacementCost;

  private String title;

  @ManyToOne
  @JoinColumn(name = "language_id")
  private Language language;

  @OneToMany(mappedBy = "film", cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JsonbTransient
  private List<FilmActor> filmActors;

  @OneToMany(mappedBy = "film", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE, orphanRemoval = true)
  @JsonbTransient
  private List<FilmCategory> filmCategories;

  public Film() {
  }

  // Getter and Setter
  public Integer getFilmId() {
    return this.filmId;
  }

  public void setFilmId(Integer filmId) {
    this.filmId = filmId;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Short getLength() {
    return this.length;
  }

  public void setLength(Short length) {
    this.length = length;
  }

  public String getRating() {
    return this.rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public Short getReleaseYear() {
    return this.releaseYear;
  }

  public void setReleaseYear(Short releaseYear) {
    this.releaseYear = releaseYear;
  }

  public Short getRentalDuration() {
    return this.rentalDuration;
  }

  public void setRentalDuration(Short rentalDuration) {
    this.rentalDuration = rentalDuration;
  }

  public BigDecimal getRentalRate() {
    return this.rentalRate;
  }

  public void setRentalRate(BigDecimal rentalRate) {
    this.rentalRate = rentalRate;
  }

  public BigDecimal getReplacementCost() {
    return this.replacementCost;
  }

  public void setReplacementCost(BigDecimal replacementCost) {
    this.replacementCost = replacementCost;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Language getLanguage() {
    return this.language;
  }

  public void setLanguage(Language language) {
    this.language = language;
  }

  public List<FilmActor> getFilmActors() {
    return this.filmActors;
  }

  public void setFilmActors(List<FilmActor> filmActors) {
    this.filmActors = filmActors;
  }

  public FilmActor addFilmActor(FilmActor filmActor) {
    getFilmActors().add(filmActor);
    filmActor.setFilm(this);

    return filmActor;
  }

  public FilmActor removeFilmActor(FilmActor filmActor) {
    getFilmActors().remove(filmActor);
    filmActor.setFilm(null);

    return filmActor;
  }

  public List<FilmCategory> getFilmCategories() {
    return this.filmCategories;
  }

  public void setFilmCategories(List<FilmCategory> filmCategories) {
    this.filmCategories = filmCategories;
  }

  public FilmCategory addFilmCategory(FilmCategory filmCategory) {
    getFilmCategories().add(filmCategory);
    filmCategory.setFilm(this);

    return filmCategory;
  }

  public FilmCategory removeFilmCategory(FilmCategory filmCategory) {
    getFilmCategories().remove(filmCategory);
    filmCategory.setFilm(null);

    return filmCategory;
  }
}