package com.example.AtividadeN2.service;

import com.example.AtividadeN2.dto.ClienteRequestDTO;
import com.example.AtividadeN2.dto.ClienteResponseDTO;
import com.example.AtividadeN2.entity.Cliente;
import com.example.AtividadeN2.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public List<ClienteResponseDTO> listarClientes() {
        return clienteRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public ClienteResponseDTO buscarCliente(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        return converterParaDTO(cliente);
    }

    public ClienteResponseDTO cadastrarCliente(ClienteRequestDTO dto) {
        validarCPF(dto.cpf(), null);

        Cliente cliente = new Cliente();

        cliente.setNome(dto.nome().trim());
        cliente.setCpf(normalizarCPF(dto.cpf()));
        cliente.setTelefone(normalizar(dto.telefone()));
        cliente.setEmail(normalizarEmail(dto.email()));
        cliente.setEndereco(normalizar(dto.endereco()));

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return converterParaDTO(clienteSalvo);
    }

    public ClienteResponseDTO atualizarCliente(UUID id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        validarCPF(dto.cpf(), id);

        cliente.setNome(dto.nome().trim());
        cliente.setCpf(normalizarCPF(dto.cpf()));
        cliente.setTelefone(normalizar(dto.telefone()));
        cliente.setEmail(normalizarEmail(dto.email()));
        cliente.setEndereco(normalizar(dto.endereco()));

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return converterParaDTO(clienteAtualizado);
    }

    public void excluirCliente(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado."));

        clienteRepository.delete(cliente);
    }

    private ClienteResponseDTO converterParaDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getTelefone(),
                cliente.getEmail(),
                cliente.getEndereco()
        );
    }

    private void validarCPF(String cpf, UUID idAtual) {
        String cpfLimpo = normalizarCPF(cpf);

        if (cpfLimpo == null) {
            return;
        }

        boolean existe = clienteRepository.findAll()
                .stream()
                .anyMatch(cliente ->
                        cpfLimpo.equals(normalizarCPF(cliente.getCpf()))
                                && !cliente.getId().equals(idAtual)
                );

        if (existe) {
            throw new RuntimeException("Já existe um cliente cadastrado com esse CPF.");
        }
    }

    private String normalizarCPF(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            return null;
        }

        return cpf.replaceAll("\\D", "");
    }

    private String normalizar(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }

    private String normalizarEmail(String email) {
        String valor = normalizar(email);
        return valor == null ? null : valor.toLowerCase();
    }
}

