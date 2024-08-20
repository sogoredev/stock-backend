package com.gds.Gestion.de.stock.repositories;


import com.gds.Gestion.de.stock.entites.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByTelephoneOrEmail(String tel, String email);
    Utilisateur findByEmail(String email);
}
