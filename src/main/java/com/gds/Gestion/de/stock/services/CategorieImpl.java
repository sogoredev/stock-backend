package com.gds.Gestion.de.stock.services;


import com.gds.Gestion.de.stock.DTOs.CategorieStockDTO;
import com.gds.Gestion.de.stock.entites.CategorieStock;
import com.gds.Gestion.de.stock.entites.Utilisateur;
import com.gds.Gestion.de.stock.exceptions.CategorieDuplicateException;
import com.gds.Gestion.de.stock.exceptions.CategorieNotFoundException;
import com.gds.Gestion.de.stock.exceptions.EmptyException;
import com.gds.Gestion.de.stock.mappers.CategorieMapper;
import com.gds.Gestion.de.stock.repositories.CategorieRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CategorieImpl implements InterfaceCategorie {

    private CategorieRepository categorieRepository;
    private CategorieMapper categorieMapper;


    @Override
    public CategorieStockDTO creerCat(CategorieStockDTO categorieStockDTO) throws CategorieDuplicateException, EmptyException {
        CategorieStock categorieStock = categorieMapper.mapDeDtoACategorie(categorieStockDTO);
        CategorieStock cat = categorieRepository.findByNom(categorieStock.getNom());

        if (categorieStockDTO.getNom().isEmpty())
            throw new EmptyException("Remplissez les champs vides");

        if (cat != null)
            throw new CategorieDuplicateException(cat.getNom() +" existe deja");

        Utilisateur principal = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        categorieStock.setUtilisateurCat(principal);
        categorieStock.setDate(LocalDate.now());
        return categorieMapper.mapDeCategorieADto(categorieRepository.save(categorieStock));
    }

    @Override
    public CategorieStockDTO modifierCat(CategorieStockDTO categorieStockDTO) throws EmptyException {
        CategorieStock categorieStockExist = categorieRepository.findById(categorieStockDTO.getIdCat())
                .orElseThrow(()-> new EmptyException("Cet categorie n'existe pas"));
        CategorieStock categorieStock = categorieMapper.mapDeDtoACategorie(categorieStockDTO);
        categorieStock.setIdCat(categorieStockExist.getIdCat());
        categorieStock.setDate(categorieStockExist.getDate());
        return categorieMapper.mapDeCategorieADto(categorieRepository.save(categorieStock));
    }

    @Override
    public String supprimerCat(Long idCategorieStock) {
        categorieRepository.deleteById(idCategorieStock);
        return "Categorie supprimer";
    }

    @Override
    public List<CategorieStockDTO> listCat() {
        List<CategorieStock> categorieStockDTOList = categorieRepository.findAll();
        return categorieStockDTOList.stream()
                .map(cat->categorieMapper.mapDeCategorieADto(cat))
                .collect(Collectors.toList());
    }

    @Override
    public CategorieStockDTO afficher(Long idCat) throws CategorieNotFoundException {
        CategorieStock categorieStock = categorieRepository.findById(idCat).
                orElseThrow(()-> new CategorieNotFoundException("Cet categorie n'existe pas"));
        return categorieMapper.mapDeCategorieADto(categorieStock);
    }
}
