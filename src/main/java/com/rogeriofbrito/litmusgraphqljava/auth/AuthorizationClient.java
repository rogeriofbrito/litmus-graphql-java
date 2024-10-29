package com.rogeriofbrito.litmusgraphqljava.auth;

import com.rogeriofbrito.litmusgraphqljava.config.LitmusConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AuthorizationClient {

    private final LitmusConfig litmusConfig;
    private final RestTemplate restTemplate;

    public AuthorizationClient(LitmusConfig litmusConfig, RestTemplate restTemplate) {
        this.litmusConfig = litmusConfig;
        this.restTemplate = restTemplate;
    }

    public LoginResponse login(LoginRequest request) {
        return restTemplate
                .postForEntity(litmusConfig.getAuthorizationApiUrl() + "/login", request, LoginResponse.class)
                .getBody();
    }
}
