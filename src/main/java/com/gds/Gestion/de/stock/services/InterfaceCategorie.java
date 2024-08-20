package com.gds.Gestion.de.stock.services;


import com.gds.Gestion.de.stock.DTOs.CategorieStockDTO;
import com.gds.Gestion.de.stock.exceptions.CategorieDuplicateException;
import com.gds.Gestion.de.stock.exceptions.CategorieNotFoundException;
import com.gds.Gestion.de.stock.exceptions.EmptyException;

import java.util.List;

public interface InterfaceCategorie {

    CategorieStockDTO creerCat(CategorieStockDTO categorieStockDTO) throws CategorieDuplicateException, EmptyException;
    CategorieStockDTO modifierCat(CategorieStockDTO categorieStockDTO) throws CategorieDuplicateException, EmptyException;
    String supprimerCat(Long idCategorieStock);
    List<CategorieStockDTO> listCat();
    CategorieStockDTO afficher(Long idCat) throws CategorieNotFoundException;

}
