package com.murray.communications.controllers.rest;

import com.murray.communications.services.users.UserAuthenticatonService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
public class UserAuthenticationController {


    private final UserAuthenticatonService userAuthenticatonService;

    public UserAuthenticationController(UserAuthenticatonService userAuthenticatonService) {
        this.userAuthenticatonService = userAuthenticatonService;
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody UserAuthenticationRequest authenticationRequest) {

        String token = userAuthenticatonService.signIn(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(token);



    }
}
