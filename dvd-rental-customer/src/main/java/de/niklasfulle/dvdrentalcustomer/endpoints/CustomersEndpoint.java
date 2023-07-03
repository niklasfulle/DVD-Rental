package de.niklasfulle.dvdrentalcustomer.endpoints;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.io.InputStream;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.json.JsonObject;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import de.niklasfulle.dvdrentalcustomer.entities.Address;
import de.niklasfulle.dvdrentalcustomer.serviceses.AddressService;
import de.niklasfulle.dvdrentalcustomer.serviceses.CustomerService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;

/**
 * Endpoint for customers.
 */
@Path("/customers")
public class CustomersEndpoint {

  // Services
  @Inject
  CustomerService customerService;

  @Inject
  AddressService addressService;

  @Inject
  JsonBuilderService jsonBuilderService;

  // Endpoints
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.TEXT_PLAIN)
  public Response createCustomer(@QueryParam("address") Integer addressId,
      @QueryParam("store") Integer storeId,
      InputStream customerStream) {
    JsonObject jsonCustomerObject = jsonBuilderService.createJsonObjectFromStream(customerStream);
    if (jsonCustomerObject == null) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Bad customer data.")
          .build();
    }

    Address address = addressService.getAddress(addressId);

    if (address == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Some involved entity does "
              + "not exist. See message body")
          .build();
    }

    return customerService.createCustomer(jsonCustomerObject, address, storeId);
  }

  @GET
  @Path("/count")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getCustomerCount() {
    return customerService.getCustomerCount();
  }

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCustomerByID(@PathParam("id") int customerId) {
    return customerService.getCustomerByID(customerId);
  }

  @GET
  @Path("/{id}/payments")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPaymentsByCustomerId(@PathParam("id") int customerId) {
    return customerService.getPaymentsByCustomerId(customerId);
  }

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCustomersLimit(@QueryParam("limit") @DefaultValue("100") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return customerService.getCustomersLimit(limit, offset);
  }
}