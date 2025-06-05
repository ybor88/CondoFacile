package com.condofacile.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {

    @Email(message = "Email non valida")
    @NotBlank(message = "L'email è obbligatoria")
    private String email;

    @NotBlank(message = "La password è obbligatoria")
    @Setter(AccessLevel.NONE)  // password non viene serializzata in output
    private String password;

    // Setter personalizzato per poter impostare la password in input ma non esporla in output
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}