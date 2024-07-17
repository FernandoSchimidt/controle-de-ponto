import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class BancoHorasService {

  private apiUrl = 'http://localhost:8080/banco-horas';

  constructor(private http: HttpClient) { }

  getBancoHoras(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
