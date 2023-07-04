package de.niklasfulle.dvdrentalcustomer.entities;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import java.sql.Timestamp;
import java.util.List;

/**
 * The Entity Address represents the address table in the database. Address is responsible for the
 * processing of the data of the Address objects in the database.
 */
// NamedQueries for the Address Entity
@NamedQueries({
    @NamedQuery(name = "Address.getAll", query = "SELECT a FROM Address a"),
    @NamedQuery(name = "Address.getLastAddressId", query = "SELECT a FROM Address a Order by addressId DESC")
})
@Entity
public class Address implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "address_id")
  private Integer addressId;

  private String address;

  private String address2;

  private String district;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

  private String phone;

  @Column(name = "postal_code")
  private String postalCode;

  @ManyToOne
  @JsonbTransient
  @JoinColumn(name = "city_id")
  private City city;

  @OneToMany(mappedBy = "address")
  @JsonbTransient
  private List<Customer> customers;

  public Address() {
  }

  public Address(String Address, String Address2, String District, City City, String Phone,
      String PostalCode, Timestamp LastUpdate) {
    this.address = Address;
    this.address2 = Address2;
    this.district = District;
    this.city = City;
    this.phone = Phone;
    this.postalCode = PostalCode;
    this.lastUpdate = LastUpdate;
  }

  // Getter and Setter
  public Integer getAddressId() {
    return this.addressId;
  }

  public void setAddressId(Integer addressId) {
    this.addressId = addressId;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddress2() {
    if (this.address2 == null) {
      return "";
    }
    return this.address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getDistrict() {
    return this.district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public Timestamp getLastUpdate() {
    return this.lastUpdate;
  }

  public void setLastUpdate(Timestamp lastUpdate) {
    this.lastUpdate = lastUpdate;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getPostalCode() {
    return this.postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public City getCity() {
    return this.city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public List<Customer> getCustomers() {
    return this.customers;
  }

  public void setCustomers(List<Customer> customers) {
    this.customers = customers;
  }
}