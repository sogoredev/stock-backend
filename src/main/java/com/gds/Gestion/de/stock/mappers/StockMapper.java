package com.gds.Gestion.de.stock.mappers;


import com.gds.Gestion.de.stock.DTOs.StockDTO;
import com.gds.Gestion.de.stock.entites.Stock;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockMapper {

    @Autowired
    private ProduitMapper produitMapper;

    public Stock mapDeDtoAStock(StockDTO stockDTO) {
        Stock stock = new Stock();
        BeanUtils.copyProperties(stockDTO, stock);
        stock.setProduitStock(produitMapper.mapDeDtoAProd(stockDTO.getProduitStockDTO()));
        return stock;
    }

    public StockDTO mapToStockDTO(Stock stock) {
        StockDTO stockDTO = new StockDTO();
        BeanUtils.copyProperties(stock, stockDTO);
        stockDTO.setProduitStockDTO(produitMapper.mapDeProdADto(stock.getProduitStock()));
        return stockDTO;
    }



}
