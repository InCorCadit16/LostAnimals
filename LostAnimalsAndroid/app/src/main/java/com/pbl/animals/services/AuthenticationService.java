package com.pbl.animals.services;

public class AuthenticationService {
    private static AuthenticationService instance;

    private AuthenticationService() {}

    public static AuthenticationService getAuthenticationService() {
        if (instance == null) {
            instance = new AuthenticationService();
        }

        return instance;
    }



}
