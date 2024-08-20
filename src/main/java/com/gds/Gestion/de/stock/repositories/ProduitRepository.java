package com.gds.Gestion.de.stock.repositories;

import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProduitRepository extends JpaRepository<Produit, String> {
    Produit findByDesignation(String designation);

    @Query("SELECT s FROM Stock s WHERE s.produitStock.idProd = ?1")
    Stock findStockByProduitId(String produitId);

    @Query("SELECT p FROM Produit p LEFT JOIN Stock s ON p.id = s.produitStock.idProd WHERE s.id IS NULL")
    List<Produit> findProductsWithoutStock();
}
