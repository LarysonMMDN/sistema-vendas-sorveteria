package com.example.AtividadeN2.dto;

import com.example.AtividadeN2.entity.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UsuarioRequestDTO(

        @NotBlank(message = "Nome não pode ser vazio")
        String nome,

        @NotBlank(message = "Senha não pode ser vazia")
        String senha,

        @NotBlank(message = "Email não pode ser vazio")
        @Email(message = "Email inválido")
        String email
) {
    public static Usuario toEntity(UsuarioRequestDTO dto){
        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setSenha(dto.senha());
        usuario.setEmail(dto.email());
        return usuario;
    }
}
