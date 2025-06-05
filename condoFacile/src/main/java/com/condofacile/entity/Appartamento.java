package com.condofacile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appartamenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String codice;

    @Column(nullable = false)
    private Boolean occupato = false;
}