package com.condofacile.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UtenteDTO {

    private Integer id;

    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    private String cognome;

    @Email(message = "Email non valida")
    @NotBlank(message = "L'email è obbligatoria")
    private String email;

    @NotBlank(message = "Il ruolo è obbligatorio")
    private String ruolo;

    private String appartamento;

    private Boolean attivo;

    @Size(min = 6, message = "La password deve essere di almeno 6 caratteri")
    @Setter(AccessLevel.NONE) // password non è serializzata in output
    private String password;

    // Setter personalizzato per poter impostare password in input, ma non esporla in output
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}