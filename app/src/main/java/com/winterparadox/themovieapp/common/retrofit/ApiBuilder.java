package com.winterparadox.themovieapp.common.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Builds an api interface
 */
public class ApiBuilder {

    private static final String Base_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "ffd597419be5a256066dc51c49bc659f";
    public static final String IMAGE = "https://image.tmdb.org/t/p/";
    public static final String SMALL_POSTER = "w342";
    public static final String MEDIUM_BACKDROP = "w780";

    public static <T> T build (boolean test, Class<T> apiInterface) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder ();
        httpClient.connectTimeout (5, TimeUnit.MINUTES)
                .readTimeout (5, TimeUnit.MINUTES);
        if ( test ) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor ();
            logging.setLevel (HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor (logging);
        }

        httpClient.addInterceptor (chain -> {
            Request request = chain.request ();
            HttpUrl url = request.url ().newBuilder ()
                    .addQueryParameter ("api_key", API_KEY)
                    .build ();
            request = request.newBuilder ().url (url).build ();
            return chain.proceed (request);
        });

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

