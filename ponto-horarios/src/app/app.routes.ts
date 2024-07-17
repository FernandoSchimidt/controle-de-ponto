import { Routes } from '@angular/router';
import { RegistrarPontoComponent } from './components/registrar-ponto/registrar-ponto.component';
import { BancoHorasComponent } from './components/banco-horas/banco-horas.component';

export const routes: Routes = [
  { path: '', redirectTo: 'registrar-ponto', pathMatch: 'full' },
  { path: 'registrar-ponto', component: RegistrarPontoComponent },
  { path: 'banco-horas', component: BancoHorasComponent },
];
