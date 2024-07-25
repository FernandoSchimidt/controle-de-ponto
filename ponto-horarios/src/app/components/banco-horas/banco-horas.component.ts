import { Component, OnInit } from '@angular/core';
import { BancoHorasService } from '../../services/banco-horas.service';
import { RegistroPontoService } from '../../services/registro-ponto.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-banco-horas',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './banco-horas.component.html',
  styleUrl: './banco-horas.component.scss',
})
export class BancoHorasComponent implements OnInit {
  bancoHoras: any[] = [];
  funcionarioId = 1; // substituir com o ID real do funcionário
  constructor(private registroPontoService: RegistroPontoService) {}

  ngOnInit() {
    this.registroPontoService
      .getTodosRegistros(this.funcionarioId)
      .subscribe((data) => {
        this.bancoHoras = data;
        console.log(this.bancoHoras)
      });
  }
}
