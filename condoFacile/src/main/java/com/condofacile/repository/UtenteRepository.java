package com.condofacile.repository;

import com.condofacile.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    Optional<Utente> findByEmail(String email);
    // Metodo per prendere tutti gli ID ordinati (se vuoi, ma puoi usare findAll + stream)
    List<Utente> findAllByOrderByIdAsc();
}