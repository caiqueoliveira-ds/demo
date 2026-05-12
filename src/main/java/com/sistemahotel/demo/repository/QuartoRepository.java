package com.sistemahotel.demo.repository;

import com.sistemahotel.demo.entity.Quarto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuartoRepository extends JpaRepository<Quarto, Long> {
    boolean existsByNumero(Integer numero);
}