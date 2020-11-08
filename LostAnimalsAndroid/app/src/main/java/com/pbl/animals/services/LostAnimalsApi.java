package com.pbl.animals.services;

import android.app.Activity;
import android.content.Context;

import com.pbl.animals.models.Species;
import com.pbl.animals.models.contracts.requests.LoginRequest;
import com.pbl.animals.models.contracts.requests.RegistrationRequest;
import com.pbl.animals.models.contracts.responses.LoginResponse;
import com.pbl.animals.models.contracts.responses.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

interface LostAnimalsApi {

    // Auth methods

    @POST("auth/register")
    Call<RegistrationResponse> register(@Body RegistrationRequest request);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/logout")
    Call<Void> logout();

    // Species
    @GET("species")
    Call<Species> getSpecies();
}
