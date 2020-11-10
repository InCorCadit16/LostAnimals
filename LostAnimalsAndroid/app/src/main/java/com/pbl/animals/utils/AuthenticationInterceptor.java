package com.pbl.animals.utils;

import android.content.Context;

import com.pbl.animals.services.AuthenticationService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthenticationInterceptor implements Interceptor {
    private Context ctx;


    public AuthenticationInterceptor(Context ctx) {
        this.ctx = ctx;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        AuthenticationService service = AuthenticationService.getAuthenticationService(ctx);
        String token = service.retrieveToken(ctx);
        Request request = chain.request();

        if (token == null) {
            return chain.proceed(request);
        }

        request = request.newBuilder()
                .addHeader("Authorization", "Bearer " + service.token)
                .build();

        return chain.proceed(request);
    }

}
