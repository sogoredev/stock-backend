package com.gds.Gestion.de.stock.services;

import com.gds.Gestion.de.stock.DTOs.ApprovisionDTO;
import com.gds.Gestion.de.stock.exceptions.*;

import java.util.List;

public interface InterfaceApprovision {
    ApprovisionDTO enregistrerApprovision(ApprovisionDTO approvisionDTO) throws ApprovisionDupicateException, EmptyException;
    ApprovisionDTO traiterApprovision(String designation) throws ApprovisionDupicateException, EmptyException, MontantQuantiteNullException, ProduitDupicateException, ApprovNotFoundException;

    ApprovisionDTO modifierApprov(ApprovisionDTO approvisionDTO) throws ApprovNotFoundException;
    ApprovisionDTO afficher(String idApprov) throws ApprovNotFoundException;

    List<ApprovisionDTO> listerApprovision();
}
