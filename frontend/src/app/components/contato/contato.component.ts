import { Contato } from 'src/app/Contato';
import { ContatoService } from './../../services/contato.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-contato',
  templateUrl: './contato.component.html',
  styleUrls: ['./contato.component.css'],
})
export class ContatoComponent implements OnInit{
  constructor(private contatoService: ContatoService) {}

  contatos: Contato[] = []

  ngOnInit(): void {
    this.carregarContatos()
  }

  deletarContato(id: number){
    if(confirm('Tem certeza que deseja excluir este contato?')){
      this.contatoService.deletarContato(id).subscribe(() => {
        console.log('Contato deletado')
        this.carregarContatos()
      })
    }
  }

  carregarContatos(){
    this.contatoService.getAll().subscribe((contatos) => {
      const data = contatos

      data.map((contato) => {
        contato.nascimento = new Date(contato.nascimento).toLocaleDateString(
          'pt-BR'
        )
      })

      this.contatos = contatos
    })
  }

}
