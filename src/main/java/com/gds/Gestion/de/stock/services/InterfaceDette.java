package com.gds.Gestion.de.stock.services;

import com.gds.Gestion.de.stock.DTOs.DetteDTO;
import com.gds.Gestion.de.stock.DTOs.VenteDTO;
import com.gds.Gestion.de.stock.exceptions.*;

import java.util.List;

public interface InterfaceDette {

    DetteDTO enregistrerDette(DetteDTO detteDTO) throws DetteDuplicateException, EmptyException;

    DetteDTO paiementDette(String idDette) throws EmptyException, DetteNotFoundException;

    List<DetteDTO> listerDette();

    DetteDTO modifierDette(DetteDTO detteDTO) throws ApprovNotFoundException, DetteNotFoundException, EmptyException;

    DetteDTO afficher(String idDette) throws DetteNotFoundException;
}
