package com.condofacile.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "residenti")
@Getter
@Setter
public class Residente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String cognome;

    @OneToOne
    @JoinColumn(name = "appartamento_id", referencedColumnName = "id")
    private Appartamento appartamento;

    @NotBlank
    private String ruolo; // proprietario/inquilino

    @NotBlank
    private String contatti;

}