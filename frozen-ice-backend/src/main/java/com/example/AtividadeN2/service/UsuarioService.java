package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.UsuarioRequestDTO;
import com.example.AtividadeN2.dto.UsuarioResponseDTO;
import com.example.AtividadeN2.entity.Usuario;
import com.example.AtividadeN2.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponseDTO salvar(UsuarioRequestDTO dto) {

        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new RuntimeException("Senha é obrigatória");
        }

        Usuario usuario = UsuarioRequestDTO.toEntity(dto);

        usuario.setSenha(passwordEncoder.encode(dto.senha()));
        usuario.setDataCadastro(LocalDateTime.now());

        Usuario salvo = usuarioRepository.save(usuario);

        return UsuarioResponseDTO.toDto(salvo);
    }

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDTO::toDto)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return UsuarioResponseDTO.toDto(usuario);
    }

    public UsuarioResponseDTO atualizar(UUID id, UsuarioRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dto.senha()));
        }

        Usuario atualizado = usuarioRepository.save(usuario);

        return UsuarioResponseDTO.toDto(atualizado);
    }

    public void excluir(UUID id) {
        usuarioRepository.deleteById(id);
    }
}

