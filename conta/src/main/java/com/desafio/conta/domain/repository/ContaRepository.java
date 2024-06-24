package com.desafio.conta.domain.repository;

import com.desafio.conta.domain.model.ContaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContaRepository extends JpaRepository<ContaEntity, Long>, JpaSpecificationExecutor<ContaEntity> {

    @Query(value = "SELECT SUM(c.valor) FROM conta c WHERE c.data_pagamento BETWEEN :startDate AND :endDate", nativeQuery = true)
    Double getValorPeriodo(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT c.* FROM conta c WHERE c.data_pagamento is null and c.data_vencimento BETWEEN :startDate AND :endDate AND c.descricao LIKE %:descricao%", nativeQuery = true)
    List<ContaEntity> getContasPagar(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate ,  @Param("descricao") String descricao);
}