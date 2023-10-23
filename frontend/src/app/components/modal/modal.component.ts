import { Component, OnInit } from '@angular/core';
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
  image?: File

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
      imagemPerfil: [null, Validators.required],
    });
  }

  onFileSelected(event: any) {
    const file: File = event.target.files[0];

    this.formContato.patchValue({ imagemPerfil: event.target.files[0] });
  }

  submit() {
    if(this.formContato.valid){
      const formData = new FormData()

      formData.append('nome', this.formContato.get('nome')!.value)
      formData.append('email', this.formContato.get('email')!.value)
      formData.append('telefone', this.formContato.get('telefone')!.value)
      formData.append('nascimento', this.formContato.get('nascimento')!.value)
      formData.append('imagemPerfil', this.formContato.get('imagemPerfil')!.value)

      this.contatoService.criarContato(formData).subscribe(
        (response) => {
          console.log('Contato criado com sucesso!', response)
          this.formContato.reset()
          this.modalService.showModal = false
          window.location.href = '/';
        },
        (error) => {
          console.log('Erro ao criar contato!', error)
        }
      )
    }
  }
}
