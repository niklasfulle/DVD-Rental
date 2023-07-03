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
import de.niklasfulle.dvdrentalcustomer.serviceses.CustomerService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;
import de.niklasfulle.dvdrentalcustomer.serviceses.PaymentService;

/**
 * Endpoint for payments.
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

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPaymentById(@PathParam("id") int paymentId) {
    return paymentService.getPaymentById(paymentId);
  }

  @DELETE
  @Path("/{id}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response deletePaymentsById(@PathParam("id") int paymentId) {
    return paymentService.deletePaymentsById(paymentId);
  }

  @GET
  @Path("/test/delete")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getTestPaymentDelete() {
    return paymentService.getLastPaymentDelete();
  }
}