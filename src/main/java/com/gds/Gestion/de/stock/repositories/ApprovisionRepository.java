package com.gds.Gestion.de.stock.repositories;

import com.gds.Gestion.de.stock.entites.Approvision;
import com.gds.Gestion.de.stock.entites.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovisionRepository extends JpaRepository<Approvision, String> {
    Approvision findByDesignation(String designation);
}
