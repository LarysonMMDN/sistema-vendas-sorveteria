package com.example.AtividadeN2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private long id;
    private String nome;
    private String descricao;
    private String tipo;
    private Boolean status;

}
