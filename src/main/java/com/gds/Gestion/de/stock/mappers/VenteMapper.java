package com.gds.Gestion.de.stock.mappers;

import com.gds.Gestion.de.stock.DTOs.ProduitDTO;
import com.gds.Gestion.de.stock.DTOs.VenteDTO;
import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Vente;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenteMapper {

    @Autowired
    private ProduitMapper produitMapper;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private UtilisateurMapper utilisateurMapper;

    public Vente mapDeDtoAVente(VenteDTO venteDTO) {
        Vente vente = new Vente();
        BeanUtils.copyProperties(venteDTO, vente);
        vente.setClientsVente(clientMapper.mapDeDtoAClient(venteDTO.getClientDTO()));
        List<ProduitDTO> produitDTOList = venteDTO.getProduitsVend();
        vente.setProduitVente(produitDTOList
                .stream().map(produitDTO -> produitMapper.mapDeDtoAProd(produitDTO))
                .collect(Collectors.toList()));
        return vente;
    }

    public VenteDTO mapDeVenteADTO(Vente vente) {
        VenteDTO venteDTO = new VenteDTO();
        BeanUtils.copyProperties(vente, venteDTO);
//        venteDTO.setUtilisateurDTO(utilisateurMapper.mapDeUserADto(vente.getUtilisateurVente()));
        venteDTO.setClientDTO(clientMapper.mapDeClientADto(vente.getClientsVente()));
        List<Produit> produitList = vente.getProduitVente();
        venteDTO.setProduitsVend(produitList.stream().map(produit -> produitMapper.mapDeProdADto(produit)).collect(Collectors.toList()));
        return venteDTO;
    }
}
