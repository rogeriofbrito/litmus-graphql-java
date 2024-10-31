package com.rogeriofbrito.litmusgraphqljava.tokenstore;

import com.rogeriofbrito.litmusgraphqljava.auth.AuthorizationClient;
import com.rogeriofbrito.litmusgraphqljava.auth.LoginRequest;
import com.rogeriofbrito.litmusgraphqljava.auth.LoginResponse;
import com.rogeriofbrito.litmusgraphqljava.config.LitmusConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;

@Component
public class TokenStore {

    private final ReentrantLock lock = new ReentrantLock();
    private LoginResponse loginResponse;
    private Long updatedAt;

    private final LitmusConfig litmusConfig;
    private final AuthorizationClient authorizationClient;

    public TokenStore(LitmusConfig litmusConfig, AuthorizationClient authorizationClient) {
        this.litmusConfig = litmusConfig;
        this.authorizationClient = authorizationClient;
    }

    public LoginResponse getLoginResponse() {
        lock.lock();
        try {
            if (tokenShouldBeUpdated()) {
                updateLoginResponse();
            }
            return loginResponse;
        } finally {
            lock.unlock();
        }
    }

    private void updateLoginResponse() {
        LoginRequest loginRequest = new LoginRequest(litmusConfig.getUsername(), litmusConfig.getPassword());

        lock.lock();
        try {
            this.loginResponse = authorizationClient.login(loginRequest);
            this.updatedAt = System.currentTimeMillis();
        } catch (Exception e) {
            throw new RuntimeException(e); // TODO: create business exception
        } finally {
            lock.unlock();
        }
    }

    public Boolean tokenShouldBeUpdated() {
        if (this.loginResponse == null) {
            return true;
        }

        Long currentTimeSeconds = System.currentTimeMillis() / 1000;
        Long updatedAtSeconds = this.updatedAt / 1000;
        return currentTimeSeconds - updatedAtSeconds >= this.loginResponse.getExpiresIn();
    }
}
