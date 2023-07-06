package de.niklasfulle.dvdrentalstore.serviceses;

import de.niklasfulle.dvdrentalstore.entities.Address;
import de.niklasfulle.dvdrentalstore.entities.City;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Service for Address entity.
 */
@Stateless
public class AddressService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Creates a new Address object and persists it to the database.
   *
   * @param jsonAddressObject JsonObject containing the address data
   * @param city              City object
   * @return Response with status code and message
   */
  public Response createAddress(JsonObject jsonAddressObject, City city) {
    try {
      Address address = new Address(
          jsonAddressObject.getString("address"),
          jsonAddressObject.getString("address2"),
          jsonAddressObject.getString("district"),
          city,
          jsonAddressObject.getString("phone"),
          jsonAddressObject.getString("postalCode"),
          Timestamp.from(Instant.now()));

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

  /**
   * Gets an Address object from the database.
   *
   * @param addressId Address id
   * @return Address object
   */
  public Address getAddress(int addressId) {
    return em.find(Address.class, addressId);
  }

  /**
   * Gets an Address object from the database and returns it as a JSON object.
   *
   * @param addressId Address id
   * @return Response with status code and message
   */
  public Response getAddressById(int addressId) {

    Address address = em.find(Address.class, addressId);
    if (address == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok().entity(jsonObjectAddressBuilder(address)).build();
  }

  /**
   * Gets the last address id from the database.
   *
   * @return Response with status code and message
   */
  public Response getLastAddressId() {
    Address address = em.createNamedQuery("Address.getLastAddressId", Address.class)
        .setMaxResults(1)
        .getSingleResult();
    if (address == null) {
      return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok().entity(jsonObjectAddressBuilder(address)).build();
  }

  /**
   * Get all addresses. The addresses are returned as a JSON array. The limit and
   * offset can be used
   * to limit the number of addresses returned.
   *
   * @param limit  The maximum number of addresses to return.
   * @param offset The number of addresses to skip.
   * @return A response with the status code and a message.
   */
  public Response getAllAddressesLimit(int limit, int offset) {
    return Response.ok(em.createNamedQuery("Address.getAll", Address.class)
        .setFirstResult(offset)
        .setMaxResults(limit > 100 ? 100 : limit)
        .getResultList())
        .build();
  }

  /**
   * Builds a JsonObject from an Address object.
   *
   * @param address Address object
   * @return JsonObject of Address
   */
  public JsonObject jsonObjectAddressBuilder(Address address) {
    return Json.createObjectBuilder()
        .add("address", address.getAddress())
        .add("city", address.getCity().getCity())
        .add("country", address.getCity().getCountry().getCountry())
        .add("district", address.getDistrict())
        .add("id", address.getAddressId())
        .add("phone", address.getPhone())
        .add("postalCode", address.getPostalCode()).build();
  }
}