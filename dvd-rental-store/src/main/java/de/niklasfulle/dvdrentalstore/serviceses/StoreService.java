package de.niklasfulle.dvdrentalstore.serviceses;

import jakarta.json.Json;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.niklasfulle.dvdrentalstore.entities.Store;

/**
 * Service for Store entity.
 */
@Stateless
public class StoreService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods 

  /**
   * Gets an Store object from the database.
   *
   * @param storeId Store id
   * @return Response with status code and message
   */
  public Response getStoreById(int storeId) {
    Store store = em.find(Store.class, storeId);
    if (store == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Store not found.")
          .build();
    }

    JsonObject response = Json.createObjectBuilder().add("id", store.getStoreId()).build();
    return Response.ok().entity(response).build();
  }

  /**
   * Returns the number of stores in the database.
   *
   * @return Response with status code and message
   */
  public Response getStoreCount() {
    return Response.ok().entity(em.createNamedQuery("Store.getAll", Store.class)
            .getResultList()
            .size())
        .build();
  }
}