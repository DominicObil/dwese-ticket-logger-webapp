package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Category;

import java.util.List;

public interface CategoryDAO {

    List<Category> listAllCategory() ;

    void deleteCategory(int id) ;

    Category getCategoryById(int id) ;

    public void insertCategory(Category categorie);

    public void updateCategory(Category categorie);

    boolean existsCategoryByNameAndNotId(String name, int id) ;

    boolean existsCategoryByName(String address) ;
}
