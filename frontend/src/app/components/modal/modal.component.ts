import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ModalService } from 'src/app/services/modal.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ContatoService } from 'src/app/services/contato.service';

@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css'],
})
export class ModalComponent implements OnInit {
  formContato!: FormGroup;

  constructor(
    public modalService: ModalService,
    private formBuilder: FormBuilder,
    private contatoService: ContatoService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.formContato = this.formBuilder.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefone: ['', Validators.required],
      nascimento: ['', Validators.required],
      imagemPerfil: ['', Validators.required],
    })
  }

  submit() {
    if (this.formContato.valid) {
      const dados = this.formContato.value

      this.contatoService.createContato(dados).subscribe(
        (resposta) => {
          console.log('Dados enviados com sucesso!', resposta)
          this.formContato.reset()
          this.modalService.showModal = false
          window.location.href = '/';
        },
        (erro) => {
          console.error('Erro ao enviar os dados', erro);
        }
      )
    }
  }
}
