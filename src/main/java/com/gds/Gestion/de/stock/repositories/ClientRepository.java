package com.gds.Gestion.de.stock.repositories;

import com.gds.Gestion.de.stock.entites.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ClientRepository extends JpaRepository<Client, String> {
    Client findByTelephone(String telephone);

    @Query(value = "SELECT COUNT(*) FROM client", nativeQuery = true)
    long countTotalClient();
}
