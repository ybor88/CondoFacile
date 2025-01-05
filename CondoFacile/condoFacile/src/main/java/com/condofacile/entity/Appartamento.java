package com.condofacile.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Appartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private int numero;

    @NotNull
    private int piano;

    @NotNull
    private double superficie;

    @NotNull
    private boolean occupato;

}