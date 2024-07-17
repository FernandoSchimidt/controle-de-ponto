import { Component, OnInit } from '@angular/core';
import { BancoHorasService } from '../../services/banco-horas.service';

@Component({
  selector: 'app-banco-horas',
  standalone: true,
  imports: [],
  templateUrl: './banco-horas.component.html',
  styleUrl: './banco-horas.component.scss'
})
export class BancoHorasComponent implements OnInit {
  bancoHoras: any[] = [];

  constructor(private bancoHorasService: BancoHorasService) { }

  ngOnInit() {
    this.bancoHorasService.getBancoHoras().subscribe(data => {
      this.bancoHoras = data;
    });
  }

}
