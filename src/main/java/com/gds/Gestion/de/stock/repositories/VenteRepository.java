package com.gds.Gestion.de.stock.repositories;

import com.gds.Gestion.de.stock.DTOs.VenteDTO;
import com.gds.Gestion.de.stock.entites.Client;
import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Stock;
import com.gds.Gestion.de.stock.entites.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VenteRepository extends JpaRepository<Vente, String> {
    Vente findByDescription(String description);

    @Query(value = "SELECT COUNT(*) FROM vente", nativeQuery = true)
    long countTotalVente();


}
