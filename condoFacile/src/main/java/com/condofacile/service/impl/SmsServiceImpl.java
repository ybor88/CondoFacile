package com.condofacile.service.impl;

import com.condofacile.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {

    private final RestTemplate restTemplate;

    public SmsServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public boolean sendSms(String toPhoneNumber, String message) {
        // Endpoint del provider SMS (es. smsapi, skebby, nexmo, etc.)
        String smsApiUrl = "https://api.smsprovider.com/send"; // da sostituire con il tuo provider reale

        // Payload da inviare
        Map<String, Object> payload = new HashMap<>();
        payload.put("to", toPhoneNumber);
        payload.put("message", message);
        payload.put("apiKey", "TUO_API_KEY"); // sostituire con credenziali reali

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    smsApiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("SMS inviato con successo a {}", toPhoneNumber);
                return true;
            } else {
                log.warn("Errore invio SMS. Codice: {}, Risposta: {}", response.getStatusCode(), response.getBody());
                return false;
            }

        } catch (Exception e) {
            log.error("Eccezione durante l'invio SMS: {}", e.getMessage(), e);
            return false;
        }
    }
}