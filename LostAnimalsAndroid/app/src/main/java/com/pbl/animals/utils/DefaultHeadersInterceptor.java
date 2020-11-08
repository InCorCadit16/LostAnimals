package com.pbl.animals.utils;

import android.content.Context;

import com.pbl.animals.services.AuthenticationService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DefaultHeadersInterceptor implements Interceptor {

    public DefaultHeadersInterceptor() {

    }

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

         Request request = chain.request().newBuilder()
                .addHeader("Content-type", "application/json")
                .addHeader("Connection", "keep-alive")
                .build();

        return chain.proceed(request);
    }

}
