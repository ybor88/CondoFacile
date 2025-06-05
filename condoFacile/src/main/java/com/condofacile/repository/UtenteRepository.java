package com.condofacile.repository;

import com.condofacile.entity.Utente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    @Query("SELECT u.appartamento FROM Utente u WHERE u.id = :id")
    String getCodiceAppartamentoById(@Param("id") Integer id);

    @Modifying
    @Transactional
    @Query("DELETE FROM Utente u WHERE u.ruolo = 'condomino'")
    int deleteAllCondomini();

    Optional<Utente> findByEmail(String email);  // metodo JPA standard
}