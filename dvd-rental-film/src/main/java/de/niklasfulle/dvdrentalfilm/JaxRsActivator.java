package de.niklasfulle.dvdrentalfilm;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * The JaxRsActivator class is the entry point for all customer endpoints. It is used to define the
 * base path for all endpoints. The base path is /resources.
 */
@ApplicationPath("/resources")
public class JaxRsActivator extends Application {

}