package com.desafio.conta.interfaces.rest;

import com.desafio.conta.application.ContaService;
import com.desafio.conta.domain.model.ContaEntity;
import com.desafio.conta.interfaces.dto.ContaDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contas")
@Validated
public class ContaController {

    @Autowired
    private ContaService contaService;

    @GetMapping
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public Page<ContaEntity> getAllContas(
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) LocalDate dataVencimento,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return contaService.findAll(descricao, dataVencimento, pageable);
    }

    @GetMapping("/valor-total-pago/{dataInicio}/{dataFim}")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<Double> getValorPeriodo(@PathVariable String dataInicio, @PathVariable String dataFim) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(dataInicio, formatter);
        LocalDate end = LocalDate.parse(dataFim, formatter);
        Double sum = contaService.getValorPeriodo(start, end);

        return ResponseEntity.ok(sum);
    }

    @GetMapping("/contas-apagar")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public List<ContaEntity> getContasApagar(@RequestParam(required = true) String dataInicio,
                                             @RequestParam(required = true) String dataFim,
                                             @RequestParam(defaultValue = "") String descricao) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(dataInicio, formatter);
        LocalDate end = LocalDate.parse(dataFim, formatter);
        List<ContaEntity>  contas = contaService.getContasPagar(start, end , descricao);

        return contas;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<ContaEntity> getAccountById(@PathVariable Long id) {
        Optional<ContaEntity> account = contaService.findById(id);
        return account.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ContaEntity createConta(@Valid @RequestBody ContaDto contaDto) {
        return contaService.save(contaDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaEntity> updateConta(@PathVariable Long id, @Valid @RequestBody ContaDto contaDto) {
        return ResponseEntity.ok(contaService.update(id, contaDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasRole('user')")
    public ResponseEntity<Void> deleteConta(@PathVariable Long id) {
        if (contaService.findById(id).isPresent()) {
            contaService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/import", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> importContas(@RequestPart("file") MultipartFile file) {
        contaService.importCSV(file);
        return ResponseEntity.ok().build();
    }

}
