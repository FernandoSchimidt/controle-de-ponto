package com.fernandoschimidt.controle_acesso.models;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class RegistroPonto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    private LocalDateTime horario;

    @Enumerated(EnumType.STRING)
    private TipoRegistro tipoRegistro;

    public enum TipoRegistro {
        ENTRADA, SAIDA_ALMOCO, RETORNO_ALMOCO, SAIDA
    }
}