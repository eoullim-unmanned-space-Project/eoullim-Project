package org.example.eoullimback.csrf;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CsrfApiController {

    @GetMapping("/api/csrf-token")
    public ResponseEntity<Map<String, String>> getCsrfToken(CsrfToken csrfToken) {
        return ResponseEntity.ok().body(Map.of("token", csrfToken.getToken(), "header", csrfToken.getHeaderName()));
    }
}
