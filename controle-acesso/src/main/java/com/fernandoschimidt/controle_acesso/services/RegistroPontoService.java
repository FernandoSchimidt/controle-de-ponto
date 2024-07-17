package com.fernandoschimidt.controle_acesso.services;

import com.fernandoschimidt.controle_acesso.models.BancoHoras;
import com.fernandoschimidt.controle_acesso.models.Funcionario;
import com.fernandoschimidt.controle_acesso.models.RegistroPonto;
import com.fernandoschimidt.controle_acesso.repositories.BancoHorasRepository;
import com.fernandoschimidt.controle_acesso.repositories.FuncionarioRepository;
import com.fernandoschimidt.controle_acesso.repositories.RegistroPontoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistroPontoService {

    @Autowired
    private RegistroPontoRepository registroPontoRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private BancoHorasRepository bancoHorasRepository;
    private static final Logger logger = LoggerFactory.getLogger(RegistroPontoService.class);

    public List<RegistroPonto> listarTodosRegistrosColaborador(Long funcionarioID) {

        Optional<Funcionario> funcionario = funcionarioRepository.findById(funcionarioID);
        return registroPontoRepository.findAllByFuncionario(funcionario);
    }

    public List<RegistroPonto> getUltimosRegistrosDoDia(Long funcionarioId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return registroPontoRepository.findTop2ByFuncionarioIdAndHorarioBetweenOrderByHorarioDesc(
                funcionarioId, startOfDay, endOfDay
        );
    }

    public RegistroPonto registrarPonto(Long funcionarioId) {
        RegistroPonto.TipoRegistro tipoRegistro = determinarTipoRegistro(funcionarioId);
        if (tipoRegistro == null) {
            throw new IllegalStateException("Não é possível registrar o mesmo tipo de ponto mais de uma vez no mesmo dia.");
        }

        RegistroPonto registroPonto = new RegistroPonto();
        Funcionario funcionario = new Funcionario();
        funcionario.setId(funcionarioId);
        registroPonto.setFuncionario(funcionario);
        registroPonto.setHorario(LocalDateTime.now());
        registroPonto.setTipoRegistro(tipoRegistro);

        RegistroPonto registroSalvo = registroPontoRepository.save(registroPonto);
        calcularBancoHoras(funcionarioId);

        return registroSalvo;
    }

    private RegistroPonto.TipoRegistro determinarTipoRegistro(Long funcionarioId) {
        List<RegistroPonto> registros = registroPontoRepository.findByFuncionarioIdOrderByHorarioAsc(funcionarioId);
        LocalDate hoje = LocalDate.now();

        List<RegistroPonto> registrosHoje = registros.stream()
                .filter(registro -> registro.getHorario().toLocalDate().isEqual(hoje))
                .collect(Collectors.toList());

        if (registrosHoje.isEmpty()) {
            return RegistroPonto.TipoRegistro.ENTRADA;
        }

        RegistroPonto ultimoRegistro = registrosHoje.get(registrosHoje.size() - 1);
        RegistroPonto.TipoRegistro proximoTipoRegistro;

        switch (ultimoRegistro.getTipoRegistro()) {
            case ENTRADA:
                proximoTipoRegistro = RegistroPonto.TipoRegistro.SAIDA_ALMOCO;
                break;
            case SAIDA_ALMOCO:
                proximoTipoRegistro = RegistroPonto.TipoRegistro.RETORNO_ALMOCO;
                break;
            case RETORNO_ALMOCO:
                proximoTipoRegistro = RegistroPonto.TipoRegistro.SAIDA;
                break;
            case SAIDA:
                proximoTipoRegistro = RegistroPonto.TipoRegistro.ENTRADA;
                break;
            default:
                throw new IllegalStateException("Tipo de registro desconhecido: " + ultimoRegistro.getTipoRegistro());
        }

        boolean tipoJaRegistradoHoje = registrosHoje.stream()
                .anyMatch(registro -> registro.getTipoRegistro() == proximoTipoRegistro);

        return tipoJaRegistradoHoje ? null : proximoTipoRegistro;
    }

    private void calcularBancoHoras(Long funcionarioId) {
        List<RegistroPonto> registros = registroPontoRepository.findByFuncionarioIdOrderByHorarioAsc(funcionarioId);
        LocalDate hoje = LocalDate.now();

        List<RegistroPonto> registrosHoje = registros.stream()
                .filter(registro -> registro.getHorario().toLocalDate().isEqual(hoje))
                .collect(Collectors.toList());

        if (registrosHoje.size() < 4) {
            return; // Aguardando todos os registros do dia serem feitos
        }

        LocalDateTime entrada = registrosHoje.get(0).getHorario();
        LocalDateTime saidaAlmoco = registrosHoje.get(1).getHorario();
        LocalDateTime retornoAlmoco = registrosHoje.get(2).getHorario();
        LocalDateTime saida = registrosHoje.get(3).getHorario();

        Duration duracaoManha = Duration.between(entrada, saidaAlmoco);
        Duration duracaoTarde = Duration.between(retornoAlmoco, saida);
        Duration duracaoTotal = duracaoManha.plus(duracaoTarde);

        long minutosTrabalhados = duracaoTotal.toMinutes();
        long minutosEsperados = 8 * 60 + 30; // 8 horas e 30 minutos

        if (minutosTrabalhados > minutosEsperados) {
            long minutosExtras = minutosTrabalhados - minutosEsperados;
            double horasExtras = minutosExtras / 60.0;

            BancoHoras bancoHoras = new BancoHoras();
            Funcionario funcionario = new Funcionario();
            funcionario.setId(funcionarioId);
            bancoHoras.setFuncionario(funcionario);
            bancoHoras.setDia(hoje);
            bancoHoras.setHorasExtras(horasExtras);

            bancoHorasRepository.save(bancoHoras);
        }

    }

    public void validaDiaTrabalhado(LocalDate date) {
        List<Long> funcionariosIds = registroPontoRepository.findAll().stream()
                .map(RegistroPonto::getFuncionario)
                .map(Funcionario::getId)
                .distinct()
                .collect(Collectors.toList());

        for (Long funcionarioId : funcionariosIds) {
            List<RegistroPonto> registrosHoje = registroPontoRepository.findByFuncionarioIdAndHorarioBetween(
                    funcionarioId,
                    date.atStartOfDay(),
                    date.plusDays(1).atStartOfDay()
            );

            if (registrosHoje.size() < 4) {
                logger.warn("Funcionario {} não completou todos os registros de ponto para {}", funcionarioId, date);
                continue;
            }

            LocalDateTime entrada = registrosHoje.get(0).getHorario();
            LocalDateTime saidaAlmoco = registrosHoje.get(1).getHorario();
            LocalDateTime retornoAlmoco = registrosHoje.get(2).getHorario();
            LocalDateTime saida = registrosHoje.get(3).getHorario();

            Duration duracaoManha = Duration.between(entrada, saidaAlmoco);
            Duration duracaoTarde = Duration.between(retornoAlmoco, saida);
            Duration duracaoTotal = duracaoManha.plus(duracaoTarde);

            long minutosTrabalhados = duracaoTotal.toMinutes();
            long minutosEsperados = 8 * 60 + 30; // 8 horas e 30 minutos

            if (minutosTrabalhados > minutosEsperados) {
                long minutosExtras = minutosTrabalhados - minutosEsperados;
                double horasExtras = minutosExtras / 60.0;

                BancoHoras bancoHoras = new BancoHoras();
                Funcionario funcionario = new Funcionario();
                funcionario.setId(funcionarioId);
                bancoHoras.setFuncionario(funcionario);
                bancoHoras.setDia(date);
                bancoHoras.setHorasExtras(horasExtras);

                bancoHorasRepository.save(bancoHoras);
                logger.info("Horas extras adicionadas ao banco de horas do funcionario {} para {}", funcionarioId, date);
            }
            else{
                long minutosDevidos = minutosTrabalhados - minutosEsperados;
                double horasDevidas = minutosDevidos / 60.0;
                BancoHoras bancoHoras = new BancoHoras();
                Funcionario funcionario = new Funcionario();
                funcionario.setId(funcionarioId);
                bancoHoras.setFuncionario(funcionario);
                bancoHoras.setDia(date);
                bancoHoras.setHorasExtras(horasDevidas);

                bancoHorasRepository.save(bancoHoras);
                logger.info("Horas faltantes subtraidas do  banco de horas do funcionario {} para {}", funcionarioId, date);
            }
        }
    }


}