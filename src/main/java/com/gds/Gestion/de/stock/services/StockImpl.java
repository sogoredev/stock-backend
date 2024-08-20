package com.gds.Gestion.de.stock.services;

import com.gds.Gestion.de.stock.DTOs.StockDTO;
import com.gds.Gestion.de.stock.entites.Produit;
import com.gds.Gestion.de.stock.entites.Stock;
import com.gds.Gestion.de.stock.entites.Utilisateur;
import com.gds.Gestion.de.stock.exceptions.EmptyException;
import com.gds.Gestion.de.stock.exceptions.StockDupicateException;
import com.gds.Gestion.de.stock.exceptions.StockNotFoundException;
import com.gds.Gestion.de.stock.mappers.StockMapper;

import com.gds.Gestion.de.stock.repositories.StockRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class StockImpl implements InterfaceStock{

    private StockRepository stockRepository;

    private StockMapper stockMapper;

    @Override
    public StockDTO enregistrerStock(StockDTO stockDto) throws StockDupicateException, EmptyException {
        Stock stock = stockMapper.mapDeDtoAStock(stockDto);
        Stock stockDesg = stockRepository.findByDesignation(stock.getDesignation());
        Produit produit = stock.getProduitStock();

        if (stockDto.getDesignation().isEmpty() || stockDto.getNote().isEmpty() || stockDto.getMontant() <= 0 || stockDto.getQuantite() <= 0)
            throw new EmptyException("Remplissez les champs vides");

        Stock stockExist = stockRepository.findByDesignation(stockDto.getDesignation());
        if (stockExist != null)
            throw new EmptyException(stockExist.getDesignation()+" existe deja.");

        Stock stockProd = stockRepository.findStockByProduitStockIdProd(produit.getIdProd());
        if (stockDesg != null)
            throw new EmptyException(stockProd.getDesignation()+" n'est pas lie au produit.");

        else if (stockProd != null) {
            // Mise à jour de la quantité et du montant
            stockProd.setQuantite(stockProd.getQuantite() + stock.getQuantite());
            stockProd.setMontant(stockProd.getMontant() + stock.getMontant());
            stockProd.setDate(LocalDate.now());
            return stockMapper.mapToStockDTO(stockRepository.save(stockProd));
        }else {
            String uniqueId;
            do {
                uniqueId = "GDS"+UUID.randomUUID();
            } while (stockRepository.existsById(uniqueId));

            stock.setIdStock(uniqueId);
            stock.setDate(LocalDate.now());
            Utilisateur userConnecter = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            stock.setUtilisateurStock(userConnecter);
            return stockMapper.mapToStockDTO(stockRepository.save(stock));
        }
    }




    @Override
    public StockDTO modifierStock(StockDTO stockDTO) throws EmptyException {

        Stock existingStock = stockRepository.findById(stockDTO.getIdStock())
                .orElseThrow(() -> new EmptyException("Stock n'existe pas"));

        Stock stock = stockMapper.mapDeDtoAStock(stockDTO);
        stock.setIdStock(existingStock.getIdStock());
        stock.setDate(existingStock.getDate());
        return stockMapper.mapToStockDTO(stockRepository.save(stock));
    }


    @Override
    public StockDTO enregistrerVenteStock(StockDTO stockDTO) {
        Stock stock = stockMapper.mapDeDtoAStock(stockDTO);
        Utilisateur userConnecter = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        stock.setUtilisateurStock(userConnecter);
        return stockMapper.mapToStockDTO(stockRepository.save(stock));
    }

    @Override
    public StockDTO enregistrerApprovStock(StockDTO stockDTO) {
        Stock stock = stockMapper.mapDeDtoAStock(stockDTO);
        Utilisateur userConnecter = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        stock.setUtilisateurStock(userConnecter);
        return stockMapper.mapToStockDTO(stockRepository.save(stock));
    }

    @Override
    public void suppressionStock(String idStoack) throws StockNotFoundException {
        StockDTO stockDTO = afficherStock(idStoack);
        if (stockDTO == null){
            throw new StockNotFoundException(stockDTO.getDesignation()+ "n'existe pas");
        }
        stockRepository.deleteById(idStoack);

    }

    @Override
    public StockDTO afficherStock(String idStock) throws StockNotFoundException {
        return stockMapper.mapToStockDTO(stockRepository.findById(idStock).orElseThrow(()-> new StockNotFoundException("Stock does not exist")));
    }

    @Override
    public List<StockDTO> listerStock() {
         List<Stock> stocksList = stockRepository.findAll();
        return stocksList.stream()
                .map(stock -> stockMapper.mapToStockDTO(stock)).collect(Collectors.toList());
    }
}
