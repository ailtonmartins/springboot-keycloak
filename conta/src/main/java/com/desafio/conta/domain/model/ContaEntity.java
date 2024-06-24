package com.desafio.conta.domain.model;


import com.desafio.conta.domain.enums.SituacaoEnum;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ContaEntity")
@Table(name = "conta")
public class ContaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="descricao")
    private String descricao;

    @Column(name="valor")
    private Double valor;

    @Column(name="data_vencimento")
    private LocalDate dataVencimento;

    @Column(name="data_pagamento")
    private LocalDate dataPagamento;

    @Column(name="situacao")
    @Enumerated(EnumType.STRING)
    private SituacaoEnum situacao;

}
