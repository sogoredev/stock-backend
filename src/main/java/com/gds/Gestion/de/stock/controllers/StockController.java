package com.gds.Gestion.de.stock.controllers;

import com.gds.Gestion.de.stock.DTOs.StockDTO;
import com.gds.Gestion.de.stock.exceptions.EmptyException;
import com.gds.Gestion.de.stock.exceptions.StockDupicateException;
import com.gds.Gestion.de.stock.exceptions.StockNotFoundException;
import com.gds.Gestion.de.stock.services.InterfaceStock;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/stock")
public class StockController {

    private InterfaceStock interfaceStock;

    @PostMapping("/enregistrerStock")
    private StockDTO stockDTO(@RequestBody StockDTO stockDTO) throws StockDupicateException, StockNotFoundException, EmptyException {
       return interfaceStock.enregistrerStock(stockDTO);
    }

    @PutMapping("/modifierStock/{idStock}")
    private StockDTO modifierStock(@RequestBody StockDTO stockDTO, @PathVariable("idStock") String idStock ) throws EmptyException {
        stockDTO.setIdStock(idStock);
      return interfaceStock.modifierStock(stockDTO);
    }

    @DeleteMapping("/supprimerStock/{idStock}")
    private void supprimerStock(@PathVariable String idStock) throws StockNotFoundException {
       interfaceStock.suppressionStock(idStock);
    }

    @GetMapping("/afficherStock/{idStock}")
    private StockDTO afficherStock(@PathVariable("idStock") String idStock) throws StockNotFoundException {
        return interfaceStock.afficherStock(idStock);
    }

    @GetMapping("listStock")
    private List<StockDTO> stockDTOList(){
        return interfaceStock.listerStock();
    }




}
