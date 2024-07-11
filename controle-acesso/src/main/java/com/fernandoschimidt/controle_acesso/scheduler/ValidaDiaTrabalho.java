package com.fernandoschimidt.controle_acesso.scheduler;

import com.fernandoschimidt.controle_acesso.services.RegistroPontoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Component
public class ValidaDiaTrabalho {


    private static final Logger logger = LoggerFactory.getLogger(ValidaDiaTrabalho.class);

    @Autowired
    private RegistroPontoService registroPontoService;

    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void checkDay() {
        LocalDate hoje = LocalDate.now();
        logger.info("Iniciando validação do dia de trabalho para {}", hoje);
        registroPontoService.validaDiaTrabalhado(hoje);
        logger.info("Validação do dia de trabalho concluída para {}", hoje);
    }
}