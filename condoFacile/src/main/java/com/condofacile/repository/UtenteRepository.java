package com.condofacile.repository;

import com.condofacile.entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtenteRepository extends JpaRepository<Utente, Integer> {

    @Query("SELECT u.appartamento FROM Utente u WHERE u.id = :id")
    String getCodiceAppartamentoById(@Param("id") Integer id);
}