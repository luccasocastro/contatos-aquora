package com.souzaluccas.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.souzaluccas.backend.models.Contato;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long>{
    
}
