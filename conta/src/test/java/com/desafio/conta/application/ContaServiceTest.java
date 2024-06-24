package com.desafio.conta.application;

import com.desafio.conta.domain.enums.SituacaoEnum;
import com.desafio.conta.domain.model.ContaEntity;
import com.desafio.conta.domain.specification.ContaSpecification;
import com.desafio.conta.interfaces.dto.ContaDto;
import com.desafio.conta.domain.repository.ContaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    private ContaEntity conta1;
    private ContaEntity conta2;

    Pageable pageable = null;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        conta1 = new ContaEntity(1L, "Teste A", 1000.0, LocalDate.now(), LocalDate.now(), SituacaoEnum.ATIVO);
        conta2 = new ContaEntity(2L, "Teste B", 250.0, LocalDate.now(), LocalDate.now(), SituacaoEnum.ATIVO);
        pageable = PageRequest.of(0, 10);
    }


    @Test
    void testFindAll() {

        List<ContaEntity> contas = Arrays.asList(conta1, conta2);

        Page<ContaEntity> page = new PageImpl<>(contas, pageable, contas.size());

        when(contaRepository.findAll(any(Specification.class),any(Pageable.class))).thenReturn(page);

        Page<ContaEntity> data = contaService.findAll("",null,pageable);

        assertEquals(2, data.getContent().size());
        verify(contaRepository, times(1)).findAll(any(Specification.class),any(Pageable.class));
    }

    @Test
    void testFindById() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta1));

        Optional<ContaEntity> foundAccount = contaService.findById(1L);
        assertTrue(foundAccount.isPresent());
        assertEquals("Teste A", foundAccount.get().getDescricao());
        verify(contaRepository, times(1)).findById(1L);
    }

    @Test
    void testSave() {

        when(contaRepository.save(any(ContaEntity.class))).thenReturn(conta1);

        ContaDto contaDto = new ContaDto();
        contaDto.setDescricao("Teste A");
        contaDto.setValor(1000.0);
        contaDto.setDataVencimento(LocalDate.now());
        contaDto.setSituacao(SituacaoEnum.ATIVO);

        ContaEntity savedAccount = contaService.save(contaDto);
        assertNotNull(savedAccount.getId());
        assertEquals("Teste A", savedAccount.getDescricao());
        verify(contaRepository, times(1)).save(any(ContaEntity.class));
    }

    @Test
    void testDeleteById() {
        contaService.deleteById(1L);
        verify(contaRepository, times(1)).deleteById(1L);
    }
}
