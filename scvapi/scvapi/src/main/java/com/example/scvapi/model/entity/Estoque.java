package com.example.scvapi.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private int quantidadeMinima;
    private int quantidadeMaxima;
    private int pontoRessuprimento;
    private int quantidadeDisponivel;


    @ManyToOne
    private Fabricante fabricante;
}
