package com.example.AtividadeN2.config;

import com.example.AtividadeN2.entity.Usuario;
import com.example.AtividadeN2.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner criarUsuariosPadrao(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!usuarioRepository.existsByEmail("admin@email.com")) {

                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setEmail("admin@email.com");
                admin.setSenha(passwordEncoder.encode("admin123"));
                admin.setPerfil("ADMIN");
                admin.setDataCadastro(LocalDateTime.now());

                usuarioRepository.save(admin);
            }

            if (!usuarioRepository.existsByEmail("atendente@email.com")) {

                Usuario atendente = new Usuario();
                atendente.setNome("Atendente");
                atendente.setEmail("atendente@email.com");
                atendente.setSenha(passwordEncoder.encode("atendente123"));
                atendente.setPerfil("ATENDENTE");
                atendente.setDataCadastro(LocalDateTime.now());

                usuarioRepository.save(atendente);
            }

            System.out.println("==================================");
            System.out.println("USUARIOS PADRAO VERIFICADOS!");
            System.out.println("Administrador: admin@email.com");
            System.out.println("Atendente: atendente@email.com");
            System.out.println("==================================");
        };
    }
}
