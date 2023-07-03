package de.niklasfulle.dvdrentalcustomer.serviceses;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import de.niklasfulle.dvdrentalcustomer.entities.Address;
import de.niklasfulle.dvdrentalcustomer.entities.Customer;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * Service for Customer entity.
 */
@Stateless
public class CustomerService {

  @PersistenceContext
  EntityManager em;

  public Response createCustomer(JsonObject jsonCustomerObject, Address address, int storeId) {
    if (storeId > 0) {
      try {
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(
            "http://localhost:8080/dvd-rental-store/resources/stores/" + storeId);

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

    Date createDate;
    try {
      createDate = Date.valueOf(jsonCustomerObject.getString("createDate"));
    } catch (DateTimeParseException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("only allowed: Dateformat as 'YYYY-MM-DD'")
          .build();
    }

    try {
      Customer customer = new Customer(jsonCustomerObject.getInt("active"),
          jsonCustomerObject.getBoolean("activebool"), createDate,
          jsonCustomerObject.getString("email"), jsonCustomerObject.getString("firstName"),
          jsonCustomerObject.getString("lastName"), Timestamp.from(Instant.now()), storeId,
          address);

      em.persist(customer);
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

  public Customer getCustomer(int customerId) {
    return em.find(Customer.class, customerId);
  }

  public Response getCustomerCount() {
    return Response.ok().entity(em.createNamedQuery("Customer.getAll", Customer.class)
            .getResultList()
            .size())
        .build();
  }

  public Response getCustomerByID(int customerId) {
    Customer customer = getCustomer(customerId);
    if (customer == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Customer not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectCustomerBuilder(customer)).build();
  }

  public Response getPaymentsByCustomerId(int customerId) {
    Customer customer = getCustomer(customerId);
    if (customer == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Customer not found.")
          .build();
    }

    return Response.ok().entity(customer.getPayments()).build();
  }

  public Response getCustomersLimit(int limit, int offset) {
    List<Customer> customerList = em.createNamedQuery("Customer.getAll", Customer.class)
        .setFirstResult(offset)
        .setMaxResults(limit > 100 ? 100 : limit)
        .getResultList();

    List<JsonObject> response = new LinkedList<>();
    for (Customer customer : customerList) {
      response.add(jsonObjectCustomerBuilder(customer));
    }

    return Response.ok().entity(response).build();
  }

  /**
   * Builds a JsonObject from a Customer object.
   *
   * @param payment Customer object
   * @return JsonObject of Customer
   */
  public JsonObject jsonObjectCustomerBuilder(Customer customer) {
    return Json.createObjectBuilder()
        .add("active", customer.getActive())
        .add("activebool", customer.getActivebool())
        .add("address",
            Json.createObjectBuilder()
                .add("href", "/dvd-rental-customer/resources/address/" +
                    customer.getAddress().getAddressId())
                .build())
        .add("createDate", customer.getCreateDate().toString())
        .add("email", customer.getEmail())
        .add("firstName", customer.getFirstName())
        .add("id", customer.getCustomerId())
        .add("lastName", customer.getLastName())
        .add("store", Json.createObjectBuilder().add(
            "href", "/dvd-rental-stores/resources/stores/" +
                customer.getStoreId()))
        .build();
  }
}