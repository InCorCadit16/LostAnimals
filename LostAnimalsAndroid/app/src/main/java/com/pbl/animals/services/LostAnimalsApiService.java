package com.pbl.animals.services;

import android.content.Context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pbl.animals.R;
import com.pbl.animals.utils.AuthenticationInterceptor;
import com.pbl.animals.utils.DefaultHeadersInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class LostAnimalsApiService {
    private static LostAnimalsApi api;

    public static LostAnimalsApi getApiService(Context ctx) {
         if (api == null) {
             final String base_url = ctx.getResources().getString(R.string.base_url);

             AuthenticationInterceptor authInterceptor = new AuthenticationInterceptor(ctx);
             DefaultHeadersInterceptor defaultInterceptor = new DefaultHeadersInterceptor();

             ObjectMapper mapper = new ObjectMapper();
             mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


             OkHttpClient client = new OkHttpClient.Builder()
                     .addInterceptor(authInterceptor)
                     .addInterceptor(defaultInterceptor)
                     .build();

             Retrofit retrofit = new Retrofit.Builder()
                     .baseUrl(base_url)
                     .client(client)
                     .addConverterFactory(JacksonConverterFactory.create(mapper))
                     .build();

             api = retrofit.create(LostAnimalsApi.class);
         }

         return api;
    }
}
