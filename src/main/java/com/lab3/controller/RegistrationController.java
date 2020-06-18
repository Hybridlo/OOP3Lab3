package com.lab3.controller;

import com.lab3.dto.UserCredentials;
import com.lab3.dto.UserDTO;
import com.lab3.dto.UserInfo;
import com.lab3.exceptions.UserAlreadyExistsException;
import com.lab3.service.KeycloakControllerService;
import com.lab3.service.RegistrationControllerService;
import com.lab3.service.UserControllerService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin
public class RegistrationController {

    private RegistrationControllerService registrationControllerService;
    private KeycloakControllerService keycloakControllerService;
    private UserControllerService userControllerService;

    public RegistrationController(RegistrationControllerService registrationControllerService,
                                  KeycloakControllerService keycloakControllerService,
                                  UserControllerService userControllerService){
        this.registrationControllerService = registrationControllerService;
        this.keycloakControllerService = keycloakControllerService;
        this.userControllerService = userControllerService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity registration(@Valid @RequestBody UserDTO userDTO){
        try {
            registrationControllerService.save(userDTO);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return keycloakControllerService.createUser(userDTO);
    }

    @PostMapping(value = "/login")
    public ResponseEntity login(@Valid @RequestBody UserCredentials userCredentials){
        return keycloakControllerService.getTokenUsingCredentials(userCredentials);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity refreshToken(@RequestHeader(value = "Authorization") String refreshToken) {
        return keycloakControllerService.getTokenUsingRefreshToken(refreshToken);
    }

    @GetMapping(value = "/me_info")
    public UserInfo getInfo(Principal principal) {
        return userControllerService.findUserByLogin(principal.getName());
    }

    @PostMapping(value = "/logout")
    public ResponseEntity<?> logoutUser(Principal principal) {
        return keycloakControllerService.logoutUser(principal);
    }
}
