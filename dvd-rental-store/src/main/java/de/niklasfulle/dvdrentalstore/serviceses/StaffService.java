package de.niklasfulle.dvdrentalstore.serviceses;

import jakarta.json.Json;
import jakarta.ejb.Stateless;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import de.niklasfulle.dvdrentalstore.entities.Staff;

/**
 * Service for Staff entity.
 */
@Stateless
public class StaffService {

  // The EntityManager is used to communicate with the database.
  @PersistenceContext
  EntityManager em;

  // Methods

  /**
   * Gets an Staff object from the database.
   *
   * @param staffId Staff id
   * @return Staff object
   */
  public Staff getStaff(int staffId) {
    return em.find(Staff.class, staffId);
  }

  /**
   * Gets an Staff object from the database and returns it as a JSON object.
   *
   * @param staffId Staff id
   * @return Response with status code and message
   */
  public Response getStaffById(int staffId) {
    Staff staff = em.find(Staff.class, staffId);
    if (staff == null) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity("Staff not found.")
          .build();
    }

    return Response.ok().entity(jsonObjectStaffBuilder(staff)).build();
  }

  /**
   * Builds a JsonObject from an Staff object.
   *
   * @param staff Staff object
   * @return JsonObject of Staff
   */
  public JsonObject jsonObjectStaffBuilder(Staff staff) {
    return Json.createObjectBuilder()
        .add("active", staff.getActive())
        .add("email", staff.getEmail())
        .add("firstName", staff.getFirstName())
        .add("id", staff.getStaffId())
        .add("lastName", staff.getLastName())
        .add("password", staff.getPassword())
        .add("picture", staff.getPictureString())
        .add("username", staff.getUsername())
        .build();
  }
}