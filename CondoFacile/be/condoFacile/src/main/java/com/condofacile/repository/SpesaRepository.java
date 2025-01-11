package com.condofacile.repository;

import com.condofacile.entity.Spesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpesaRepository extends JpaRepository<Spesa, Long> {
}