package de.niklasfulle.dvdrentalstore.entities;

import java.io.Serial;
import java.sql.Timestamp;
import java.io.Serializable;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * The Entity Rental represents the rental table in the database. Rental is responsible for the
 * processing of the data of the Rental objects in the database.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Rental.getLastRental", query = "SELECT r FROM Rental r ORDER BY returnDate DESC"),
    @NamedQuery(name = "Rental.getRentalByCustomerAndInventory", query = "SELECT r FROM Rental r where customerId = ?1 and inventory = ?2")
})
public class Rental implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "rental_id")
  private Integer rentalId;

  @Column(name = "customer_id")
  private Integer customerId;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @Column(name = "rental_date")
  private Timestamp rentalDate;

  @Column(name = "return_date", nullable = true)
  private Timestamp returnDate;

  @ManyToOne
  @JoinColumn(name = "inventory_id")
  private Inventory inventory;

  @ManyToOne
  @JoinColumn(name = "staff_id")
  private Staff staff;

  public Rental() {
  }

  public Rental(Integer customerId, Timestamp rentalDate, Timestamp returnDate, Inventory inventory,
      Staff staff,
      Timestamp lastUpdate) {
    this.customerId = customerId;
    this.rentalDate = rentalDate;
    this.returnDate = returnDate;
    this.inventory = inventory;
    this.staff = staff;
    this.lastUpdate = lastUpdate;
  }

  // Getter and Setter
  public Integer getRentalId() {
    return this.rentalId;
  }

  public void setRentalId(Integer rentalId) {
    this.rentalId = rentalId;
  }

  public Integer getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public Timestamp getRentalDate() {
    return this.rentalDate;
  }

  public void setRentalDate(Timestamp rentalDate) {
    this.rentalDate = rentalDate;
  }

  public String getRentalDateString() {
    return this.rentalDate.toString();
  }

  public Timestamp getReturnDate() {
    return this.returnDate;
  }

  public void setReturnDate(Timestamp returnDate) {
    this.returnDate = returnDate;
  }

  public String getReturnDateString() {
    if (this.returnDate == null) {
      return "";
    } else {
      return this.returnDate.toString();
    }
  }

  public Inventory getInventory() {
    return this.inventory;
  }

  public void setInventory(Inventory inventory) {
    this.inventory = inventory;
  }

  public Staff getStaff() {
    return this.staff;
  }

  public void setStaff(Staff staff) {
    this.staff = staff;
  }
}