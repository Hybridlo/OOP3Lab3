package com.lab3.service;

import com.lab3.converter.UserConverter;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.lab3.dto.UserCredentials;
import com.lab3.dto.UserDTO;
import com.lab3.service.data.KeycloakService;

import java.security.Principal;

@Service
public class KeycloakControllerService {
    private KeycloakService keycloakService;
    private UserConverter userConverter;
    
    KeycloakControllerService(KeycloakService keycloakService, UserConverter userConverter) {
        this.keycloakService = keycloakService;
        this.userConverter = userConverter;
    }

    /*
     * Get token for the first time when user log in. We need to pass
     * credentials only once. Later communication will be done by sending token.
     */

    public ResponseEntity<?> logoutUser(Principal principal) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) principal;
        String userId = token.getAccount().getKeycloakSecurityContext().getToken().getSubject();
        keycloakService.logoutUser(userId);

        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }


    public ResponseEntity<?> getTokenUsingCredentials(UserCredentials userCredentials) {

        String responseToken;
        try {

            responseToken = keycloakService.getToken(userCredentials);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (responseToken == null)
            return new ResponseEntity<>("{\"error\": \"Invalid credentials\"}", HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(responseToken, HttpStatus.OK);

    }

    /*
     * When access token get expired than send refresh token to get new access
     * token. We will receive new refresh token also in this response.Update
     * client cookie with updated refresh and access token
     */
    public ResponseEntity<?> getTokenUsingRefreshToken(String refreshToken) {

        String responseToken;
        try {

            responseToken = keycloakService.getByRefreshToken(refreshToken);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(responseToken, HttpStatus.OK);

    }

    /*
     * Creating user in keycloak passing UserDTO contains username, password
     */
    public ResponseEntity<?> createUser(UserDTO userDTO) {
        try {

            keycloakService.createUserInKeyCloak(userDTO);
            return getTokenUsingCredentials(userConverter.dtoToCredentials(userDTO));
        }

        catch (Exception ex) {

            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }

    }
}
