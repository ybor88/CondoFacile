package com.condofacile.controller;

import com.condofacile.dto.LoginRequestDTO;
import com.condofacile.dto.UtenteDTO;
import com.condofacile.error.EmailNotFoundException;
import com.condofacile.error.PasswordIncorrectException;
import com.condofacile.service.UtenteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/condofacile/api/login")
public class LoginController {

    @Autowired
    private UtenteService service;

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateLogin(@RequestBody LoginRequestDTO loginRequest) {
        Map<String, Object> response = new HashMap<>();

        try {
            UtenteDTO utenteDTO = service.validateLogin(loginRequest.getEmail(), loginRequest.getPassword());

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Credenziali valide");
            response.put("data", utenteDTO);

            return ResponseEntity.ok(response);

        } catch (EmailNotFoundException e) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (PasswordIncorrectException e) {
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

        } catch (Exception e) {
            log.error("Errore durante la validazione login", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Errore interno del server");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



}
