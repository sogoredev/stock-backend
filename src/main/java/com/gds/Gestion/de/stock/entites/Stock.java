package com.gds.Gestion.de.stock.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    private String idStock;
    private String designation;
    private int quantite;
    private int montant;
    private LocalDate date;
    private String note;

    @ManyToOne
    private Utilisateur utilisateurStock;

    @OneToOne
    private Produit produitStock;



}
