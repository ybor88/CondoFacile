package com.condofacile.repository;

import com.condofacile.entity.Bolletta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BollettaRepository extends JpaRepository<Bolletta, Integer> {
    List<Bolletta> findByUtenteId(Integer utenteId);
}