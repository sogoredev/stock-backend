package com.gds.Gestion.de.stock.services;


import com.gds.Gestion.de.stock.DTOs.DetteDTO;
import com.gds.Gestion.de.stock.entites.*;
import com.gds.Gestion.de.stock.enums.StatusDette;
import com.gds.Gestion.de.stock.exceptions.*;
import com.gds.Gestion.de.stock.mappers.DetteMapper;
import com.gds.Gestion.de.stock.mappers.ProduitMapper;
import com.gds.Gestion.de.stock.repositories.DetteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class DetteImpl implements InterfaceDette {

    @Autowired
    private DetteMapper detteMapper;
    @Autowired
    private DetteRepository detteRepository;
    @Autowired
    private InterfaceProduit interfaceProduit;
    @Autowired
    private ProduitMapper produitMapper;

    @Override
    public DetteDTO enregistrerDette(DetteDTO detteDTO) throws DetteDuplicateException, EmptyException {
        Dette dette = detteMapper.mapDeDtoADette(detteDTO);
        Dette byTitre = detteRepository.findByTitre(dette.getTitre());

        if (byTitre != null){
            throw new DetteDuplicateException(byTitre.getTitre()+ " existe deja");
        }

        Utilisateur principal = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        dette.setUtilisateurDette(principal);
        dette.setIdDette(UUID.randomUUID().toString());
        dette.setStatus(StatusDette.NON_PAYER);

        List<Produit> produitList = dette.getProduitDette();
        if (produitList.isEmpty())
            throw new EmptyException("La liste de produit est vide");
        for (Produit produit : produitList){
            dette.setMontant(getMontantDette(produit, dette));
        }

        return detteMapper.mapDeDetteADTO(detteRepository.save(dette));
    }

    @Override
    public DetteDTO paiementDette(String idDette) throws EmptyException, DetteNotFoundException {

        Dette dette = detteRepository.findById(idDette)
                .orElseThrow(()-> new DetteNotFoundException("Cet dette n'existe pas"));

        if (dette.getStatus() != StatusDette.NON_PAYER)
            throw new EmptyException("Dette est deja paye");

        List<Produit> produitList = dette.getProduitDette();
        if (produitList.isEmpty())
            throw new EmptyException("La liste de produit est vide");

        for (Produit produit : produitList){

            dette.setMontant(getMontantDette(produit, dette));

            int nouvelQuantite = produit.getQuantite() - dette.getQuantite();
            produit.setQuantite(nouvelQuantite);

            int nouvelMontant = produit.getMontant() - dette.getMontant();
            produit.setMontant(nouvelMontant);

            produit.setIdProd(produit.getIdProd());
            interfaceProduit.enregistrerVenteProd(produitMapper.mapDeProdADto(produit));

            dette.setStatus(StatusDette.PAYER);
            detteRepository.save(dette);
        }
        return null;
    }

    private static int getMontantDette(Produit produit, Dette dette) {
        int montant = produit.getPrixUnitaire() * dette.getQuantite();
        if (dette.getReduction() < 0 )
            throw new InsufficientStockException("La reduction doit etre superieur a zero : " + dette.getReduction());
        if (dette.getReduction() >= montant)
            throw new InsufficientStockException("La reduction est trop eleve pour le produit " + produit.getDesignation());
        if (dette.getReduction() > (0.2 * montant))
            throw new InsufficientStockException("La réduction ne peut pas dépasser 20 % du montant pour le produit " + produit.getDesignation());
        return montant - dette.getReduction();
    }

    @Override
    public List<DetteDTO> listerDette() {
        List<Dette> dettesList = detteRepository.findAll();
        return dettesList.stream()
                .map(dette -> detteMapper.mapDeDetteADTO(dette))
                .collect(Collectors.toList());
    }

    @Override
    public DetteDTO modifierDette(DetteDTO detteDTO) throws DetteNotFoundException, EmptyException {
        Dette detteExist = detteRepository.findById(detteDTO.getIdDette()).
                orElseThrow(()-> new DetteNotFoundException("Cet Dette n'existe pas"));
        Dette dette = detteMapper.mapDeDtoADette(detteDTO);
        dette.setIdDette(detteExist.getIdDette());
        dette.setStatus(detteExist.getStatus());

        List<Produit> produitList = dette.getProduitDette();
        if (produitList.isEmpty())
            throw new EmptyException("La liste de produit est vide");
        for (Produit produit : produitList){
            dette.setMontant(getMontantDette(produit, dette));
        }

        return detteMapper.mapDeDetteADTO(detteRepository.save(dette));
    }

    @Override
    public DetteDTO afficher(String idDette) throws DetteNotFoundException {
        Dette dette = detteRepository.findById(idDette).
                orElseThrow(()-> new DetteNotFoundException("Cet dette n'existe pas"));
        return detteMapper.mapDeDetteADTO(dette);
    }
}

//    SET FOREIGN_KEY_CHECKS=0;


