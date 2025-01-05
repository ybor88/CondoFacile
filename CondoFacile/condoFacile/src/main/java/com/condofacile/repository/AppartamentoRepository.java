package com.condofacile.repository;

import com.condofacile.entity.Appartamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppartamentoRepository extends JpaRepository<Appartamento, Long> {
}