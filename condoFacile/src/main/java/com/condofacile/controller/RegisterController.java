package com.condofacile.controller;

import com.condofacile.dto.AppartamentoDTO;
import com.condofacile.service.RegisterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/condofacile/api/register")
public class RegisterController {

    @Autowired
    private RegisterService service;

    @GetMapping("/appartamentiList")
    public ResponseEntity<Map<String, Object>> getAvailableAppartamenti() {
        List<AppartamentoDTO> appartamenti = service.getAvailableAppartamenti();
        Map<String, Object> response = new HashMap<>();

        if (appartamenti == null || appartamenti.isEmpty()) {
            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", "Nessun appartamento disponibile trovato");
            response.put("data", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.put("status", HttpStatus.OK.value());
        response.put("message", "Lista degli appartamenti disponibili");
        response.put("data", appartamenti);
        return ResponseEntity.ok(response);
    }
}
