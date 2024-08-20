package com.gds.Gestion.de.stock.entites;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vente {

    @Id
    private String idVente;
    @Column(length = 50)
    private String description;
    @Column(length = 100)
    private int montant;
    @Column(length = 10)
    private int quantite;
    @Column(length = 100)
    private String note;
    private LocalDate dateVente;
    @Column(length = 10)
    private int reduction;

    @ManyToOne
    private Utilisateur utilisateurVente;

    @ManyToOne
    private Client clientsVente;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Produit> produitVente = new ArrayList<>();
}
