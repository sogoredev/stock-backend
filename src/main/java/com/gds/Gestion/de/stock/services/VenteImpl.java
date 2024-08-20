package com.gds.Gestion.de.stock.services;


import com.gds.Gestion.de.stock.DTOs.VenteDTO;
import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Utilisateur;
import com.gds.Gestion.de.stock.entites.Vente;
import com.gds.Gestion.de.stock.exceptions.*;
import com.gds.Gestion.de.stock.mappers.ProduitMapper;
import com.gds.Gestion.de.stock.mappers.VenteMapper;
import com.gds.Gestion.de.stock.repositories.VenteRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class VenteImpl implements InterfaceVente {

    @Autowired
    private VenteMapper venteMapper;
    @Autowired
    private VenteRepository venteRepository;
    @Autowired
    private InterfaceProduit interfaceProduit;
    @Autowired
    private ProduitMapper produitMapper;

//    ajouter une vente
    @Override
    public VenteDTO effectuerVente(VenteDTO venteDTO) throws Exception {

        Vente vente = venteMapper.mapDeDtoAVente(venteDTO);

        if (venteDTO.getQuantite() <= 0)
            throw new EmptyException("Quantite doit etre superieur a zero");

        List<Produit> produitsVenteList = vente.getProduitVente();
        if (produitsVenteList != null) {
            for (Produit produit : produitsVenteList) {
                if (produit != null) {
                    if (produit.getQuantite() < vente.getQuantite() && produit.getMontant() < vente.getMontant()) {
                        throw new InsufficientStockException("Quantité insuffisante pour le produit : " + produit.getIdProd());
                    }
                    Utilisateur userConnected = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    vente.setUtilisateurVente(userConnected);
                    vente.setDateVente(LocalDate.now());

                    int montantVente = getMontantVente(venteDTO, produit, vente);
                    vente.setMontant(montantVente);
                    vente.setIdVente(UUID.randomUUID().toString());

                    int montantProd = produit.getMontant() - montantVente;
                    produit.setMontant(montantProd);
                    int quantiteProd = produit.getQuantite() - vente.getQuantite();
                    produit.setQuantite(quantiteProd);
                    produit.setIdProd(produit.getIdProd());
                    interfaceProduit.enregistrerVenteProd(produitMapper.mapDeProdADto(produit));

                    return venteMapper.mapDeVenteADTO(venteRepository.save(vente));
                } else {
                    throw new InsufficientStockException("Cet produit n'est pas en stock : " + produit.getDesignation());
                }
            }
        }else {
            throw new InsufficientStockException("Vous n'avez pas de produits ");
        }
        return venteDTO;
    }

//    calcul du montant de vente
    private static int getMontantVente(VenteDTO venteDTO, Produit produit, Vente vente) {
        int montant = produit.getPrixUnitaire() * venteDTO.getQuantite();
        if (vente.getReduction() < 0 )
            throw new InsufficientStockException("La reduction doit etre superieur a zero : " + vente.getReduction());
        if (vente.getReduction() >= montant)
            throw new InsufficientStockException("La reduction est trop eleve pour le produit " + produit.getDesignation());
        if (vente.getReduction() > (0.2 * montant))
            throw new InsufficientStockException("La réduction ne peut pas dépasser 20 % du montant pour le produit " + produit.getDesignation());
        return montant - vente.getReduction();
    }

//  modifier une vente
    @Override
    public VenteDTO modifierVente(VenteDTO venteDTO) throws EmptyException {
        Vente venteExist = venteRepository.findById(venteDTO.getIdVente())
                .orElseThrow(()-> new EmptyException("Cette vente n'existe pas"));
        Vente vente = venteMapper.mapDeDtoAVente(venteDTO);

        List<Produit> produitsVenteList = vente.getProduitVente();
        if (produitsVenteList != null) {
            for (Produit produit : produitsVenteList) {
                if (produit != null) {
                    if (produit.getQuantite() < vente.getQuantite() && produit.getMontant() < vente.getMontant()) {
                        throw new InsufficientStockException("Quantité insuffisante pour le produit : " + produit.getDesignation());
                    }
                    int montantVente = getMontantVente(venteDTO, produit, vente);
                    vente.setMontant(montantVente);
                    vente.setDateVente(venteExist.getDateVente());
                    vente.setIdVente(venteExist.getIdVente());

                    int montantProd = produit.getMontant() - montantVente;
                    produit.setMontant(montantProd);
                    int quantiteProd = produit.getQuantite() - vente.getQuantite();
                    produit.setQuantite(quantiteProd);
                    produit.setIdProd(produit.getIdProd());
                    interfaceProduit.enregistrerVenteProd(produitMapper.mapDeProdADto(produit));

                    return venteMapper.mapDeVenteADTO(venteRepository.save(vente));
                } else {
                    throw new InsufficientStockException("Cet produit n'est pas en stock : " + produit.getIdProd());
                }
            }
        }else {
            throw new InsufficientStockException("Vous n'avez pas de produits ");
        }
        return venteDTO;
    }

//    recuperer une vente
    @Override
    public VenteDTO afficherVente(String idVente) throws VenteNotFoundException {
        return venteMapper.mapDeVenteADTO(venteRepository.findById(idVente).orElseThrow(()-> new VenteNotFoundException("Vente n'existe pas")));
    }

//  recperer la liste des vente
    @Override
    public List<VenteDTO> listerVente() {
        List<Vente> allVente = venteRepository.findAll();
        return allVente.stream().map(vente -> venteMapper.mapDeVenteADTO(vente)).collect(Collectors.toList());
    }

    @Override
    public long totalVente() {
        return venteRepository.countTotalVente();
    }

    //    supprimer une vente
    @Override
    public void supprimerVente(String idVente) throws VenteNotFoundException {
        VenteDTO venteDTO = afficherVente(idVente);
        if (venteDTO == null){
            throw new VenteNotFoundException("Vente n'existe pas");
        }
        venteRepository.deleteById(idVente);
    }
}

//    SET FOREIGN_KEY_CHECKS=0;


