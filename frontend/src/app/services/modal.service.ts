import { Injectable } from '@angular/core';
import { createMask } from '@ngneat/input-mask';

@Injectable({
  providedIn: 'root'
})
export class ModalService {

  mask = '(99) 9 9999-9999'
  public maskTelefone = createMask(this.mask)

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
