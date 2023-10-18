package com.souzaluccas.backend.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.souzaluccas.backend.models.Contato;
import com.souzaluccas.backend.repositories.ContatoRepository;

@Service
public class ContatoService {
    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public Contato create(Contato obj){
        try{
            return contatoRepository.save(obj);
        }catch(DataIntegrityViolationException e){
            throw new RuntimeException("Erro ao criar o contato: " + e.getMessage(), e);
        }
        
    }

    public List<Contato> list(){
        return (List<Contato>) contatoRepository.findAll();
    }

    public Contato findById(Long id){
        return contatoRepository.findById(id).orElseThrow(
            () -> new NoSuchElementException("Contato não encontrado com o ID: " + id)
        );
    }

    public Contato update(Contato obj, Long id){
        return contatoRepository.findById(id).map(contatoExistente -> {
                contatoExistente.setNome(obj.getNome());
                contatoExistente.setEmail(obj.getEmail());
                contatoExistente.setTelefone(obj.getTelefone());
                contatoExistente.setNascimento(obj.getNascimento());

                return contatoRepository.save(contatoExistente);
            }).orElseThrow(() -> new NoSuchElementException("Contato não encontrado com o ID: " + id));
    }

    public void delete(Long id){
        try{
            contatoRepository.deleteById(id);
        }catch(EmptyResultDataAccessException e){
            throw new NoSuchElementException("Contato não encontrado com o ID: " + id);
        }
    }
}
