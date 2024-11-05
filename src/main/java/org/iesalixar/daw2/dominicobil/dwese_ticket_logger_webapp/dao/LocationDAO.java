package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Location;

import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz LocationDAO que define las operaciones CRUD para la entidad Location.
 */
public interface LocationDAO {

    /**
     * Lista todas las ubicaciones de la base de datos.
     * @return Lista de ubicaciones
     */
    List<Location> listAllLocations();

    /**
     * Inserta una nueva ubicación en la base de datos.
     * @param location Ubicación a insertar
     */
    void insertLocation(Location location) ;

    /**
     * Actualiza una ubicación existente en la base de datos.
     * @param location Ubicación a actualizar
     */
    void updateLocation(Location location) ;

    /**
     * Elimina una ubicación de la base de datos.
     * @param id ID de la ubicación a eliminar
     */
    void deleteLocation(int id) ;

    /**
     * Obtiene una ubicación por su ID.
     * @param id ID de la ubicación
     * @return Ubicación correspondiente al ID
     */
    Location getLocationById(int id) ;


    boolean existsLocationByCode(String address);
    boolean existsLocationByCodeAndNotId(String address, int id);
}
