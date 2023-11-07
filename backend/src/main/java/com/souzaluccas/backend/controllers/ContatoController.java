package com.souzaluccas.backend.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.souzaluccas.backend.models.ContatoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.souzaluccas.backend.models.Contato;
import com.souzaluccas.backend.services.ContatoService;

import jakarta.validation.Valid;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<Contato> create(@Valid @RequestParam("nome") String nome, @RequestParam("email") String email, @RequestParam("telefone") String telefone, @RequestParam("nascimento") LocalDate nascimento, @RequestParam("imagemPerfil") MultipartFile imagemPerfil) {
        try{
            String extensao = imagemPerfil.getOriginalFilename().substring(imagemPerfil.getOriginalFilename().lastIndexOf("."));
            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path path = Paths.get(uploadDir + nomeArquivo);
            Files.write(path, imagemPerfil.getBytes());

            Contato contato = new Contato(null, nome, email, telefone, nascimento, nomeArquivo);
            return new ResponseEntity<>(contatoService.create(contato), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<ContatoResponse>> list() {
        try{
            List<Contato> contatos = new ArrayList<Contato>(contatoService.list());

            if(contatos.isEmpty()) return new ResponseEntity<>(HttpStatus.NO_CONTENT);

            List<ContatoResponse> contatosResponse = contatos.stream().map(contato -> {
                String imagemPerfilUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path("/contatos/imagens/").path(contato.getImagemPerfil()).toUriString();

                return new ContatoResponse(contato.getId(), contato.getNome(), contato.getEmail(), contato.getTelefone(), contato.getNascimento(), imagemPerfilUrl);
            }).collect(Collectors.toList());

            return new ResponseEntity<>(contatosResponse, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> findById(@PathVariable Long id) {
        Contato contato = contatoService.findById(id);
        return new ResponseEntity<>(contato, HttpStatus.OK);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Contato> update(@Valid @RequestParam("nome") String nome, @RequestParam("email") String email, @RequestParam("telefone") String telefone, @RequestParam("nascimento") LocalDate nascimento, @RequestParam("imagemPerfil") MultipartFile imagemPerfil, @PathVariable Long id){
        Contato obj = new Contato(null, nome, email, telefone, nascimento, imagemPerfil.getName());
        try{
            Contato novoContato = contatoService.update(obj, id, imagemPerfil);

            return new ResponseEntity<>(novoContato, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        contatoService.delete(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping("/imagens/{fileName:.+}")
    public ResponseEntity<Resource> downloadImagem(@PathVariable String fileName) {
        try{
            Path path = Paths.get(uploadDir + fileName);
            Resource resource = new UrlResource(path.toUri());

            if(resource.exists() || resource.isReadable()){
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
            }else{
                throw new RuntimeException("Não foi possível ler o arquivo: " + fileName);
            }
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
