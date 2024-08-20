package com.gds.Gestion.de.stock.DTOs;


import com.gds.Gestion.de.stock.entites.Produit;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class StockDTO {

    private String idStock;
    private String designation;
    private int quantite;
    private int montant;
    private LocalDate date;
    private String note;
    private ProduitDTO produitStockDTO;

}
