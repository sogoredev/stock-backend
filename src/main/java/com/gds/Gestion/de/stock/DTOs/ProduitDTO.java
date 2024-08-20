package com.gds.Gestion.de.stock.DTOs;


import com.gds.Gestion.de.stock.entites.Stock;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;


@Data
public class ProduitDTO {

    private String idProd;
    private String designation;
    private int quantite;
    private int prixUnitaire;
    private int montant;
    private String image;
    private LocalDate date;
    private String note;
    private CategorieStockDTO categorieStockProdDTO;

}
