package com.winterparadox.themovieapp.common.retrofit;

import android.content.Context;

import com.winterparadox.themovieapp.common.NetworkUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
    public static final String LARGE_PROFILE = "h632";

    public static <T> T build (Context context, Class<T> apiInterface, boolean test) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder ();
        httpClient.connectTimeout (5, TimeUnit.MINUTES)
                .readTimeout (5, TimeUnit.MINUTES);

        // logging
        if ( test ) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor ();
            logging.setLevel (HttpLoggingInterceptor.Level.BODY);

            httpClient.addInterceptor (logging);
        }

        // cache control
        Interceptor interceptor = chain -> {
            Request request = chain.request ();

            // Add Cache Control only for GET methods
            if ( request.method ().equals ("GET") ) {
                if ( NetworkUtils.isConnected (context) ) {
                    // 1 day
                    request = request.newBuilder ()
                            .header ("Cache-Control", "only-if-cached")
                            .build ();
                }
            }
            Response originalResponse = chain.proceed (request);
            return originalResponse.newBuilder ()
                    .header ("Cache-Control", "max-age=600")
                    .build ();
        };

        Interceptor interceptorOffline = chain -> {
            Request request = chain.request ();

            // Add Cache Control only for GET methods
            if ( request.method ().equals ("GET") ) {
                if ( !NetworkUtils.isConnected (context) ) {
                    // 4 weeks stale
                    request = request.newBuilder ()
                            .header ("Cache-Control", "public, max-stale=2419200")
                            .build ();
                }
            }
            Response originalResponse = chain.proceed (request);
            return originalResponse.newBuilder ()
                    .header ("Cache-Control", "max-age=600")
                    .build ();
        };

        httpClient.addNetworkInterceptor (interceptor);
        httpClient.addInterceptor (interceptorOffline);

        // add api key to request
        httpClient.addInterceptor (chain -> {
            Request request = chain.request ();
            HttpUrl url = request.url ().newBuilder ()
                    .addQueryParameter ("api_key", API_KEY)
                    .build ();
            request = request.newBuilder ().url (url).build ();
            return chain.proceed (request);
        });

        httpClient.cache (new Cache (context.getCacheDir (), 10 * 1024 * 1024));

        // build
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

