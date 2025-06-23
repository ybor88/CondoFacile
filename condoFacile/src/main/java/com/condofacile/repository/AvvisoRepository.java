package com.condofacile.repository;

import com.condofacile.entity.Avviso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvvisoRepository extends JpaRepository<Avviso, Integer> {
    // Puoi aggiungere metodi custom se necessario
}