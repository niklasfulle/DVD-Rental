package de.niklasfulle.dvdrentalstore.serviceses;

import java.util.List;
import jakarta.json.Json;
import java.util.LinkedList;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.niklasfulle.dvdrentalstore.entities.Inventory;

/**
 * Service for Inventory entity.
 */
@Stateless
public class InventoryService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Gets an Inventory object from the database.
   *
   * @param inventoryId Inventory id
   * @return
   */
  public Inventory getInventory(int inventoryId) {
    return em.find(Inventory.class, inventoryId);
  }

  /**
   * Gets an Inventory object from the database and returns it as a JSON object.
   *
   * @param inventoryId Inventory id
   * @return Response with status code and message
   */
  public Response getInventoryById(int inventoryId) {
    Inventory inventory = em.find(Inventory.class, inventoryId);
    if (inventory == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Customer not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectInventoryBuilder(inventory)).build();
  }

  /**
   * Returns a list of all Inventorys with the given film id.
   *
   * @param filmId Film id
   * @return Response with status code and message
   */
  public Response getInventoryListByFilmId(int filmId) {
    List<Inventory> inventories = em.createNamedQuery("Inventory.getAllByFilmId", Inventory.class)
        .setParameter(1, filmId)
        .getResultList();

    if (inventories.isEmpty()) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Inventorys not found.")
          .build();
    }

    List<JsonObject> response = new LinkedList<>();

    for (Inventory inventory : inventories) {
      response.add(jsonObjectInventoryBuilder(inventory));
    }
    return Response.ok().entity(response).build();
  }

  /**
   * Builds a JsonObject from an Inventory object.
   *
   * @param inventory Inventory object
   * @return JsonObject of Inventory
   */
  public JsonObject jsonObjectInventoryBuilder(Inventory inventory) {
    return Json.createObjectBuilder()
        .add("film",
            Json.createObjectBuilder()
                .add("href", "/dvd-rental-film/resources/films/" + inventory.getFilmId()).build())
        .add("id", inventory.getInventoryId())
        .add("store", Json.createObjectBuilder()
            .add("href", "/dvd-rental-store/resources/stores/" + inventory.getStore().getStoreId())
            .build())
        .build();
  }
}