package com.fernandoschimidt.controle_acesso.repositories;

import com.fernandoschimidt.controle_acesso.models.BancoHoras;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BancoHorasRepository extends JpaRepository<BancoHoras, Long> {
    List<BancoHoras> findByFuncionarioId(Long funcionarioId);
}