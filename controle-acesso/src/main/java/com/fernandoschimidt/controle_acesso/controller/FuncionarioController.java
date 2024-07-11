package com.fernandoschimidt.controle_acesso.controller;

import com.fernandoschimidt.controle_acesso.models.Funcionario;
import com.fernandoschimidt.controle_acesso.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping
    public ResponseEntity<Funcionario> salvar(@RequestBody Funcionario data) {

        Funcionario funcionario = funcionarioService.salvar(data);
        return ResponseEntity.ok(funcionario);
    }
}