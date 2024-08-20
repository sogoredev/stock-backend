package com.gds.Gestion.de.stock.mappers;

import com.gds.Gestion.de.stock.DTOs.DetteDTO;
import com.gds.Gestion.de.stock.DTOs.ProduitDTO;
import com.gds.Gestion.de.stock.DTOs.VenteDTO;
import com.gds.Gestion.de.stock.entites.Dette;
import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Vente;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DetteMapper {

    @Autowired
    private ProduitMapper produitMapper;
    @Autowired
    private ClientMapper clientMapper;

    public Dette mapDeDtoADette(DetteDTO detteDTO) {
        Dette dette = new Dette();
        BeanUtils.copyProperties(detteDTO, dette);
        dette.setClientDette(clientMapper.mapDeDtoAClient(detteDTO.getClientDTO()));
        List<ProduitDTO> produitDTOList = detteDTO.getProduitDTOS();
        dette.setProduitDette(produitDTOList.stream().map(produitDTO -> produitMapper.mapDeDtoAProd(produitDTO)).collect(Collectors.toList()));
        return dette;
    }

    public DetteDTO mapDeDetteADTO(Dette dette) {
        DetteDTO detteDTO = new DetteDTO();
        BeanUtils.copyProperties(dette, detteDTO);
        detteDTO.setClientDTO(clientMapper.mapDeClientADto(dette.getClientDette()));
        List<Produit> produitList = dette.getProduitDette();
        detteDTO.setProduitDTOS(produitList.stream().map(produit -> produitMapper.mapDeProdADto(produit)).collect(Collectors.toList()));
        return detteDTO;
    }
}
