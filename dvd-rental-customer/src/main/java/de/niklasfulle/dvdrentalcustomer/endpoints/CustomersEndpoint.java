package de.niklasfulle.dvdrentalcustomer.endpoints;

import de.niklasfulle.dvdrentalcustomer.entities.Address;
import de.niklasfulle.dvdrentalcustomer.serviceses.AddressService;
import de.niklasfulle.dvdrentalcustomer.serviceses.CustomerService;
import de.niklasfulle.dvdrentalcustomer.serviceses.JsonBuilderService;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.InputStream;

/**
 * Endpoint for customers. The customers are stored in the database.
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

  /**
   * Creates a customer. The customer is stored in the database. The customer is created with the
   * given address. The address must exist. The address is passed as a query parameter.
   *
   * @param addressId      The address id.
   * @param storeId        The store id.
   * @param customerStream The customer data as a JSON object.
   * @return A response with the status code and a message.
   */
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

  /**
   * Get a customer by its Id. The customer is returned as a JSON object.
   *
   * @param customerId The Id of the customer.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCustomerById(@PathParam("id") int customerId) {
    return customerService.getCustomerById(customerId);
  }

  /**
   * Returns the payments of a customer. The payments are returned as a JSON
   *
   * @param customerId The Id of the customer.
   * @return A response with the status code and a message.
   */
  @GET
  @Path("/{id}/payments")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPaymentsByCustomerId(@PathParam("id") int customerId) {
    return customerService.getPaymentsByCustomerId(customerId);
  }

  /**
   * Get all customer. The customer are returned as a JSON array. The limit and offset can be used
   * to limit the number of customer returned.
   *
   * @param limit  The maximum number of customer to return.
   * @param offset The number of customer to skip.
   * @return A response with the status code and a message.
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getCustomersLimit(@QueryParam("limit") @DefaultValue("100") int limit,
      @QueryParam("offset") @DefaultValue("0") int offset) {
    return customerService.getCustomersLimit(limit, offset);
  }

  /**
   * Returns the number of customers.
   *
   * @return The number of customers.
   */
  @GET
  @Path("/count")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getCustomerCount() {
    return customerService.getCustomerCount();
  }
}