package com.souzaluccas.backend.models;

import java.time.LocalDate;

public record ContatoResponse(Long id, String nome, String email, String telefone, LocalDate nascimento, String imagemPerfilUrl) {
}
