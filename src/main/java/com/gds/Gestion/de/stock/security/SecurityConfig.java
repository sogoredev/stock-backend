package com.gds.Gestion.de.stock.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private JwtFilter jwtFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserDetailsService userDetailsService;

    public SecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilter jwtFilter, UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/login").permitAll()

                        .requestMatchers(POST,"/user/creer").hasRole("SUPER_ADMIN")
                        .requestMatchers(PUT, "/user/modifier/{idUser}").hasRole("SUPER_ADMIN")
                        .requestMatchers(DELETE, "/user/supprimer/{idUser}").hasRole("SUPER_ADMIN")
                        .requestMatchers(GET, "/user/{idUser}").hasRole("SUPER_ADMIN")
                        .requestMatchers(GET, "/user/userListe").hasRole("SUPER_ADMIN")
                        .requestMatchers(GET, "/user/userListe").hasRole("SUPER_ADMIN")
                        .requestMatchers(GET, "/user/roleListe").hasRole("SUPER_ADMIN")

                        .requestMatchers(POST, "/categorie/creerCat").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(PUT, "/categorie/modifierCat/{idCat}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(DELETE, "/categorie/supprimerCat/{idCat}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/categorie/afficherCat/{idCat}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/categorie/listeCat").hasAnyRole("SUPER_ADMIN", "ADMIN")

                        .requestMatchers(POST, "/produit/enregistrerProd").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(PUT, "/produit/modifierProd/{idProd}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(DELETE, "/produit/supprimerProd/{idProd}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/produit/afficherProd/{idProd}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/produit/listeProd").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/produit/afficherProdSansStock").hasAnyRole("SUPER_ADMIN", "ADMIN")

                        .requestMatchers(POST, "/stock/enregistrerStock").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(PUT, "/stock/modifierStock/{idStock}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(DELETE, "/stock/supprimerStock/{idStock}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/stock/afficherStock/{idStock}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/stock/listStock").hasAnyRole("SUPER_ADMIN", "ADMIN")

                        .requestMatchers(POST, "/vente/effectuerVente").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(PUT, "/vente/modifier/{idVente}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(DELETE, "/vente/{idVente}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/vente/afficherVente/{idVente}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/vente/listeVente").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER")
                        .requestMatchers(GET, "/vente/totalVente").hasAnyRole("SUPER_ADMIN", "ADMIN", "USER")

                        .requestMatchers(POST, "/client/ajouterClient").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/client/listeClient").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/client/totalClient").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(PUT, "/client/modifierClient/{idClient}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/client/afficherClient/{idClient}").hasAnyRole("SUPER_ADMIN", "ADMIN")

                        .requestMatchers(POST, "/approvision/creerApprov").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(POST, "/approvision/traiterApprov").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/approvision/listeApprov").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(PUT, "/approvision/modifierApprov/{idApprov}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/approvision/afficherApprov/{idApprov}").hasAnyRole("SUPER_ADMIN", "ADMIN")

                        .requestMatchers(POST, "/dette/enregistrerDette").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/dette/listeDette").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(PUT, "/dette/modifierDette/{idDette}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(GET, "/dette/afficherDette/{idDette}").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers(POST, "/dette/payerDette").hasAnyRole("SUPER_ADMIN", "ADMIN")

                        .anyRequest().authenticated())
                        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
         DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }


}
