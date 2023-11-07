package com.souzaluccas.backend.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.souzaluccas.backend.models.Contato;
import com.souzaluccas.backend.repositories.ContatoRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ContatoService {
    private final ContatoRepository contatoRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public Contato create(Contato obj) {
        try {
            return contatoRepository.save(obj);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Erro ao criar o contato: " + e.getMessage(), e);
        }
    }

    public List<Contato> list() {
        return contatoRepository.findAll();
    }

    public Contato findById(Long id) {
        return contatoRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Contato não encontrado com o ID: " + id));
    }

    public Contato update(Contato obj, Long id, MultipartFile imagemPerfil) throws IOException {
        return contatoRepository.findById(id).map(contatoExistente -> {
            contatoExistente.setNome(obj.getNome());
            contatoExistente.setEmail(obj.getEmail());
            contatoExistente.setTelefone(obj.getTelefone());
            contatoExistente.setNascimento(obj.getNascimento());

            String extensao = imagemPerfil.getOriginalFilename().substring(imagemPerfil.getOriginalFilename().lastIndexOf("."));
            String nomeArquivo = UUID.randomUUID().toString() + extensao;
            Path path = Paths.get(uploadDir + nomeArquivo);
            try {
                Files.write(path, imagemPerfil.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            contatoExistente.setImagemPerfil(nomeArquivo);
            return contatoRepository.save(contatoExistente);
        }).orElseThrow(() -> new NoSuchElementException("Contato não encontrado com o ID: " + id));
    }

    public void delete(Long id) {
        try {
            Contato contato = contatoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Contato não encontrado com o ID: " + id));

            Path path = Paths.get(uploadDir + contato.getImagemPerfil());
            Files.deleteIfExists(path);

            contatoRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchElementException("Contato não encontrado com o ID: " + id);
        } catch (IOException e) {
            throw new RuntimeException("Falha ao deletar a foto de perfil!", e);
        }
    }
}
