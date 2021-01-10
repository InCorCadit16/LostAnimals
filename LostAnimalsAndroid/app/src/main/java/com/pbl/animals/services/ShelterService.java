package com.pbl.animals.services;

import android.content.Context;

import com.pbl.animals.models.Shelter;

import retrofit2.Callback;

public class ShelterService {
    private static ShelterService instance;
    private static LostAnimalsApi api;


    private ShelterService() {}

    public static ShelterService getShelterService(Context ctx) {
        if (instance == null) {
            instance = new ShelterService();
        }

        if (api == null) {
            api = LostAnimalsApiService.getApiService(ctx);
        }

        return instance;
    }

    public void getShelters(boolean forMap, Callback<Shelter[]> callback) { api.getShelters(forMap).enqueue(callback); }

    public void getShelterById(long id, Callback<Shelter> callback) { api.getShelterById(id).enqueue(callback); }
}
