package com.pbl.animals.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pbl.animals.models.User;
import com.pbl.animals.services.AuthenticationService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity {

    protected AuthenticationService authService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        authService = AuthenticationService.getAuthenticationService(this);
        authService.retrieveToken(this);

        if (authService.token != null) {
            if (authService.user == null) {
                tryLoadUser();
            }
        } else {
            goToLogin();
        }
    }

    protected void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.finish();
        startActivity(intent);
    }


    protected void tryLoadUser() {
        String token = authService.retrieveToken(this);
        if (token != null) {
            authService.getUser(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        authService.user = response.body();
                    } else {
                        goToLogin();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("LOGIN", t.getMessage());
                }
            });
        }
    }
}
