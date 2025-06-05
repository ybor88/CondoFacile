package com.condofacile.repository;

import com.condofacile.entity.Appartamento;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppartamentoRepository extends JpaRepository<Appartamento, Long> {

    @Query("SELECT a FROM Appartamento a WHERE a.occupato = false")
    List<Appartamento> findAvailableAppartamenti();

    @Modifying
    @Transactional
    @Query("UPDATE Appartamento a SET a.occupato = true WHERE a.codice = :codiceAppartamento")
    int setOccupatoTrueByCodice(String codiceAppartamento);

    @Modifying
    @Transactional
    @Query("UPDATE Appartamento a SET a.occupato = false WHERE a.codice = :codiceAppartamento")
    int setOccupatoFalseByCodice(String codiceAppartamento);

    @Modifying
    @Transactional
    @Query("DELETE FROM Appartamento a WHERE a.occupato = true")
    int deleteAllOccupatoTrue();

}