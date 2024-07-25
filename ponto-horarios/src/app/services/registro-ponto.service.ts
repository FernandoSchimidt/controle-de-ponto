import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RegistroPontoService {
  private apiUrl = 'http://localhost:8080/registros-ponto';

  constructor(private http: HttpClient) {}

  registrarPonto(): Observable<any> {
    const funcionarioId = 1; // substituir com o ID real do funcionário
    return this.http.post(`${this.apiUrl}/registrar/${funcionarioId}`, {});
  }

  getUltimosRegistrosDoDia(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.apiUrl}/ultimos-registros/${funcionarioId}`
    );
  }

  getTodosRegistros(funcionarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${funcionarioId}`);
  }
}
