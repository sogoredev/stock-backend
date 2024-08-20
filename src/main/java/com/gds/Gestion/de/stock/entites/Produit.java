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
public class Produit {

    @Id
    private String idProd;
    @Column(length = 60)
    private String designation;
    @Column(length = 10)
    private int quantite;
    @Column(length = 100)
    private int prixUnitaire;
    @Column(length = 100)
    private int montant;
    private String image;
    private LocalDate date;
    @Column(length = 255)
    private String note;

//    SET FOREIGN_KEY_CHECKS=0;


    @ManyToOne
    private Utilisateur utilisateurProd;

    @ManyToOne
    private CategorieStock categorieStock;


/*    @ManyToMany
    @JoinTable(
            name = "produitsVente",
            joinColumns = @JoinColumn(name = "prod_id"),
            inverseJoinColumns = @JoinColumn(name = "vente_id"))
    private List<Vente> venteProduit = new ArrayList<>();*/

/*    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "produitsDette",
            joinColumns = @JoinColumn(name = "prod_id"),
            inverseJoinColumns = @JoinColumn(name = "dette_id"))
    private List<Dette> detteProduit = new ArrayList<>();*/
}
