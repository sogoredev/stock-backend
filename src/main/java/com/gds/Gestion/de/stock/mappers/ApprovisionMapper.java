package com.gds.Gestion.de.stock.mappers;

import com.gds.Gestion.de.stock.DTOs.ApprovisionDTO;
import com.gds.Gestion.de.stock.DTOs.ProduitDTO;
import com.gds.Gestion.de.stock.entites.Approvision;
import com.gds.Gestion.de.stock.entites.Produit;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ApprovisionMapper {

    private UtilisateurMapper utilisateurMapper;
    private ProduitMapper produitMapper;

    public Approvision mapDeDtoAApprov(ApprovisionDTO approvisionDTO){
        Approvision approvision = new Approvision();
        BeanUtils.copyProperties(approvisionDTO, approvision);
        List<ProduitDTO> produitDTOList = approvisionDTO.getProduitsApprov();
        approvision.setProduitsApprov(produitDTOList
                .stream().map(produitDTO -> produitMapper.mapDeDtoAProd(produitDTO))
                .collect(Collectors.toList()));
        return approvision;
    }

    public ApprovisionDTO mapDeApprovADto(Approvision approvision){
        ApprovisionDTO approvisionDTO = new ApprovisionDTO();
        BeanUtils.copyProperties(approvision, approvisionDTO);
        List<Produit> produitDTOList = approvision.getProduitsApprov();
        approvisionDTO.setProduitsApprov(produitDTOList
                .stream().map(produit -> produitMapper.mapDeProdADto(produit))
                .collect(Collectors.toList()));
        return approvisionDTO;
    }
}
