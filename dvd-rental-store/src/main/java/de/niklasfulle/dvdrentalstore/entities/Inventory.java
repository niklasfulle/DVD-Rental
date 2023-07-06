package de.niklasfulle.dvdrentalstore.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * The Entity Inventory represents the inventory table in the database. Inventory is responsible for
 * the processing of the data of the Inventory objects in the database.
 */
@Entity
@NamedQuery(name = "Inventory.getAllByFilmId", query = "SELECT i from Inventory i WHERE film_id = ?1")
public class Inventory implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "inventory_id")
  private Integer inventoryId;

  @Column(name = "film_id")
  private Integer filmId;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  @OneToMany(mappedBy = "inventory")
  @JsonbTransient
  private List<Rental> rentals;

  public Inventory() {
  }

  // Getter and Setter
  public Integer getInventoryId() {
    return this.inventoryId;
  }

  public void setInventoryId(Integer inventoryId) {
    this.inventoryId = inventoryId;
  }

  public Integer getFilmId() {
    return this.filmId;
  }

  public void setFilmId(Integer filmId) {
    this.filmId = filmId;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Store getStore() {
    return this.store;
  }

  public void setStore(Store store) {
    this.store = store;
  }

  public List<Rental> getRentals() {
    return this.rentals;
  }

  public void setRentals(List<Rental> rentals) {
    this.rentals = rentals;
  }

  public Rental addRental(Rental rental) {
    getRentals().add(rental);
    rental.setInventory(this);

    return rental;
  }

  public Rental removeRental(Rental rental) {
    getRentals().remove(rental);
    rental.setInventory(null);

    return rental;
  }
}