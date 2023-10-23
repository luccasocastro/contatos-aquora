package com.souzaluccas.backend.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.souzaluccas.backend.models.Contato;
import com.souzaluccas.backend.services.ContatoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contatos")
public class ContatoController {
    private final ContatoService contatoService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Contato> create(@Valid @RequestParam("nome") String nome, @RequestParam("email") String email,
            @RequestParam("telefone") String telefone, @RequestParam("nascimento") LocalDate nascimento,
            @RequestParam("imagemPerfil") MultipartFile imagemPerfil) {

        // Salve a imagem de perfil no diretório configurado
        String imagemPerfilPath = uploadDir + File.separator + imagemPerfil.getOriginalFilename();
        Path imagePath = Paths.get(imagemPerfilPath);

        try {
            Files.write(imagePath, imagemPerfil.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            // Lida com erros ao salvar a imagem
        }

        Contato contato = new Contato(null, nome, email, telefone, nascimento, imagemPerfilPath);
        Contato novoContato = contatoService.create(contato);

        return ResponseEntity.status(HttpStatus.CREATED).body(novoContato);
    }

    @GetMapping
    public ResponseEntity<List<Contato>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(contatoService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> findById(@PathVariable Long id) {
        Contato contato = contatoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(contato);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Contato> update(@RequestBody Contato obj, @RequestParam Long id) {
        Contato contatoAtualizado = contatoService.update(obj, id);
        return ResponseEntity.status(HttpStatus.OK).body(contatoAtualizado);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> delete(@RequestParam Long id) {
        contatoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
