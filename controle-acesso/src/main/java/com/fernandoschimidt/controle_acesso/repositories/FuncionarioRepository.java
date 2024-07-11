package com.fernandoschimidt.controle_acesso.repositories;

import com.fernandoschimidt.controle_acesso.models.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
}