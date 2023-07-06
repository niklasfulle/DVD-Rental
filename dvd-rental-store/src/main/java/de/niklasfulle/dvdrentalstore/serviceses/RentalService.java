package de.niklasfulle.dvdrentalstore.serviceses;

import de.niklasfulle.dvdrentalstore.entities.Inventory;
import de.niklasfulle.dvdrentalstore.entities.Rental;
import de.niklasfulle.dvdrentalstore.entities.Staff;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Service for Rental entity.
 */
public class RentalService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Services
  @Inject
  StaffService staffService;

  @Inject
  InventoryService inventoryService;

  // Methods

  /**
   * Creates a new Rental object and persists it to the database.
   *
   * @param jsonRentalObject JsonObject containing the rental data
   * @return Response with status code and message
   */
  public Response createRental(JsonObject jsonRentalObject) {
    Inventory inventory = inventoryService.getInventory(jsonRentalObject.getInt("inventory"));
    Staff staff = staffService.getStaff(jsonRentalObject.getInt("staff"));
    if (inventory == null || staff == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Some involved entity does "
              + "not exist. See message body")
          .build();
    }

    int customerId = jsonRentalObject.getInt("customer");

    // Check if customer exists
    if (customerId > 0) {
      try {
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(
            "http://localhost:8080/dvd-rental-customer/resources/customers/"
                + customerId);

        int response = target.request().get().getStatus();
        client.close();
        if (response != 200) {
          return Response.status(Response.Status.NOT_FOUND)
              .entity("Some involved entity does "
                  + "not exist. See message body")
              .build();
        }
      } catch (ProcessingException e) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
            .entity("SERVICE_UNAVAILABLE")
            .build();
      }
    }

    LocalDateTime rentalDate;
    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      rentalDate = LocalDateTime.parse(jsonRentalObject.getString("date"), formatter);
    } catch (DateTimeParseException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(
              "only allowed: customer (int), inventory (int), staff (int), date (yyyy-MM-dd HH:mm)")
          .build();
    }

    if (getRantalByCustomerInventoryDate(customerId, inventory)) {
      return Response.status(Response.Status.NOT_ACCEPTABLE)
          .entity("could not execute statement, key already exists").build();
    }

    try {
      Timestamp returnDate = null;
      Rental rental = new Rental(customerId, Timestamp.valueOf(rentalDate), returnDate, inventory, staff,
          Timestamp.from(Instant.now()));

      em.persist(rental);
      em.flush();

      return Response.status(Response.Status.CREATED)
          .entity("Rental created")
          .build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Couldn't add Rental to Database.")
          .build();
    }
  }

  /**
   * The method returns a Rental
   * 
   * @param rentalId Rental id
   * @return Response with status code and message
   */
  public Response putRentalsByIdReturned(int rentalId) {
    Rental rental = em.find(Rental.class, rentalId);
    if (rental != null) {
      if (rental.getReturnDate() == null) {
        rental.setReturnDate(Timestamp.from(Instant.now()));
        return Response.ok().entity("Rental is terminated.").build();
      }
      return Response.status(422)
          .entity("Rental already terminated.")
          .build();
    }
    return Response.status(Response.Status.NOT_FOUND)
        .entity("Rental not found.")
        .build();
  }

  /**
   * The method returns a Rental object as JsonObject.
   * 
   * @param rentalId Rental id
   * @return Response with status code and message
   */
  public Response getRentalsById(int rentalId) {
    Rental rental = em.find(Rental.class, rentalId);
    if (rental == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Rental not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectRentalBuilder(rental)).build();
  }

  /**
   * The method returns a Rental object by customer id and inventory id.
   * 
   * @param customerId Customer id
   * @param inventory  Inventory id
   * @return Response with status code and message
   */
  public boolean getRantalByCustomerInventoryDate(int customerId, Inventory inventory) {
    return em.createNamedQuery("Rental.getRentalByCustomerAndInventory", Rental.class)
        .setParameter(1, customerId)
        .setParameter(2, inventory)
        .setMaxResults(1)
        .getResultList().size() != 0;
  }

  /**
   * Returns the last rental with no return date.
   * 
   * @return Response with status code and message
   */
  public Response getLastRentalNoReturnDate() {
    return Response.ok().entity(em.createNamedQuery("Rental.getLastRental", Rental.class)
        .setMaxResults(1)
        .getSingleResult()
        .getRentalId())
        .build();
  }

  /**
   * Builds a JsonObject from an Rental object.
   *
   * @param rental Rental object
   * @return JsonObject of Rental
   */
  public JsonObject jsonObjectRentalBuilder(Rental rental) {
    return Json.createObjectBuilder().add(
        "customer",
        Json.createObjectBuilder()
            .add("href", "/dvd-rental-customer/resources/customers/" +
                rental.getCustomerId())
            .build())
        .add("film",
            Json.createObjectBuilder()
                .add("href", "/dvd-rental-film/resources/films/" +
                    rental.getInventory().getFilmId())
                .build())
        .add("rentalDate", rental.getRentalDateString())
        .add("rentalId", rental.getRentalId())
        .add("returnDate", rental.getReturnDateString())
        .add("store",
            Json.createObjectBuilder()
                .add("href", "/dvd-rental-store/resources/stores/" +
                    rental.getStaff().getStoreId())
                .build())
        .build();
  }
}