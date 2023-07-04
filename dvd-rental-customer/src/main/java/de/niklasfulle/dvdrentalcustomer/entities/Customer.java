package de.niklasfulle.dvdrentalcustomer.entities;

import jakarta.json.bind.annotation.JsonbTransient;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * The Entity Customer represents the customer table in the database. Customer is responsible for
 * the processing of the data of the Customer objects in the database.
 */
// NamedQueries for the Customer Entity
@NamedQueries({
    @NamedQuery(name = "Customer.getAll", query = "SELECT c FROM Customer c")
})
@Entity
public class Customer implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "customer_id")
  private Integer customerId;

  private Integer active;

  private Boolean activebool;

  @Temporal(TemporalType.DATE)
  @Column(name = "create_date")
  private Date createDate;

  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  @Column(name = "store_id")
  private Integer storeId;

  @ManyToOne
  @JoinColumn(name = "address_id")
  private Address address;

  @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
  @JsonbTransient
  private List<Payment> payments;

  public Customer(Integer active, Boolean activebool, Date createDate, String email,
      String firstName, String lastName, Timestamp lastUpdate, Integer storeId, Address address) {
    this.active = active;
    this.activebool = activebool;
    this.createDate = createDate;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.lastUpdate = lastUpdate;
    this.storeId = storeId;
    this.address = address;
  }

  // Getter and Setter
  public Integer getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(Integer customerId) {
    this.customerId = customerId;
  }

  public Integer getActive() {
    return this.active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public Boolean getActivebool() {
    return this.activebool;
  }

  public void setActivebool(Boolean activebool) {
    this.activebool = activebool;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
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

  public Integer getStoreId() {
    return this.storeId;
  }

  public void setStoreId(Integer storeId) {
    this.storeId = storeId;
  }

  public Address getAddress() {
    return this.address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public List<Payment> getPayments() {
    return this.payments;
  }

  public void setPayments(List<Payment> payments) {
    this.payments = payments;
  }

  public Payment addPayment(Payment payment) {
    getPayments().add(payment);
    payment.setCustomer(this);

    return payment;
  }

  public Payment removePayment(Payment payment) {
    getPayments().remove(payment);
    payment.setCustomer(null);

    return payment;
  }
}