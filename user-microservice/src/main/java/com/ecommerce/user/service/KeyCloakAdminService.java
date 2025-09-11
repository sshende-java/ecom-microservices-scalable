package com.ecommerce.user.service;

import com.ecommerce.user.dto.UserRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeyCloakAdminService {

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${keycloak.admin.server-url}")
    private String keycloakServerUrl;

    @Value("${keycloak.admin.realm}")
    private String realm;

    @Value("${keycloak.admin.client-id}")
    private String clientId;

    private final RestTemplate restTemplate = new RestTemplate();


    //Getting Access token from Keycloak
    public String getAdminAccessToken() {
        //MultiValueMap is a type of map that allows you to associate multiple values with a single key.
//        eg:{
//            "category": ["books", "movies", "music"]
//        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("username", adminUsername);
        params.add("password", adminPassword);
        params.add("grant_type", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);      //like in postman
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String url = keycloakServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return response.getBody().get("access_token").toString();


    }

    //Create User in keycloak once u get Access token
    public String createUser(String token, UserRequest request) {

        //you can refer this body in keycloak documentation of creating new user in keycloak
//        {
//            "username": "usernew",
//                "firstName": "New First Name",
//                "lastName": "New Last Name",
//                "email": "usernew@email.com",
//                "enabled": true,
//                "credentials": [
//                    {
//                        "type": "password",
//                            "value": "usernew",
//                            "temporary": false
//                    }
//                ]
//        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", request.getUsername());
        userPayload.put("firstName", request.getFirstName());
        userPayload.put("lastName", request.getLastName());
        userPayload.put("email", request.getEmail());
        userPayload.put("enabled", true);
        Map<String, Object> credential = new HashMap<>();
        credential.put("type", "password");     //fixed value
        credential.put("value", request.getPassword());
        credential.put("temporary", false);      //Consider password as not temporary

        userPayload.put("credentials", List.of(credential));     //refer above structure

        HttpEntity<Map<String, Object>> requestPayload = new HttpEntity<>(userPayload, headers);

        String url = keycloakServerUrl + "/admin/realms/" + realm + "/users";


        /* -------  Just to log Request JSON -----------*/
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            json = mapper.writeValueAsString(userPayload);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Request JSON: " + json);
        /* -------  CLOSE Just to log Request JSON -----------*/


//      Create User in KeyCloak 
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestPayload, String.class);
        if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
            throw new RuntimeException("Failed to create user in keycloak " + response.getBody());
        }

        //Extract Keycloak user id from Response header
        URI location = response.getHeaders().getLocation();
        if (location == null) {
            throw new RuntimeException("Keycloak did not return keycloak location header " + response.getBody());
        }
        String path = location.getPath();
        return path.substring(path.lastIndexOf("/") + 1);
    }
}
