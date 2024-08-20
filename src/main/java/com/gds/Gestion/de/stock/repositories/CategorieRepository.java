package com.gds.Gestion.de.stock.repositories;

import com.gds.Gestion.de.stock.entites.CategorieStock;
import com.gds.Gestion.de.stock.entites.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<CategorieStock, Long> {
    CategorieStock findByNom(String nom);
}
