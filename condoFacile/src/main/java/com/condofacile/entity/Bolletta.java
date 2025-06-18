package com.condofacile.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "bollette")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bolletta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

    @Column(name = "imports", nullable = false)
    private BigDecimal importo;

    @Column(name = "data_emissione", nullable = false)
    private LocalDate dataEmissione;

    @Column(name = "data_scadenza", nullable = false)
    private LocalDate dataScadenza;

    @Column(name = "pagata")
    private Boolean pagata = false;

    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;
}