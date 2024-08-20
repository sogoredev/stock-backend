package com.gds.Gestion.de.stock.mappers;


import com.gds.Gestion.de.stock.DTOs.ClientDTO;
import com.gds.Gestion.de.stock.DTOs.ProduitDTO;
import com.gds.Gestion.de.stock.entites.Client;
import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Stock;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProduitMapper {

    @Autowired
    private CategorieMapper categorieMapper;

    public ProduitDTO mapDeProdADto(Produit produit) {
        ProduitDTO produitDTO = new ProduitDTO();
        BeanUtils.copyProperties(produit, produitDTO);
        produitDTO.setCategorieStockProdDTO(categorieMapper.mapDeCategorieADto(produit.getCategorieStock()));
        return produitDTO;
    }


    public Produit mapDeDtoAProd(ProduitDTO produitDTO) {
        Produit produit = new Produit();
        BeanUtils.copyProperties(produitDTO, produit);
        produit.setCategorieStock(categorieMapper.mapDeDtoACategorie(produitDTO.getCategorieStockProdDTO()));
        return produit;
    }
}
