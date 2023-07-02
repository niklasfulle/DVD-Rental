package de.niklasfulle.dvdrentalstore.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * The Entity Staff represents the staff table in the database. Staff is responsible for the
 * processing of the data of the Staff objects in the database.
 */
@Entity
public class Staff implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "staff_id")
  private Integer staffId;

  private Boolean active;

  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  private String password;

  private byte[] picture;

  @Column(name = "store_id")
  private Integer storeId;

  private String username;

  @OneToMany(mappedBy = "staff")
  @JsonbTransient
  private List<Rental> rentals;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;

  @OneToMany(mappedBy = "staff")
  @JsonbTransient
  private List<Store> stores;

  public Staff() {
  }

  // Getter and Setter
  public Integer getStaffId() {
    return this.staffId;
  }

  public void setStaffId(Integer staffId) {
    this.staffId = staffId;
  }

  public Boolean getActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public byte[] getPicture() {
    return this.picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public String getPictureString() {
    return this.picture.toString();
  }

  public Integer getStoreId() {
    return this.storeId;
  }

  public void setStoreId(Integer storeId) {
    this.storeId = storeId;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Address getAddress() {
    return this.address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<Rental> getRentals() {
    return this.rentals;
  }

  public void setRentals(List<Rental> rentals) {
    this.rentals = rentals;
  }

  public Rental addRental(Rental rental) {
    getRentals().add(rental);
    rental.setStaff(this);

    return rental;
  }

  public Rental removeRental(Rental rental) {
    getRentals().remove(rental);
    rental.setStaff(null);

    return rental;
  }

  public List<Store> getStores() {
    return this.stores;
  }

  public void setStores(List<Store> stores) {
    this.stores = stores;
  }

  public Store addStore(Store store) {
    getStores().add(store);
    store.setStaff(this);

    return store;
  }

  public Store removeStore(Store store) {
    getStores().remove(store);
    store.setStaff(null);

    return store;
  }
}