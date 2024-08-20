package com.gds.Gestion.de.stock.DTOs;


import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Data
public class ClientDTO {

    private String idClient;
    private String nom;
    private String prenom;
    private String adresse;
    private String telephone;
    private String email;
    private LocalDate dateAjout;

}
