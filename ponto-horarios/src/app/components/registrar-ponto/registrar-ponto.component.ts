import { Component } from '@angular/core';
import { RegistroPontoService } from '../../services/registro-ponto.service';
import { DatePipe, NgClass } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-registrar-ponto',
  standalone: true,
  imports: [DatePipe, NgClass, RouterLink],
  templateUrl: './registrar-ponto.component.html',
  styleUrl: './registrar-ponto.component.scss',
})
export class RegistrarPontoComponent {
  isPressed = false;
  errorMessage = '';
  pressTimer: any;
  ultimosRegistros: any[] = [];
  funcionarioId = 1; // substituir com o ID real do funcionário

  constructor(private registroPontoService: RegistroPontoService) {
    this.carregarUltimosRegistros();
  }

  startPress() {
    this.isPressed = true;
    this.pressTimer = setTimeout(() => {
      // Lógica para registrar o ponto após 2 segundos
      this.registrarPonto();
      this.isPressed = false;
    }, 1000);
  }
  endPress() {
    this.isPressed = false;
    clearTimeout(this.pressTimer);
  }

  registrarPonto() {
    this.registroPontoService.registrarPonto().subscribe(
      (response) => {
        console.log('Ponto registrado', response);
        this.carregarUltimosRegistros();
      },
      (error) => {
        console.log(error.error.message);
      }
    );
  }

  carregarUltimosRegistros() {
    this.registroPontoService
      .getUltimosRegistrosDoDia(this.funcionarioId)
      .subscribe((data) => {
        console.log(data);
        this.ultimosRegistros = data;
      });
  }
}
