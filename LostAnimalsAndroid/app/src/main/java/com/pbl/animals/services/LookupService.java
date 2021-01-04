package com.pbl.animals.services;

import android.content.Context;

import com.pbl.animals.models.contracts.responses.LookupsResponse;

import retrofit2.Callback;

public class LookupService {
    private static LookupService instance;
    private static LostAnimalsApi api;


    private LookupService() {}

    public static LookupService getLookupService(Context ctx) {
        if (instance == null) {
            instance = new LookupService();
        }

        if (api == null) {
            api = LostAnimalsApiService.getApiService(ctx);
        }

        return instance;
    }

    public void getLookups(Callback<LookupsResponse> callback) {
        api.getLookups().enqueue(callback);
    }
}
