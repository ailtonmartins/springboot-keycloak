package com.desafio.conta.application;

import com.desafio.conta.domain.enums.SituacaoEnum;
import com.desafio.conta.domain.model.ContaEntity;
import com.desafio.conta.domain.specification.ContaSpecification;
import com.desafio.conta.interfaces.dto.ContaDto;
import com.desafio.conta.domain.repository.ContaRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    public Page<ContaEntity> findAll(String descricao, LocalDate dataVencimento, Pageable pageable) {
        Specification<ContaEntity> spec = Specification.where(ContaSpecification.descricaoContains(descricao))
                .and(ContaSpecification.dataVencimentoEquals(dataVencimento));
        return contaRepository.findAll(spec, pageable);
    }

    public List<ContaEntity> getContasPagar(LocalDate startDate, LocalDate endDate , String descricao) {
        return contaRepository.getContasPagar(startDate, endDate, descricao);
    }

    public Double getValorPeriodo(LocalDate startDate, LocalDate endDate ) {
        return contaRepository.getValorPeriodo(startDate, endDate);
    }

    public Optional<ContaEntity> findById(Long id) {
        return contaRepository.findById(id);
    }

    public ContaEntity save(ContaDto contaDto) {
        ContaEntity conta = new ContaEntity(null, contaDto.getDescricao(), contaDto.getValor(),contaDto.getDataVencimento(), contaDto.getDataPagamento(), contaDto.getSituacao());
        conta.setDescricao(contaDto.getDescricao());
        conta.setValor(contaDto.getValor());
        if( contaDto.getDataPagamento() != null ){
            conta.setDataPagamento( contaDto.getDataPagamento() );
        }
        conta.setDataVencimento( contaDto.getDataVencimento() );
        conta.setSituacao( contaDto.getSituacao() );
        return contaRepository.save(conta);
    }

    public ContaEntity update(Long id, ContaDto contatDTO) {
        Optional<ContaEntity> optionalAccount = contaRepository.findById(id);
        if (optionalAccount.isPresent()) {
            ContaEntity conta = optionalAccount.get();
            conta.setDescricao(contatDTO.getDescricao());
            conta.setValor(contatDTO.getValor());
            conta.setDataPagamento( contatDTO.getDataPagamento() );
            conta.setDataVencimento( contatDTO.getDataVencimento() );
            conta.setSituacao( contatDTO.getSituacao() );
            return contaRepository.save(conta);
        } else {
            throw new RuntimeException("Conta not found");
        }
    }

    public void deleteById(Long id) {
        contaRepository.deleteById(id);
    }

    public void importCSV(MultipartFile file) {
        try {
            BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

            List<ContaEntity> accounts = new ArrayList<>();

            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");


            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (CSVRecord csvRecord : csvRecords) {
                ContaEntity conta = new ContaEntity(
                        null,
                        csvRecord.get("descricao"),
                        Double.parseDouble(csvRecord.get("valor")),
                        LocalDate.parse(csvRecord.get("data_vencimento"),pattern),
                        LocalDate.parse(csvRecord.get("data_pagamento"),pattern),
                        SituacaoEnum.valueOf(csvRecord.get("situacao"))
                );

                accounts.add(conta);
            }

            contaRepository.saveAll(accounts);
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
