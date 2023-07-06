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
 * The Entity Store represents the store table in the database. Store is
 * responsible for the
 * processing of the data of the Store objects in the database.
 */
@Entity
@NamedQuery(name = "Store.getAll", query = "SELECT s FROM Store s")
public class Store implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "store_id")
  private Integer storeId;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @OneToMany(mappedBy = "store")
  @JsonbTransient
  private List<Inventory> inventories;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;

  @ManyToOne
  @JoinColumn(name = "manager_staff_id")
  private Staff staff;

  public Store() {
  }

  public Store(Staff staff, Address address, Timestamp lastUpdate) {
    this.staff = staff;
    this.address = address;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public Integer getStoreId() {
    return this.storeId;
  }

  public void setStoreId(Integer storeId) {
    this.storeId = storeId;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Address getAddress() {
    return this.address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Staff getStaff() {
    return this.staff;
  }

  public void setStaff(Staff staff) {
    this.staff = staff;
  }

  public List<Inventory> getInventories() {
    return this.inventories;
  }

  public void setInventories(List<Inventory> inventories) {
    this.inventories = inventories;
  }

  public Inventory addInventory(Inventory inventory) {
    getInventories().add(inventory);
    inventory.setStore(this);

    return inventory;
  }

  public Inventory removeInventory(Inventory inventory) {
    getInventories().remove(inventory);
    inventory.setStore(null);

    return inventory;
  }
}