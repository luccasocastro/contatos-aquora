package com.souzaluccas.backend.controllers;

import java.util.List;

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

import com.souzaluccas.backend.models.Contato;
import com.souzaluccas.backend.services.ContatoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contatos")
public class ContatoController {
    private final ContatoService contatoService;

    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Contato> create(@Valid @RequestBody Contato obj){
        Contato contato = contatoService.create(obj);
        return ResponseEntity.status(HttpStatus.CREATED).body(contato);
    }

    @GetMapping
    public ResponseEntity<List<Contato>> list(){
        return ResponseEntity.status(HttpStatus.OK).body(contatoService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> findById(@PathVariable Long id){
        Contato contato = contatoService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(contato);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Contato> update(@RequestBody Contato obj, @RequestParam Long id){
        Contato contatoAtualizado = contatoService.update(obj, id);
        return ResponseEntity.status(HttpStatus.OK).body(contatoAtualizado);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Object> delete(@RequestParam Long id){
        contatoService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
