package com.pbl.animals.services;


import com.pbl.animals.models.Comment;
import com.pbl.animals.models.Post;
import com.pbl.animals.models.User;
import com.pbl.animals.models.contracts.requests.CreatePostRequest;
import com.pbl.animals.models.contracts.requests.LoginRequest;
import com.pbl.animals.models.contracts.requests.RegistrationRequest;
import com.pbl.animals.models.contracts.requests.UpdatePostRequest;
import com.pbl.animals.models.contracts.responses.LoginResponse;
import com.pbl.animals.models.contracts.responses.LookupsResponse;
import com.pbl.animals.models.contracts.responses.RegistrationResponse;
import com.pbl.animals.models.inner.SpeciesLookup;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

    @GET("post/{id}")
    Call<Post> getPostById(@Path("id") long id);

    @POST("post")
    Call<Long> createPost(@Body() CreatePostRequest request);

    @PUT("post")
    Call<Void> updatePost(@Body() UpdatePostRequest request);

    @DELETE("post/{id}")
    Call<Void> deletePost(@Path("id") long id);


    // Comments
    @GET("comments/post/{postId}")
    Call<Comment[]> getCommentsByPostId(@Path("postId") long postId);

    // Lookups
    @GET("lookup")
    Call<LookupsResponse> getLookups();

}
