package com.fernandoschimidt.controle_acesso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControleAcessoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleAcessoApplication.class, args);
	}

}
