package com.gds.Gestion.de.stock.repositories;

import com.gds.Gestion.de.stock.entites.Dette;
import com.gds.Gestion.de.stock.entites.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetteRepository extends JpaRepository<Dette, String> {
    Dette findByTitre(String string);
}
