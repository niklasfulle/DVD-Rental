package de.niklasfulle.dvdrentalstore.entities;

import java.io.Serial;
import java.util.List;
import java.sql.Timestamp;
import java.io.Serializable;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.json.bind.annotation.JsonbTransient;
/**
 * The Entity City represents the city table in the database. City is responsible for the processing
 * of the data of the City objects in the database.
 */
@NamedQueries({
    @NamedQuery(name = "City.getAll", query = "SELECT c FROM City c"),
    @NamedQuery(name = "City.getCityByName", query = "SELECT c from City c WHERE city = ?1")
})
@Entity
public class City implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "city_id")
  private Integer cityId;

  private String city;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @OneToMany(mappedBy = "city")
  @JsonbTransient
  private List<Address> addresses;

  @ManyToOne
  @JoinColumn(name = "country_id")
  private Country country;

  public City() {
  }

  public City(String city, Country country, Timestamp lastUpdate) {
    this.city = city;
    this.country = country;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public Integer getCityId() {
    return this.cityId;
  }

  public void setCityId(Integer cityId) {
    this.cityId = cityId;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Country getCountry() {
    return this.country;
  }

  public void setCountry(Country country) {
    this.country = country;
  }

  public List<Address> getAddresses() {
    return this.addresses;
  }

  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }

  public Address addAddress(Address address) {
    getAddresses().add(address);
    address.setCity(this);

    return address;
  }

  public Address removeAddress(Address address) {
    getAddresses().remove(address);
    address.setCity(null);

    return address;
  }
}