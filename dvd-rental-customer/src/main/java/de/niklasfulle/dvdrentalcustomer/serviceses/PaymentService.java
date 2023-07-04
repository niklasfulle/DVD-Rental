package de.niklasfulle.dvdrentalcustomer.serviceses;

import de.niklasfulle.dvdrentalcustomer.entities.Customer;
import de.niklasfulle.dvdrentalcustomer.entities.Payment;
import jakarta.ejb.Stateless;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

/**
 * Service for Payment entity.
 */
@Stateless
public class PaymentService {

  @PersistenceContext
  EntityManager em;

  /**
   * Creates a new Payment object and persists it to the database.
   *
   * @param jsonPaymentObject JsonObject containing the payment data
   * @param customer          Customer object
   * @return Response with status code and message
   */
  public Response createPayment(JsonObject jsonPaymentObject, Customer customer) {
    try {
      Payment payment = new Payment(new BigDecimal(jsonPaymentObject.getString("amount")),
          Timestamp.from(Instant.now()),
          Integer.valueOf(jsonPaymentObject.getInt("rental")),
          Integer.valueOf(jsonPaymentObject.getInt("staff")),
          customer);

      em.persist(payment);
      em.flush();

      return Response.status(Response.Status.CREATED)
          .entity("Payment created")
          .build();
    } catch (Exception e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Couldn't add Payment to Database.")
          .build();
    }
  }

  /**
   * Gets a Payment object from the database and returns it as a JSON object.
   *
   * @param paymentId Payment id
   * @return Response with status code and message
   */
  public Response getPaymentById(int paymentId) {
    Payment payment = em.find(Payment.class, paymentId);
    if (payment == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Payment not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectPaymentBuilder(payment)).build();
  }

  /**
   * Deletes a Payment object from the database.
   *
   * @param paymentId Payment id
   * @return Response with status code and message
   */
  public Response deletePaymentsById(int paymentId) {
    Payment payment = em.find(Payment.class, paymentId);
    if (payment == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Payment not found.")
          .build();
    }

    em.remove(payment);
    em.flush();

    return Response.status(Response.Status.NO_CONTENT).build();
  }


  public Response getLastPaymentDelete() {
    return Response.ok().entity(em.createNamedQuery("Payment.getLastPayment", Payment.class)
            .setMaxResults(1)
            .getSingleResult()
            .getPaymentId())
        .build();
  }

  /**
   * Builds a JsonObject from a Payment object.
   *
   * @param payment Payment object
   * @return JsonObject of Payment
   */
  public JsonObject jsonObjectPaymentBuilder(Payment payment) {
    return Json.createObjectBuilder()
        .add("active", payment.getAmount())
        .add("customer",
            Json.createObjectBuilder()
                .add("href", "/dvd-rental-customer/resources/customers/" +
                    payment.getCustomer().getCustomerId())
                .build())
        .add("id", payment.getPaymentId())
        .add("staff", Json.createObjectBuilder().add(
            "href", "/dvd-rental-store/resources/staff/" +
                payment.getStaffId()))
        .build();
  }
}