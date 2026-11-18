package com.example.AtividadeN2.Controller;

import com.example.AtividadeN2.dto.ClienteRequestDTO;
import com.example.AtividadeN2.dto.ClienteResponseDTO;
import com.example.AtividadeN2.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarCliente(@PathVariable UUID id) {
        return ResponseEntity.ok(clienteService.buscarCliente(id));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> cadastrarCliente(
            @Valid @RequestBody ClienteRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.cadastrarCliente(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteRequestDTO dto) {

        return ResponseEntity.ok(clienteService.atualizarCliente(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirCliente(@PathVariable UUID id) {
        clienteService.excluirCliente(id);
        return ResponseEntity.noContent().build();
    }

}
