package com.gds.Gestion.de.stock.services;


import com.gds.Gestion.de.stock.DTOs.ApprovisionDTO;
import com.gds.Gestion.de.stock.entites.*;
import com.gds.Gestion.de.stock.enums.Etat;
import com.gds.Gestion.de.stock.exceptions.*;
import com.gds.Gestion.de.stock.mappers.ApprovisionMapper;
import com.gds.Gestion.de.stock.mappers.ProduitMapper;
import com.gds.Gestion.de.stock.mappers.StockMapper;
import com.gds.Gestion.de.stock.repositories.ApprovisionRepository;
import com.gds.Gestion.de.stock.repositories.ProduitRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ApprovisionImpl implements InterfaceApprovision {

    private ApprovisionRepository approvisionRepository;
    private ApprovisionMapper approvisionMapper;
    private ProduitRepository produitRepository;
    private InterfaceProduit interfaceProduit;
    private ProduitMapper produitMapper;

    @Override
    public ApprovisionDTO traiterApprovision(String idApprov) throws EmptyException, ProduitDupicateException, ApprovNotFoundException {

        Approvision approvision = approvisionRepository.findById(idApprov)
                .orElseThrow(()-> new ApprovNotFoundException("Cet apprivision n'existe pas"));;

        if (approvision.getEtat() != Etat.ENCORE)
            throw new EmptyException("Approvion est deja traite");

        List<Produit> produitList = approvision.getProduitsApprov();
        if (produitList.isEmpty())
            throw new EmptyException("La liste de produit est vide");

        for (Produit produit : produitList){

            int nouvelQuantite = produit.getQuantite() + approvision.getQuantite();
            produit.setQuantite(nouvelQuantite);
            int nouvelMontant = produit.getMontant() + approvision.getMontant();
            produit.setMontant(nouvelMontant);
            produit.setIdProd(produit.getIdProd());
            interfaceProduit.enregistrerVenteProd(produitMapper.mapDeProdADto(produit));
            approvision.setEtat(Etat.ARRIVER);
            approvisionRepository.save(approvision);
        }
        return null;
    }

    @Override
    public ApprovisionDTO enregistrerApprovision(ApprovisionDTO approvisionDTO) throws ApprovisionDupicateException, EmptyException {
        Approvision approvision = approvisionMapper.mapDeDtoAApprov(approvisionDTO);

        if (approvisionDTO.getAdresseFrs().isEmpty() || approvisionDTO.getCbm() <= 0 || approvisionDTO.getQuantite() <= 0 || approvisionDTO.getPrixUnitaire() <= 0)
            throw new EmptyException("Remplisser les champs vides");

        Approvision byDesignation = approvisionRepository.findByDesignation(approvision.getDesignation());
        if (byDesignation != null)
            throw new ApprovisionDupicateException("La designation: "+byDesignation.getDesignation()+ " existe deja");

        Utilisateur userConnected = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        approvision.setUtilisateurAprov(userConnected);
        approvision.setIdApprov(UUID.randomUUID().toString());
        approvision.setEtat(Etat.ENCORE);
        approvision.setFraisTransit((int) (approvision.getCbm() * 26000));
        int montant = approvision.getPrixUnitaire() * approvision.getQuantite();
        approvision.setMontant(montant + approvision.getFraisTransit());
        approvisionMapper.mapDeApprovADto(approvisionRepository.save(approvision));
        return approvisionDTO;
    }

    @Override
    public ApprovisionDTO modifierApprov(ApprovisionDTO approvisionDTO) throws ApprovNotFoundException {
        Approvision approvExist = approvisionRepository.findById(approvisionDTO.getIdApprov()).
                orElseThrow(()-> new ApprovNotFoundException("Cet Approvision n'existe pas"));
        Approvision approvision = approvisionMapper.mapDeDtoAApprov(approvisionDTO);
        approvision.setIdApprov(approvExist.getIdApprov());
        approvision.setEtat(approvExist.getEtat());
        approvision.setFraisTransit((int) (approvision.getCbm() * 26000));
        int montant = approvision.getPrixUnitaire() * approvision.getQuantite();
        approvision.setMontant(montant + approvision.getFraisTransit());
        return approvisionMapper.mapDeApprovADto(approvisionRepository.save(approvision));
    }

    @Override
    public ApprovisionDTO afficher(String idApprov) throws ApprovNotFoundException {
        Approvision approvision = approvisionRepository.findById(idApprov).
                orElseThrow(()-> new ApprovNotFoundException("Cet Approvision n'existe pas"));
        return approvisionMapper.mapDeApprovADto(approvision);
    }


    @Override
    public List<ApprovisionDTO> listerApprovision() {
        List<Approvision> approvisionList = approvisionRepository.findAll();
        return approvisionList.stream()
                .map(approvision -> approvisionMapper.mapDeApprovADto(approvision))
                .collect(Collectors.toList());
    }
}



