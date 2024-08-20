package com.gds.Gestion.de.stock.DTOs;



import com.gds.Gestion.de.stock.entites.Utilisateur;
import com.gds.Gestion.de.stock.enums.Etat;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;


@Data
public class ApprovisionDTO {

    private String idApprov;
    private String designation;
    private int quantite;
    private int prixUnitaire;
    private int montant;
    private int fraisTransit;
    private double cbm;
    private LocalDate dateAchat;
    private LocalDate dateArriver;
    private String adresseFrs;
    private String image;
    private Etat etat;
    private String description;
    private List<ProduitDTO> produitsApprov;

}
