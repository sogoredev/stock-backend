package com.gds.Gestion.de.stock.entites;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
public class Client {

    @Id
    private String idClient;
    @Column(length = 60)
    private String nom;
    @Column(length = 60)
    private String prenom;
    @Column(length = 80)
    private String adresse;
    @Column(length = 15)
    private String telephone;
    @Column(length = 80)
    private String email;
    private LocalDate dateAjout;

    @ManyToOne
    private Utilisateur utilisateurClient;
}
