package com.personal_game.masoi.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitServer {
    private static Retrofit retrofit;
    private static String BASE_URL = "https://game.covid21tsp.space/api/";

    public static Retrofit getRetrofit_lib() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
