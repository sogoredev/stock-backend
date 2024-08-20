package com.gds.Gestion.de.stock.services;


import com.gds.Gestion.de.stock.DTOs.UtilisateurDTO;
import com.gds.Gestion.de.stock.entites.UserRole;
import com.gds.Gestion.de.stock.entites.Utilisateur;
import com.gds.Gestion.de.stock.enums.TypeActive;
import com.gds.Gestion.de.stock.enums.TypeAuth;
import com.gds.Gestion.de.stock.exceptions.EmailIncorrectException;
import com.gds.Gestion.de.stock.exceptions.UtilisateurDuplicateException;
import com.gds.Gestion.de.stock.exceptions.EmptyException;
import com.gds.Gestion.de.stock.exceptions.UtilisateurNotFoundException;
import com.gds.Gestion.de.stock.mappers.UtilisateurMapper;
import com.gds.Gestion.de.stock.repositories.UserRoleRepository;
import com.gds.Gestion.de.stock.repositories.UtilisateurRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class UtilisateurImpl implements UserDetailsService {

    private UtilisateurRepository utilisateurRepository;
    private UserRoleRepository userRoleRepository;

    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    
    public UtilisateurDTO creerUser(UtilisateurDTO utilisateurDTO) throws UtilisateurDuplicateException, EmptyException, EmailIncorrectException {
        Utilisateur utilisateur = utilisateurMapper.mapDeDtoAUser(utilisateurDTO);
        Utilisateur utilisateurNew = utilisateurRepository.findByTelephoneOrEmail(utilisateur.getTelephone(), utilisateur.getEmail());

        if (utilisateurDTO.getEmail().isEmpty() || utilisateurDTO.getPassword().isEmpty() || utilisateurDTO.getTelephone().isEmpty())
            throw new EmptyException("Remplissez les champs vides");

        if (utilisateurNew != null)
            throw new UtilisateurDuplicateException("Cet "+utilisateurNew.getEmail()+" ou"+utilisateurNew.getTelephone()+" existe déjà");

        if (!utilisateur.getEmail().contains("@") && !utilisateur.getEmail().contains("."))
            throw new EmailIncorrectException(utilisateur.getEmail()+" est incorrect");

        List<UserRole> roles = utilisateurDTO.getRoles().stream()
                .map(roleDTO -> userRoleRepository.findByName(roleDTO.getName()))
                .collect(Collectors.toList());
        utilisateur.setRoles(roles);
        utilisateur.setDate(LocalDate.now());
        utilisateur.setActivation(TypeActive.ACTIVER);
        utilisateur.setAuthentification(TypeAuth.FALSE);
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        Utilisateur utilisateurSave = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.mapDeUserADto(utilisateurSave);
    }

    
    public List<UtilisateurDTO> afficherUsers() {
        List<Utilisateur> utilisateursList = utilisateurRepository.findAll();
        return utilisateursList.stream()
                .map(utilisateur -> utilisateurMapper.mapDeUserADto(utilisateur))
                .collect(Collectors.toList());
    }

    
    public UtilisateurDTO afficherUsersId(Long idUser) throws UtilisateurNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findById(idUser)
                .orElseThrow(() -> new UtilisateurNotFoundException(idUser+" de l'utilisateur n'existe pas"));
        return utilisateurMapper.mapDeUserADto(utilisateur);
    }

    
    public UtilisateurDTO modifierUser(UtilisateurDTO utilisateurDTO) {
        Utilisateur utilisateur = utilisateurMapper.mapDeDtoAUser(utilisateurDTO);
        Utilisateur saveUser = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.mapDeUserADto(saveUser);
    }

    
    public String supprimerUser(Long idUser) {
        utilisateurRepository.deleteById(idUser);
        return "Utilisateur supprimé";
    }


    @Override
    public Utilisateur loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.utilisateurRepository.findByEmail(email);
    }
}
