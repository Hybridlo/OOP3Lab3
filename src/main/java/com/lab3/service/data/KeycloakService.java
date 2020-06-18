package com.lab3.service.data;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Component;

import com.lab3.dto.UserCredentials;
import com.lab3.dto.UserDTO;

@Component
public class KeycloakService {

    private String SECRETKEY = "979f7f94-f8c7-463c-8203-dad0645dfcdc";

    private String CLIENTID = "login-app";

    private String AUTHURL = "http://127.0.0.1:8080/auth";

    private String REALM = "lab3";

    public String getToken(UserCredentials userCredentials) {

        String responseToken = null;
        try {

            String username = userCredentials.getLogin();

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("grant_type", "password"));
            urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
            urlParameters.add(new BasicNameValuePair("username", username));
            urlParameters.add(new BasicNameValuePair("password", userCredentials.getPassword()));
            urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

            String response = sendPost(urlParameters);
            ObjectMapper mapper = new ObjectMapper();
            responseToken = (String) mapper.readValue(response, Map.class).get("access_token");

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (responseToken == null)
            return responseToken;

        return "{\"token\": \"" + responseToken + "\"}";

    }

    public String getByRefreshToken(String refreshToken) {

        String responseToken = null;
        try {

            List<NameValuePair> urlParameters = new ArrayList<>();
            urlParameters.add(new BasicNameValuePair("grant_type", "refresh_token"));
            urlParameters.add(new BasicNameValuePair("client_id", CLIENTID));
            urlParameters.add(new BasicNameValuePair("refresh_token", refreshToken));
            urlParameters.add(new BasicNameValuePair("client_secret", SECRETKEY));

            responseToken = sendPost(urlParameters);

        } catch (Exception e) {
            e.printStackTrace();

        }

        return responseToken;
    }

    public int createUserInKeyCloak(UserDTO userDTO) {

        int statusId = 0;
        try {

            UsersResource userResource = getKeycloakUserResource();

            UserRepresentation user = new UserRepresentation();
            user.setUsername(userDTO.getLogin());
            user.setEnabled(true);

            // Create user
            Response result = userResource.create(user);
            System.out.println("Keycloak create user response code>>>>" + result.getStatus());

            statusId = result.getStatus();

            if (statusId == 201) {

                String userId = result.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                System.out.println("User created with userId:" + userId);

                // Define password credential
                CredentialRepresentation passwordCred = new CredentialRepresentation();
                passwordCred.setTemporary(false);
                passwordCred.setType(CredentialRepresentation.PASSWORD);
                passwordCred.setValue(userDTO.getPassword());

                // Set password credential
                userResource.get(userId).resetPassword(passwordCred);

                // set role
                RealmResource realmResource = getRealmResource();
                RoleRepresentation savedRoleRepresentation = realmResource.roles()
                        .get(("ROLE_" + userDTO.getType()).toUpperCase())
                        .toRepresentation();
                realmResource.users().get(userId).roles().realmLevel().add(Arrays.asList(savedRoleRepresentation));

                System.out.println("Username==" + userDTO.getLogin() + " created in keycloak successfully");

            }

            else if (statusId == 409) {
                System.out.println("Username==" + userDTO.getLogin() + " already present in keycloak");

            } else {
                System.out.println("Username==" + userDTO.getLogin() + " could not be created in keycloak");

            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return statusId;

    }

    // after logout user from the keycloak system. No new access token will be
    // issued.
    public void logoutUser(String userId) {

        UsersResource userResource = getKeycloakUserResource();

        userResource.get(userId).logout();

    }

    // Reset password
    public void resetPassword(String newPassword, String userId) {

        UsersResource userResource = getKeycloakUserResource();

        // Define password credential
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(newPassword.trim());

        // Set password credential
        userResource.get(userId).resetPassword(passwordCred);

    }

    private UsersResource getKeycloakUserResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("hybridlo").password("5502")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        RealmResource realmResource = kc.realm(REALM);
        UsersResource userResource = realmResource.users();

        return userResource;
    }

    private RealmResource getRealmResource() {

        Keycloak kc = KeycloakBuilder.builder().serverUrl(AUTHURL).realm("master").username("hybridlo").password("5502")
                .clientId("admin-cli").resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                .build();

        RealmResource realmResource = kc.realm(REALM);

        return realmResource;

    }

    private String sendPost(List<NameValuePair> urlParameters) throws Exception {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(AUTHURL + "/realms/" + REALM + "/protocol/openid-connect/token");

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);

        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        return result.toString();

    }

}