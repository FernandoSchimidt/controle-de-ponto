import { Component, OnInit } from '@angular/core';
import { BancoHorasService } from '../../services/banco-horas.service';
import { RegistroPontoService } from '../../services/registro-ponto.service';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';

export interface Apontamento {
  id: number;
  funcionario: Funcionario;
  horario: string;
  tipoRegistro: string;
}
export interface Funcionario {
  id: number;
  nome: string;
  cargo: string;
  cpf: string;
  login: string;
}

@Component({
  selector: 'app-banco-horas',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './banco-horas.component.html',
  styleUrl: './banco-horas.component.scss',
})


export class BancoHorasComponent implements OnInit {
  apontamentos: Apontamento[] = [];
  diasDaSemana: string[] = ['Domingo', 'Segunda-feira', 'Terça-feira', 'Quarta-feira', 'Quinta-feira', 'Sexta-feira', 'Sábado'];
   tabela: any[] = [];
  // bancoHoras: any[] = [];
  funcionarioId = 1; // substituir com o ID real do funcionário
  constructor(private registroPontoService: RegistroPontoService) {}

  ngOnInit() {
    this.registroPontoService
      .getTodosRegistros(this.funcionarioId)
      .subscribe((data) => {
        this.tabela = data;
        console.log(this.tabela);
      });
  }
  organizarTabela(): void {
    const tabela: any = {};

    this.apontamentos.forEach(apontamento => {
      const diaSemana = new Date(apontamento.horario).toLocaleDateString('pt-BR', { weekday: 'long' });

      if (!tabela[diaSemana]) {
        tabela[diaSemana] = {
          entrada: '',
          saidaAlmoco: '',
          retornoAlmoco: '',
          saida: ''
        };
      }

      switch (apontamento.tipoRegistro) {
        case 'ENTRADA':
          tabela[diaSemana].entrada = new Date(apontamento.horario).toLocaleTimeString('pt-BR');
          break;
        case 'SAIDA_ALMOCO':
          tabela[diaSemana].saidaAlmoco = new Date(apontamento.horario).toLocaleTimeString('pt-BR');
          break;
        case 'RETORNO_ALMOCO':
          tabela[diaSemana].retornoAlmoco = new Date(apontamento.horario).toLocaleTimeString('pt-BR');
          break;
        case 'SAIDA':
          tabela[diaSemana].saida = new Date(apontamento.horario).toLocaleTimeString('pt-BR');
          break;
      }
    });

    this.diasDaSemana.forEach(dia => {
      this.tabela.push({
        diaSemana: dia,
        entrada: tabela[dia]?.entrada || '',
        saidaAlmoco: tabela[dia]?.saidaAlmoco || '',
        retornoAlmoco: tabela[dia]?.retornoAlmoco || '',
        saida: tabela[dia]?.saida || ''
      });
    });
  }
}
