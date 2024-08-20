package com.gds.Gestion.de.stock.repositories;

import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String> {
    Stock findByDesignation(String designation);
    Stock findStockByProduitStockIdProd(String produitStockId);

}
