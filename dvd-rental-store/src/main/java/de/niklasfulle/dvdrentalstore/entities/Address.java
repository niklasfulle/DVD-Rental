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
 * The Entity Address represents the address table in the database. Address is responsible for the
 * processing of the data of the Address objects in the database.
 */
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

  @Column(name = "postal_code")
  private String postalCode;

  private String phone;

  @ManyToOne
  @JsonbTransient
  @JoinColumn(name = "city_id")
  private City city;

  @OneToMany(mappedBy = "address")
  @JsonbTransient
  private List<Staff> staffs;

  @OneToMany(mappedBy = "address")
  @JsonbTransient
  private List<Store> stores;

  @Column(name = "last_update")
  private Timestamp lastUpdate;

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

  public List<Staff> getStaffs() {
    return this.staffs;
  }

  public void setStaffs(List<Staff> staffs) {
    this.staffs = staffs;
  }

  public Staff addStaff(Staff staff) {
    getStaffs().add(staff);
    staff.setAddress(this);

    return staff;
  }

  public Staff removeStaff(Staff staff) {
    getStaffs().remove(staff);
    staff.setAddress(null);

    return staff;
  }

  public List<Store> getStores() {
    return this.stores;
  }

  public void setStores(List<Store> stores) {
    this.stores = stores;
  }

  public Store addStore(Store store) {
    getStores().add(store);
    store.setAddress(this);

    return store;
  }

  public Store removeStore(Store store) {
    getStores().remove(store);
    store.setAddress(null);

    return store;
  }
}