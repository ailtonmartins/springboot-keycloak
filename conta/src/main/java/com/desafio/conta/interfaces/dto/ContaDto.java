package com.desafio.conta.interfaces.dto;

import com.desafio.conta.domain.enums.SituacaoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContaDto {

    @NotBlank(message = "Descricao é obrigratorio")
    private String descricao;

    @NotNull(message = "Valor é obrigatirio")
    private Double valor;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Data Vencimento é obrigatirio")
    private LocalDate dataVencimento;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataPagamento = null;

    @NotNull(message = "Situacao é obrigatirio")
    private SituacaoEnum situacao;
}
