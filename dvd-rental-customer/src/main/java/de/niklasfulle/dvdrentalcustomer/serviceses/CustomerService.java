package de.niklasfulle.dvdrentalcustomer.serviceses;

import de.niklasfulle.dvdrentalcustomer.entities.Address;
import de.niklasfulle.dvdrentalcustomer.entities.Customer;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * Service for Customer entity.
 */
@Stateless
public class CustomerService {

  @PersistenceContext
  EntityManager em;

  /**
   * Creates a new Customer object and persists it to the database. Checks if the storeId is valid.
   *
   * @param jsonCustomerObject JsonObject containing the customer data
   * @param address            Address object
   * @param storeId            Store id
   * @return Response with status code and message
   */
  public Response createCustomer(JsonObject jsonCustomerObject, Address address, int storeId)
      throws ParseException {

    // TODO works with the store services
    
    /*if (storeId > 0) {
      try {
        Client client = ClientBuilder.newClient();

        WebTarget target = client.target(
            "http://store-wildfly:8080/dvd-rental-store/resources/stores/" + storeId);

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
    }*/

    Date createDate;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    try {
      java.util.Date parsedDate = format.parse(jsonCustomerObject.getString("createDate"));
      createDate = new Date(parsedDate.getTime());
      System.out.println("Converted SQL date: " + createDate);
    } catch (ParseException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("only allowed: Dateformat as 'YYYY-MM-DD'")
          .build();
    }

    try {
      Customer customer = new Customer(jsonCustomerObject.getInt("active"),
          jsonCustomerObject.getBoolean("activebool"), createDate,
          jsonCustomerObject.getString("email"), jsonCustomerObject.getString("firstName"),
          jsonCustomerObject.getString("lastName"), Timestamp.from(Instant.now()), 1,
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

  /**
   * Gets a Customer object from the database.
   *
   * @param customerId Customer id
   * @return Customer object
   */
  public Customer getCustomer(int customerId) {
    return em.find(Customer.class, customerId);
  }

  /**
   * Gets a Customer object from the database and returns it as a JSON object.
   *
   * @param customerId Customer id
   * @return Response with status code and message
   */
  public Response getCustomerById(int customerId) {
    Customer customer = em.find(Customer.class, customerId);
    if (customer == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Customer not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectCustomerBuilder(customer)).build();
  }

  /**
   * Returns the number of customers in the database.
   *
   * @return Response with status code and message
   */
  public Response getCustomerCount() {
    return Response.ok().entity(em.createNamedQuery("Customer.getAll", Customer.class)
            .getResultList()
            .size())
        .build();
  }

  /**
   * Gets all Payments of a Customer. Returns a list of JSON objects.
   *
   * @param customerId Customer id
   * @return Response with status code and message
   */
  public Response getPaymentsByCustomerId(int customerId) {
    Customer customer = em.find(Customer.class, customerId);
    if (customer == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Customer not found.")
          .build();
    }

    return Response.ok().entity(customer.getPayments()).build();
  }

  /**
   * Get all customers. The customers are returned as a JSON array. The limit and offset can be used
   * to limit the number of customers returned.
   *
   * @param limit  The maximum number of customers to return.
   * @param offset The number of customers to skip.
   * @return A response with the status code and a message.
   */
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
   * @param customer Customer object
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