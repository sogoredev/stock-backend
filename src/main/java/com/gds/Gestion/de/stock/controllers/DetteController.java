package com.gds.Gestion.de.stock.controllers;

import com.gds.Gestion.de.stock.DTOs.ApprovisionDTO;
import com.gds.Gestion.de.stock.DTOs.DetteDTO;
import com.gds.Gestion.de.stock.DTOs.DetteDTO;
import com.gds.Gestion.de.stock.DTOs.VenteDTO;
import com.gds.Gestion.de.stock.exceptions.*;
import com.gds.Gestion.de.stock.services.InterfaceDette;
import com.gds.Gestion.de.stock.services.InterfaceVente;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/dette")
public class DetteController {

    private InterfaceDette interfaceDette;

    @PostMapping("/enregistrerDette")
    private DetteDTO effectuerDette(@Valid @RequestBody DetteDTO detteDTO) throws Exception {
        return interfaceDette.enregistrerDette(detteDTO);
    }

    @PostMapping("/payerDette")
    private DetteDTO payerDette(@Valid @RequestBody String idDette) throws EmptyException, MontantQuantiteNullException, ProduitDupicateException, DetteNotFoundException {
        return interfaceDette.paiementDette(idDette);
    };

    @PutMapping("/modifierDette/{idDette}")
    private DetteDTO update(@Valid @RequestBody DetteDTO DetteDTO, @PathVariable("idDette") String idDette) throws DetteNotFoundException, ApprovNotFoundException, EmptyException {
        DetteDTO.setIdDette(idDette);
        return interfaceDette.modifierDette(DetteDTO);
    }

    @GetMapping("/afficherDette/{idDette}")
    private DetteDTO afficher(@Valid @PathVariable("idDette") String idDette) throws DetteNotFoundException {
        return interfaceDette.afficher(idDette);
    }


    @GetMapping("/listeDette")
    private List<DetteDTO> listeVente() {
        return interfaceDette.listerDette();
    }
}
