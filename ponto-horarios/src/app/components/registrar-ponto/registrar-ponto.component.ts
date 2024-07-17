import { Component } from '@angular/core';
import { RegistroPontoService } from '../../services/registro-ponto.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-registrar-ponto',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './registrar-ponto.component.html',
  styleUrl: './registrar-ponto.component.scss',
})
export class RegistrarPontoComponent {
  ultimosRegistros: any[] = [];
  funcionarioId = 1; // substituir com o ID real do funcionário

  constructor(private registroPontoService: RegistroPontoService) {}

  registrarPonto() {
    this.registroPontoService.registrarPonto().subscribe((response) => {
      console.log('Ponto registrado', response);
    });
  }

  carregarUltimosRegistros() {
    this.registroPontoService
      .getUltimosRegistrosDoDia(this.funcionarioId)
      .subscribe((data) => {
        this.ultimosRegistros = data;
      });
  }
}
