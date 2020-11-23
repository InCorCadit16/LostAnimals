package com.pbl.animals.services;


import com.pbl.animals.models.Post;
import com.pbl.animals.models.User;
import com.pbl.animals.models.contracts.requests.LoginRequest;
import com.pbl.animals.models.contracts.requests.RegistrationRequest;
import com.pbl.animals.models.contracts.responses.LoginResponse;
import com.pbl.animals.models.contracts.responses.RegistrationResponse;
import com.pbl.animals.models.inner.SpeciesLookup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

interface LostAnimalsApi {

    // Auth methods

    @POST("auth/register")
    Call<RegistrationResponse> register(@Body RegistrationRequest request);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("auth/logout")
    Call<Void> logout();

    @GET("auth/user")
    Call<User> getUser();

    // Species
    @GET("species")
    Call<SpeciesLookup> getSpecies();

    // Posts

    @GET("post")
    Call<List<Post>> getPosts(@Query("forMap") boolean forMap);

    @POST("post")
    Call<Void> makePost(Post post);
}
