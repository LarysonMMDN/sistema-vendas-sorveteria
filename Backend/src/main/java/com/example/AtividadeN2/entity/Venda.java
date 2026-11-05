package com.example.AtividadeN2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String formaPagamento;
    private BigDecimal valorTotal;
    private String status;
    private Usuario usuario;
}
