package com.gds.Gestion.de.stock.services;

import com.gds.Gestion.de.stock.DTOs.StockDTO;
import com.gds.Gestion.de.stock.exceptions.EmptyException;
import com.gds.Gestion.de.stock.exceptions.StockDupicateException;
import com.gds.Gestion.de.stock.exceptions.StockNotFoundException;

import java.util.List;

public interface InterfaceStock {

    StockDTO enregistrerStock(StockDTO stockDTO) throws StockDupicateException, StockNotFoundException, EmptyException;
    StockDTO modifierStock(StockDTO stockDTO) throws EmptyException;
    StockDTO enregistrerVenteStock(StockDTO stockDTO);
    StockDTO enregistrerApprovStock(StockDTO stockDTO);
    void suppressionStock(String idStoack) throws StockNotFoundException;
    StockDTO afficherStock(String idStock) throws StockNotFoundException;
    List<StockDTO> listerStock();
}
