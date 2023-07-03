package de.niklasfulle.dvdrentalcustomer.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * The Entity Country represents the country table in the database. Country is responsible for the
 * processing of the data of the Country objects in the database.
 */
@Entity
public class Country implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "country_id")
  private Integer countryId;

  private String country;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @OneToMany(mappedBy = "country")
  @JsonbTransient
  private List<City> cities;

  public Country(String country, Timestamp lastUpdate) {
    this.country = country;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public Integer getCountryId() {
    return this.countryId;
  }

  public void setCountryId(Integer countryId) {
    this.countryId = countryId;
  }

  public String getCountry() {
    return this.country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public List<City> getCities() {
    return this.cities;
  }

  public void setCities(List<City> cities) {
    this.cities = cities;
  }

  public City addCity(City city) {
    getCities().add(city);
    city.setCountry(this);

    return city;
  }

  public City removeCity(City city) {
    getCities().remove(city);
    city.setCountry(null);

    return city;
  }
}