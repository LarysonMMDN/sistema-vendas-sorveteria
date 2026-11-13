package com.example.AtividadeN2.Controller;

import com.example.AtividadeN2.dto.VendaRequestDTO;
import com.example.AtividadeN2.dto.VendaResponseDTO;
import com.example.AtividadeN2.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public ResponseEntity<VendaResponseDTO> cadastrar(
            @Valid @RequestBody VendaRequestDTO dto) {

        return ResponseEntity.ok(vendaService.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<List<VendaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(vendaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponseDTO> buscarPorId(
            @PathVariable UUID id) {

        return ResponseEntity.ok(vendaService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        vendaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
