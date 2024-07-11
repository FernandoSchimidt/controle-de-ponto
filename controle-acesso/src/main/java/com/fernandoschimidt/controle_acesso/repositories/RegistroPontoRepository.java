package com.fernandoschimidt.controle_acesso.repositories;

import com.fernandoschimidt.controle_acesso.models.Funcionario;
import com.fernandoschimidt.controle_acesso.models.RegistroPonto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RegistroPontoRepository extends JpaRepository<RegistroPonto, Long> {
    List<RegistroPonto> findByFuncionarioIdOrderByHorarioAsc(Long funcionarioId);
    List<RegistroPonto> findByFuncionarioIdAndHorarioBetween(Long funcionarioId, LocalDateTime start, LocalDateTime end);
    List<RegistroPonto> findAllByFuncionario(Optional<Funcionario> funcionario);
}