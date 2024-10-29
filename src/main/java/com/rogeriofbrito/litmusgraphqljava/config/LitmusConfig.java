package com.rogeriofbrito.litmusgraphqljava.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "litmus")
public class LitmusConfig {

    private String username;
    private String password;
    private String graphqlApiUrl;
    private String authorizationApiUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGraphqlApiUrl() {
        return graphqlApiUrl;
    }

    public void setGraphqlApiUrl(String graphqlApiUrl) {
        this.graphqlApiUrl = graphqlApiUrl;
    }

    public String getAuthorizationApiUrl() {
        return authorizationApiUrl;
    }

    public void setAuthorizationApiUrl(String authorizationApiUrl) {
        this.authorizationApiUrl = authorizationApiUrl;
    }
}
