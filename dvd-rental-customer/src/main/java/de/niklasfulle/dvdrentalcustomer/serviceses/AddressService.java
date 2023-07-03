package de.niklasfulle.dvdrentalcustomer.serviceses;

import java.sql.Timestamp;
import java.time.Instant;
import de.niklasfulle.dvdrentalcustomer.entities.Address;
import de.niklasfulle.dvdrentalcustomer.entities.City;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;

/**
 * Service for Address entity.
 */
@Stateless
public class AddressService {

  @PersistenceContext
  EntityManager em;

  public Response createAddress(JsonObject jsonAddressObject, City city) {
    try {
      Address address = new Address(
          jsonAddressObject.getString("address"),
          jsonAddressObject.getString("address2"),
          jsonAddressObject.getString("district"),
          city,
          jsonAddressObject.getString("phone"),
          jsonAddressObject.getString("postalCode"),
          Timestamp.from(Instant.now())
      );

      em.persist(address);
      em.flush();

      return Response.status(Response.Status.CREATED)
          .entity("Address created")
          .build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Couldn't add Rental to Database.")
          .build();
    }
  }

  public Address getAddress(int addressId) {
    return em.find(Address.class, addressId);
  }

  public Response getAllAddressesLimit(int limit, int offset) {
    return Response.ok(em.createNamedQuery("Address.getAll", Address.class)
            .setFirstResult(offset)
            .setMaxResults(limit > 100 ? 100 : limit)
            .getResultList())
        .build();
  }

  public Response getAddressByID(int addressId) {
    Address address = getAddress(addressId);
    if (address == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }

    return Response.ok().entity(jsonObjectAddressBuilder(address)).build();
  }

  public Response getLastAddressID() {
    int ret = em.createQuery("SELECT r FROM Address r ORDER BY addressId DESC", Address.class)
        .setMaxResults(1)
        .getSingleResult().getAddressId();
    return Response.ok().entity(Json.createObjectBuilder().add("id", ret)).build();
  }

  /**
   * Builds a JsonObject from a Address object.
   *
   * @param payment Address object
   * @return JsonObject of Address
   */
  public JsonObject jsonObjectAddressBuilder(Address address) {
    return Json.createObjectBuilder()
        .add("address", address.getAddress())
        .add("address2", address.getAddress2())
        .add("city", address.getCity().getCity())
        .add("country", address.getCity().getCountry().getCountry())
        .add("district", address.getDistrict())
        .add("id", address.getAddressId())
        .add("phone", address.getPhone())
        .add("postalCode", address.getPostalCode()).build();
  }
}