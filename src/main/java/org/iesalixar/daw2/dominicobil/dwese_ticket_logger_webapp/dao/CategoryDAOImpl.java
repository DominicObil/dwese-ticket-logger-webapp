package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class CategoryDAOImpl implements CategoryDAO {

    private static final Logger logger = LoggerFactory.getLogger(CategoryDAOImpl.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Category> listAllCategory() {
        logger.info("Listing all categories from the database.");
        String query = "SELECT c FROM Category c"; // Cambiado a JPQL
        List<Category> categories = entityManager.createQuery(query, Category.class).getResultList();
        logger.info("Retrieved {} categories from the database.", categories.size());
        return categories;
    }



    @Override
    public void insertCategory(Category category) {
        logger.info("Inserting Category with name: {} and image: {}", category.getName(), category.getImage());
        entityManager.persist(category);
        logger.info("Inserted Category with ID: {}", category.getId());
    }

    @Override
    public void updateCategory(Category category) {
        logger.info("Updating Category with id: {}", category.getId());
        entityManager.merge(category);
        logger.info("Updated Category with id: {}", category.getId());
    }

    @Override
    public void deleteCategory(int id) {
        logger.info("Deleting Category with id: {}", id);
        Category category = entityManager.find(Category.class, id);
        if (category != null) {
            entityManager.remove(category);
            logger.info("Deleted Category with id: {}", id);
        } else {
            logger.warn("Category with id: {} not found.", id);
        }
    }



    @Override
    public Category getCategoryById(int id) {
        logger.info("Retrieving Category by id: {}", id);
        Category category = entityManager.find(Category.class, id);
        if (category != null) {
            logger.info("Category retrieved: {} - {}", category.getName(), category.getImage());
        } else {
            logger.warn("No Category found with id: {}", id);
        }
        return category;
    }
    @Override
    public boolean existsCategoryByNameAndNotId(String name, int id) {
        logger.info("Checking if Category with name: {} exists excluding id: {}", name, id);
        String query = "SELECT COUNT(c) FROM Category c WHERE UPPER(c.name) = :name AND c.id != :id";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("name", name.toUpperCase())
                .setParameter("id", id)
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Category with name: {} exists excluding id {}: {}", name, id, exists);
        return exists;
    }

    @Override
    public boolean existsCategoryByName(String name) {
        logger.info("Checking if Category with name: {} exists", name);
        String query = "SELECT COUNT(c) FROM Category c WHERE UPPER(c.name) = :name";
        Long count = entityManager.createQuery(query, Long.class)
                .setParameter("name", name.toUpperCase())
                .getSingleResult();
        boolean exists = count != null && count > 0;
        logger.info("Category with name: {} exists: {}", name, exists);
        return exists;
    }
}

