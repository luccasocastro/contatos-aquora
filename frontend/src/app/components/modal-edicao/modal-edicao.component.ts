import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Contato } from 'src/app/Contato';
import { ContatoService } from 'src/app/services/contato.service';
import { ModalService } from 'src/app/services/modal.service';

@Component({
  selector: 'app-modal-edicao',
  templateUrl: './modal-edicao.component.html',
  styleUrls: ['./modal-edicao.component.css'],
})
export class ModalEdicaoComponent implements OnInit {
  formContato!: FormGroup;
  image?: File;
  contatoParaEdicao!: Contato;

  constructor(
    public modalService: ModalService,
    private formBuilder: FormBuilder,
    private contatoService: ContatoService
  ) {}

  ngOnInit(): void {
    this.formContato = this.formBuilder.group({
      nome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      telefone: ['', Validators.required],
      nascimento: ['', Validators.required],
      imagemPerfil: ['', Validators.required],
    });
    this.carregarContato()
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    this.formContato.patchValue({ imagemPerfil: event.target.files[0] });
  }

  carregarContato(){
    const contatoId = this.modalService.getContatoId();
    this.contatoService.buscarContato(contatoId).subscribe((data) => {
      this.contatoParaEdicao = data;
    });
  }

  submit() {
    if (this.formContato.valid) {
      const formData = new FormData();
      formData.append('nome', this.formContato.get('nome')?.value);
      formData.append('email', this.formContato.get('email')?.value);
      formData.append('telefone', this.formContato.get('telefone')?.value);
      if (!this.formContato.get('nascimento')?.value) {
        formData.append('nascimento', this.contatoParaEdicao.nascimento);
      }
      formData.append('nascimento', this.formContato.get('nascimento')?.value);
      if (!this.formContato.get('imagemPerfil')?.value) {
        formData.append('imagemPerfil', this.contatoParaEdicao.imagemPerfil);
      }
      formData.append(
        'imagemPerfil',
        this.formContato.get('imagemPerfil')?.value
      );

      this.contatoService
        .atualizarContato(this.contatoParaEdicao.id, formData)
        .subscribe(
          (response) => {
            console.log('Contato atualizado com sucesso!', response);
            this.formContato.reset();
            this.modalService.showModalPut = false;
            window.location.href = '/';
          },
          (error) => {
            console.log('Erro ao atualizar o contato contato!', error);
          }
        );
    }
  }
}
