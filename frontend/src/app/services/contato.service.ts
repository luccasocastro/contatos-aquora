import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { HttpClient } from '@angular/common/http';
import { Contato } from '../Contato';

@Injectable({
  providedIn: 'root',
})
export class ContatoService {
  private apiUrl = 'http://localhost:8080/contatos';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Contato[]>{
    return this.http.get<Contato[]>(this.apiUrl)
  }
}
