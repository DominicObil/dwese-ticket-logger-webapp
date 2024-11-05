package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import jakarta.persistence.EntityManager;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;


@Repository
@Transactional
public class SupermarketDAOImpl implements SupermarketDAO {


    // Logger para registrar eventos importantes en el DAO
    private static final Logger logger = LoggerFactory.getLogger(SupermarketDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Lists all supermarkets from the database.
     *
     * @return List of supermarkets
     */
    @Override
    public List<Supermarket> listAllSupermarkets() {
        logger.info("Listing all supermarkets from the database.");
        String query = "SELECT s FROM Supermarket s";
        List<Supermarket> supermarkets = entityManager.createQuery(query, Supermarket.class).getResultList();
        logger.info("Retrieved {} supermarkets from the database.", supermarkets.size());
        return supermarkets;
    }

    /**
     * Inserts a new supermarket into the database.
     *
     * @param supermarket Supermarket to insert
     */
    @Override
    public void insertSupermarket(Supermarket supermarket) {
        logger.info("Inserting supermarket with name: {}", supermarket.getName());
        entityManager.persist(supermarket);
        logger.info("Supermarket inserted. Rows affected: {}", supermarket.getId());
    }

    /**
     * Updates an existing supermarket in the database.
     *
     * @param supermarket Supermarket to update
     */
    @Override
    public void updateSupermarket(Supermarket supermarket) {
        logger.info("Updating supermarket with ID: {}", supermarket.getId());
        entityManager.merge(supermarket);
        logger.info("Supermarket updated. Rows affected: {}", supermarket.getId());
    }

    /**
     * Deletes a supermarket from the database.
     *
     * @param id ID of the supermarket to delete
     */
    @Override
    public void deleteSupermarket(int id) {
        logger.info("Deleting supermarket with ID: {}", id);
        Supermarket supermarket = entityManager.find(Supermarket.class, id);
        if (supermarket != null) {
            entityManager.remove(supermarket);
            logger.info("Supermarket with ID: {} deleted.", id);
        } else {
            logger.info("Supermarket with ID: {} not found.", id);
        }
    }

    /**
     * Retrieves a supermarket by its ID.
     *
     * @param id ID of the supermarket to retrieve
     * @return Supermarket found or null if it doesn't exist
     */
    @Override
    public Supermarket getSupermarketById(int id) {
        logger.info("Retrieving supermarket by ID: {}", id);
        Supermarket supermarket = entityManager.find(Supermarket.class, id);
        if (supermarket != null) {
            logger.info("Supermarket found: {} - {}", supermarket.getName(), supermarket.getLocations());
        } else {
            logger.info("Supermarket with ID: {} not found.", id);
        }
        return supermarket;
    }

    /**
     * Checks if a supermarket with the specified name already exists in the database.
     *
     * @param name the name of the supermarket to check
     * @return true if a supermarket with the name exists, false otherwise
     */
    @Override
    public boolean existsSupermarketByName(String name) {
        logger.info("Checking if supermarket with name: {} exists.", name);
        String query = "SELECT COUNT(s) FROM Supermarket s WHERE UPPER(s.name) = :name";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("name", name.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Supermarket with name: {} exists: {}", name, exists);
        return exists;
    }

    /**
     * Checks if a supermarket with the specified name already exists in the database,
     * excluding a supermarket with a specific ID.
     *
     * @param name the name of the supermarket to check
     * @param id   the ID of the supermarket to exclude from the check
     * @return true if a supermarket with the name exists (and is not the supermarket with the given ID),
     * false otherwise
     */
    @Override
    public boolean existsSupermarketByNameAndNotId(String name, int id) {
        logger.info("Checking if supermarket with name: {} exists excluding ID: {}", name, id);
        String query = "SELECT COUNT(s) FROM Supermarket s WHERE UPPER(s.name) = :name AND id != :id";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("name", name.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Supermarket with name: {} exists excluding ID {}: {}", name, id, exists);
        return exists;
    }
}