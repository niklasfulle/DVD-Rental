package de.niklasfulle.dvdrentalcustomer.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.io.InputStream;
import jakarta.ws.rs.DELETE;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.json.JsonObject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import de.niklasfulle.dvdrentalcustomer.entities.Customer;
import de.niklasfulle.dvdrentalcustomer.serviceses.PaymentService;
import de.niklasfulle.dvdrentalcustomer.serviceses.CustomerService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;

/**
 * Endpoint for payments. The payments are stored in the database.
 */
@Path("/payments")
public class PaymentsEndpoint {

  // Services
  @Inject
  PaymentService paymentService;

  @Inject
  CustomerService customerService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints

  /**
   * Creates a new payment. The payment is stored in the database. The customer must exist.
   *
   * @param paymentStream The payment data as a JSON object.
   * @return A response with the status code and a message.
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createPayment(InputStream paymentStream) {
    JsonObject jsonPaymentObject = jsonBuilderService.createJsonObjectFromStream(paymentStream);
    if (jsonPaymentObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Bad payment data.")
          .build();
    }

    Customer customer = customerService.getCustomer(jsonPaymentObject.getInt("customer"));
    if (customer == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Some involved entity does "
              + "not exist. See message body")
          .build();
    }

    return paymentService.createPayment(jsonPaymentObject, customer);
  }

  /**
   * Delete a payment by its Id. The payment is deleted from the database.
   *
   * @param paymentId The Id of the payment.
   * @return A response with the status code and a message.
   */
  @DELETE
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response deletePaymentsById(@PathParam("id") int paymentId) {
    return paymentService.deletePaymentsById(paymentId);
  }

  /**
   * Get a payment by its Id. The payment is returned as a JSON object.
   *
   * @param paymentId The Id of the payment.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPaymentById(@PathParam("id") int paymentId) {
    return paymentService.getPaymentById(paymentId);
  }

  @GET
  @Path("/test/delete")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getTestPaymentDelete() {
    return paymentService.getLastPaymentDelete();
  }
}