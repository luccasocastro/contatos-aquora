import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  public showModalPost: boolean = false
  public showModalPut: boolean = false

  private contatoId!: number;

  setContatoId(id: number){
    this.contatoId = id
  }

  getContatoId(){
    return this.contatoId
  }

  constructor() { }
}
