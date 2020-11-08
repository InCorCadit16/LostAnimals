package com.pbl.animals.services;

import android.content.Context;

import com.pbl.animals.models.User;
import com.pbl.animals.models.contracts.requests.LoginRequest;
import com.pbl.animals.models.contracts.requests.RegistrationRequest;
import com.pbl.animals.models.contracts.responses.LoginResponse;
import com.pbl.animals.models.contracts.responses.RegistrationResponse;

import retrofit2.Callback;

public class AuthenticationService {
    private static AuthenticationService instance;
    private static LostAnimalsApi api;

    public String token;
    public User user;

    private AuthenticationService() {}

    public static AuthenticationService getAuthenticationService(Context ctx) {
        if (instance == null) {
            instance = new AuthenticationService();
        }

        if (api == null) {
            api = LostAnimalsApiService.getApiService(ctx);
        }

        return instance;
    }

    public void Register(RegistrationRequest request, Callback<RegistrationResponse> callback) {
        api.register(request).enqueue(callback);
    }

    public void Login(LoginRequest request, Callback<LoginResponse> callback) {
        api.login(request).enqueue(callback);
    }

    public void Logout(Callback<Void> callback) {
        api.logout().enqueue(callback);
    }
}
