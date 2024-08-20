package com.gds.Gestion.de.stock.services;

import com.gds.Gestion.de.stock.DTOs.VenteDTO;
import com.gds.Gestion.de.stock.exceptions.EmptyException;
import com.gds.Gestion.de.stock.exceptions.VenteNotFoundException;

import java.util.List;

public interface InterfaceVente {

    VenteDTO effectuerVente(VenteDTO venteDTO) throws Exception;
    VenteDTO modifierVente(VenteDTO venteDTO) throws EmptyException;
    VenteDTO afficherVente(String idVente) throws VenteNotFoundException;
    List<VenteDTO> listerVente();
    long totalVente();
    void supprimerVente(String idVente) throws VenteNotFoundException;
}
