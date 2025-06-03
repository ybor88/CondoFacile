package com.condofacile.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "utenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100)
    private String cognome;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false, columnDefinition = "TEXT")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Ruolo ruolo;

    @Column(length = 10)
    private String appartamento;

    @Column(nullable = false)
    private Boolean attivo = true;

    public enum Ruolo {
        condomino,
        amministratore
    }
}
