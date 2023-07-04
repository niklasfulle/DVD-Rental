package de.niklasfulle.dvdrentalcustomer.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * The Entity Payment represents the payment table in the database. Payment is responsible for the
 * processing of the data of the Payment objects in the database.
 */
// NamedQueries for the Payment Entity
@NamedQueries({
    @NamedQuery(name = "Payment.getLastPayment", query = "SELECT r FROM Payment r ORDER BY paymentId DESC")
})
@Entity
public class Payment implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "payment_id")
  private Integer paymentId;

  private BigDecimal amount;

  @Column(name = "payment_date")
  private Timestamp paymentDate;

  @Column(name = "rental_id")
  private Integer rentalId;

  @Column(name = "staff_id")
  private Integer staffId;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Customer customer;

  public Payment() {
  }

  public Payment(BigDecimal amount, Timestamp paymentDate, Integer rentalId, Integer staffId,
      Customer customer) {
    this.amount = amount;
    this.paymentDate = paymentDate;
    this.rentalId = rentalId;
    this.staffId = staffId;
    this.customer = customer;
  }

  // Getter and Setter
  public Integer getPaymentId() {
    return this.paymentId;
  }

  public void setPaymentId(Integer paymentId) {
    this.paymentId = paymentId;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Timestamp getPaymentDate() {
    return this.paymentDate;
  }

  public void setPaymentDate(Timestamp paymentDate) {
    this.paymentDate = paymentDate;
  }

  public Integer getRentalId() {
    return this.rentalId;
  }

  public void setRentalId(Integer rentalId) {
    this.rentalId = rentalId;
  }

  public Integer getStaffId() {
    return this.staffId;
  }

  public void setStaffId(Integer staffId) {
    this.staffId = staffId;
  }

  public Customer getCustomer() {
    return this.customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }
}