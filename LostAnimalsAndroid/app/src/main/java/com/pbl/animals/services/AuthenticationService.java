package com.pbl.animals.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.pbl.animals.R;
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

    public void register(RegistrationRequest request, Callback<RegistrationResponse> callback) {
        api.register(request).enqueue(callback);
    }

    public void login(LoginRequest request, Callback<LoginResponse> callback) {
        api.login(request).enqueue(callback);
    }

    public void logout(Callback<Void> callback) {
        api.logout().enqueue(callback);
    }

    public void getUser(Callback<User> callback) {
        api.getUser().enqueue(callback);
    }

    public void saveToken(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(ctx.getString(R.string.auth_file_name), Context.MODE_PRIVATE);
        preferences.edit().putString(ctx.getString(R.string.token_name),token).apply();
    }

    public String retrieveToken(Context ctx) {
        SharedPreferences preferences = ctx.getSharedPreferences(ctx.getString(R.string.auth_file_name), Context.MODE_PRIVATE);
        token = preferences.getString(ctx.getString(R.string.token_name), null);
        return token;
    }
}
