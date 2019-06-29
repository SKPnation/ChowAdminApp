package com.example.ayomide.chowadminapp.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FCMRetrofitClient {
    public static Retrofit retrofit = null;

    public static Retrofit getFCMClient(String baseURL)
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl( baseURL )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build();
        }
        return retrofit;
    }
}
