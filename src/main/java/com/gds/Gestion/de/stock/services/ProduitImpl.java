package com.gds.Gestion.de.stock.services;


import com.gds.Gestion.de.stock.DTOs.ProduitDTO;
import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Utilisateur;
import com.gds.Gestion.de.stock.exceptions.*;
import com.gds.Gestion.de.stock.mappers.ProduitMapper;
import com.gds.Gestion.de.stock.repositories.ProduitRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ProduitImpl implements InterfaceProduit {

    private ProduitRepository produitRepository;
    private ProduitMapper produitMapper;


    @Override
    public ProduitDTO enregistrerProd(ProduitDTO produitDTO) throws MontantQuantiteNullException, ProduitDupicateException, EmptyException {
        Produit produit = produitMapper.mapDeDtoAProd(produitDTO);
        /*Produit desigProduit = produitRepository.findByDesignation(produit.getDesignation());

        if (desigProduit != null)
           throw new ProduitDupicateException(desigProduit.getDesignation()+ " produit existe deja");*/

        if (produitDTO.getPrixUnitaire() <= 0 || produitDTO.getQuantite() <= 0)
            throw new MontantQuantiteNullException(produitDTO.getPrixUnitaire()+" ou"+produitDTO.getQuantite() +"doivent etre superieur a zero");

        int montant = produitDTO.getPrixUnitaire() * produitDTO.getQuantite();
        produit.setMontant(montant);
        produit.setDate(LocalDate.now());
        Utilisateur userConnecter = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        produit.setUtilisateurProd(userConnecter);
        produit.setIdProd("GDS "+UUID.randomUUID());
        produit.setDate(LocalDate.now());
        return produitMapper.mapDeProdADto(produitRepository.save(produit));
    }

    @Override
    public ProduitDTO enregistrerVenteProd(ProduitDTO produitDTO) {
        Produit produit = produitMapper.mapDeDtoAProd(produitDTO);
        Utilisateur userConnecter = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        produit.setUtilisateurProd(userConnecter);
        return produitMapper.mapDeProdADto(produitRepository.save(produit));
    }

    @Override
    public ProduitDTO modifierProd(ProduitDTO produitDTO) throws EmptyException {

        Produit produitExist = produitRepository.findById(produitDTO.getIdProd())
                .orElseThrow(()-> new EmptyException("Cet produits n'existe pas"));

        Produit produit = produitMapper.mapDeDtoAProd(produitDTO);

        produit.setIdProd(produitExist.getIdProd());
        int montant = produitDTO.getPrixUnitaire() * produitDTO.getQuantite();
        produit.setMontant(montant);
        produit.setDate(produitExist.getDate());

        return produitMapper.mapDeProdADto(produitRepository.save(produit));
    }

    @Override
    public void suppressionProd(String idProd) throws ProduitNotFoundException {
        ProduitDTO stock = afficherProd(idProd);
        if (stock != null) {
            throw new ProduitNotFoundException(stock.getDesignation()+ "n'existe pas");
        }
        produitRepository.deleteById(idProd);
    }

    @Override
    public ProduitDTO afficherProd(String idStock) throws ProduitNotFoundException {
       return produitMapper.mapDeProdADto(produitRepository.findById(idStock).orElseThrow(()->
               new ProduitNotFoundException(" Cet stock n'existe pas")));

    }

    @Override
    public List<ProduitDTO> ListerProd() {
        List<Produit> produits = produitRepository.findAll();
        return produits.stream().map(stock -> produitMapper.mapDeProdADto(stock))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProduitDTO> afficherProdSansStock() {
        List<Produit> produits = produitRepository.findProductsWithoutStock();
        return produits.stream().map(stock -> produitMapper.mapDeProdADto(stock))
                .collect(Collectors.toList());
    }
}
