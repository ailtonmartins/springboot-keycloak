package com.desafio.conta.interfaces.rest;

import com.desafio.conta.application.KeycloakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class LoginController {

    @Autowired
    private KeycloakService keycloakService;

    @PostMapping("/gerar")
    public ResponseEntity<String> getToken(@RequestParam String username, @RequestParam String password) {
        String token = keycloakService.getToken(username, password);
        return ResponseEntity.ok(token);
    }
}
