package com.desafio.conta.interfaces.rest;


import com.desafio.conta.application.ContaService;
import com.desafio.conta.domain.enums.SituacaoEnum;
import com.desafio.conta.domain.model.ContaEntity;
import com.desafio.conta.domain.repository.ContaRepository;
import com.desafio.conta.interfaces.dto.ContaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.BDDMockito.given;

@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

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
    void testGetAccountById() throws Exception {
        when(contaService.findById(1L)).thenReturn(Optional.of(conta1));

        mockMvc.perform(get("/contas/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste A"));

        verify(contaService, times(1)).findById(1L);
    }

    @Test
    void testCreateAccount() throws Exception {
        ContaDto contaDto = new ContaDto();
        contaDto.setDescricao("John Doe");
        contaDto.setValor(1000.0);

        when(contaService.save(any(ContaDto.class))).thenReturn(conta1);

        mockMvc.perform(post("/contas")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\": \"Teste A\", \"valor\": 1000.0, \"situacao\": \"ATIVO\", \"dataVencimento\":\"2024-01-03\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste A"));

        verify(contaService, times(1)).save(any(ContaDto.class));
    }

    @Test
    void testUpdateAccount() throws Exception {
        ContaDto contaDto = new ContaDto();
        contaDto.setDescricao("John Doe");
        contaDto.setValor(1000.0);

        when(contaService.update(any(Long.class), any(ContaDto.class))).thenReturn(conta1);

        mockMvc.perform(put("/contas/1")
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\": \"Teste A\", \"valor\": 1000.0, \"situacao\": \"ATIVO\", \"dataVencimento\":\"2024-01-03\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Teste A"));

        verify(contaService, times(1)).update(any(Long.class), any(ContaDto.class));
    }

    @Test
    void testDeleteAccount() throws Exception {
        when(contaService.findById(1L)).thenReturn(Optional.of(conta1));

        mockMvc.perform(delete("/contas/1").with(SecurityMockMvcRequestPostProcessors.jwt()
                        .authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(contaService, times(1)).findById(1L);
        verify(contaService, times(1)).deleteById(1L);
    }

    @Test
    void testImportAccounts() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "accounts.csv", "text/csv", "name,balance\nJohn Doe,1000.0\nJane Doe,2000.0".getBytes());

        mockMvc.perform(multipart("/contas/import")
                        .file(file).with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isOk());

        verify(contaService, times(1)).importCSV(any(MultipartFile.class));
    }


}
