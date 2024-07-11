package com.fernandoschimidt.controle_acesso.controller;
import com.fernandoschimidt.controle_acesso.models.RegistroPonto;
import com.fernandoschimidt.controle_acesso.services.RegistroPontoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/registros-ponto")
public class RegistroPontoController {

    @Autowired
    private RegistroPontoService registroPontoService;

    @GetMapping("/{funcionarioId}")
    public List<RegistroPonto> listarTodosRegistrosColaborador(@PathVariable Long funcionarioId) {
        return registroPontoService.listarTodosRegistrosColaborador(funcionarioId);
    }

    @PostMapping("/registrar/{funcionarioId}")
    public RegistroPonto registrarPonto(@PathVariable Long funcionarioId) {
        try {
            return registroPontoService.registrarPonto(funcionarioId);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}