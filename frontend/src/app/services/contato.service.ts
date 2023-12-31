import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Contato } from '../Contato';

@Injectable({
  providedIn: 'root',
})
export class ContatoService {
  private apiUrl = 'http://localhost:8080/contatos';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Contato[]> {
    return this.http.get<Contato[]>(this.apiUrl);
  }

  criarContato(dados: FormData): Observable<FormData> {
    const headers = new HttpHeaders();
    return this.http.post<FormData>(`${this.apiUrl}/criar`, dados, { headers });
  }

  deletarContato(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/deletar/${id}`)
  }

  buscarContato(id: number): Observable<Contato>{
    return this.http.get<Contato>(`${this.apiUrl}/${id}`)
  }

  atualizarContato(id: number, dados: FormData): Observable<FormData>{
    const headers = new HttpHeaders()
    return this.http.put<FormData>(`${this.apiUrl}/atualizar/${id}`, dados, { headers })
  }
}
