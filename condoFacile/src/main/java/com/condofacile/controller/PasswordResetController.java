package com.condofacile.controller;

import com.condofacile.service.ResetService;
import com.condofacile.util.NetworkUtils;
import jakarta.validation.constraints.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/condofacile/restore/password")
public class PasswordResetController {

    @Autowired
    private ResetService resetService;

    private static final int PORT = 8080;

    @PostMapping("/recover")
    public ResponseEntity<Map<String, Object>> sendResetLink(@RequestBody Map<String, String> body) {
        String newPassword = body.get("newPassword");  // già hashata lato client
        String email = body.get("email");              // lato client da raccogliere

        final int PORT = 9090;
        String baseUrl = "http://localhost:" + PORT;
        String resetLink = baseUrl + "/condofacile/restore/password/update?email=" + email + "&newPassword=" + newPassword;

        boolean success = resetService.sendResetLink(email, resetLink);

        Map<String, Object> res = new HashMap<>();
        if (success) {
            res.put("status", 200);
            res.put("message", "Link inviato tramite email");
            return ResponseEntity.ok(res);
        } else {
            res.put("status", 500);
            res.put("message", "Errore durante l'invio del link");
            return ResponseEntity.status(500).body(res);
        }
    }

    @GetMapping("/update")
    public ResponseEntity<Map<String, Object>> updatePassword(
            @RequestParam("email") @Email String email,
            @RequestParam("newPassword") String newPassword
    ) {
        boolean updated = resetService.updatePassword(email, newPassword);

        Map<String, Object> res = new HashMap<>();
        if (updated) {
            res.put("status", 200);
            res.put("message", "Password aggiornata con successo");
            return ResponseEntity.ok(res);
        } else {
            res.put("status", 404);
            res.put("message", "Utente non trovato");
            return ResponseEntity.status(404).body(res);
        }
    }
}
