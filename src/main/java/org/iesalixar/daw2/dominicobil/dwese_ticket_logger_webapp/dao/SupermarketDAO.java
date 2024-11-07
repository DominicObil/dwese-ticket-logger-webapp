package org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.dao;

import org.iesalixar.daw2.dominicobil.dwese_ticket_logger_webapp.entity.Supermarket;


import java.util.List;

public interface SupermarketDAO {
    List<Supermarket> listAllSupermarkets();
    Supermarket getSupermarketById(int id) ;
    void insertSupermarket(Supermarket supermarket);
    void updateSupermarket(Supermarket supermarket) ;
    void deleteSupermarket(int id) ;
    boolean existsSupermarketByName(String name) ;
    boolean existsSupermarketByNameAndNotId(String name, int id);
}