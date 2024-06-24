package com.desafio.conta.domain.specification;

import com.desafio.conta.domain.model.ContaEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class ContaSpecification {

    public static Specification<ContaEntity> descricaoContains(String name) {
        return (root, query, criteriaBuilder) -> name == null || name.equals("")  ? null : criteriaBuilder.like(root.get("descricao"), "%" + name + "%");
    }

    public static Specification<ContaEntity> dataVencimentoEquals(LocalDate dataVencimento) {
        return (root, query, criteriaBuilder) -> dataVencimento == null ? null : criteriaBuilder.equal(root.get("dataVencimento"), dataVencimento);
    }
}
