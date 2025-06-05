package com.condofacile.controller;

import com.condofacile.dto.LoginRequestDTO;
import com.condofacile.error.EmailNotFoundException;
import com.condofacile.error.PasswordIncorrectException;
import com.condofacile.service.UtenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/condofacile/api/login")
public class LoginController {

    @Autowired
    private UtenteService service;

    @PostMapping("/validate")
    public ResponseEntity<?> validateLogin(@RequestBody LoginRequestDTO loginRequest) {
        try {
            boolean isValid = service.validateLogin(loginRequest.getEmail(), loginRequest.getPassword());
            if (isValid) {
                return ResponseEntity.ok("Credenziali valide");
            } else {
                // teoricamente qui non arrivi perché il service lancia eccezioni
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
            }
        } catch (EmailNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (PasswordIncorrectException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            log.error("Errore durante la validazione login", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore interno del server");
        }
    }



}
