package com.condofacile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "avvisi")@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Avviso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String titolo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String messaggio;

    @Column(name = "data_pubblicazione", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataPubblicazione;

    @Column(name = "solo_personale")
    private Boolean soloPersonale = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utente destinatario;
}