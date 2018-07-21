package com.winterparadox.themovieapp.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Builds an api interface
 */
public class ApiBuilder {

    private static final String Base_URL = "https://api.themoviedb.org/3/";

    public static <T> T build (boolean test, Class<T> apiInterface) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder ();
        httpClient.connectTimeout (5, TimeUnit.MINUTES)
                .readTimeout (5, TimeUnit.MINUTES);
        if ( test ) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor ();
            logging.setLevel (HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor (logging);
        }
        OkHttpClient OkHttpClient = httpClient.build ();
        Retrofit retrofit = new Retrofit
                .Builder ()
                .baseUrl (Base_URL).client (OkHttpClient)
                .addConverterFactory (GsonConverterFactory.create ())
                .addCallAdapterFactory (RxErrorHandlingCallAdapterFactory.create ())
                .build ();
        return retrofit.create (apiInterface);


    }


}

