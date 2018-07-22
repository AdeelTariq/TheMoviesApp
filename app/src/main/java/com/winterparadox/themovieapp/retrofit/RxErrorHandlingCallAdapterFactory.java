package com.winterparadox.themovieapp.retrofit;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.HttpException;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by Adeel on 1/16/2017.
 */

public class RxErrorHandlingCallAdapterFactory extends CallAdapter.Factory {
    private final RxJava2CallAdapterFactory original;

    private RxErrorHandlingCallAdapterFactory () {
        original = RxJava2CallAdapterFactory.create ();
    }

    public static CallAdapter.Factory create () {
        return new RxErrorHandlingCallAdapterFactory ();
    }

    @Override
    public CallAdapter<?, ?> get (Type returnType, Annotation[] annotations, Retrofit retrofit) {
        return new RxCallAdapterWrapper (retrofit, original.get (returnType, annotations,
                retrofit));
    }

    private static class RxCallAdapterWrapper implements CallAdapter<Call, Single<?>> {
        private final Retrofit retrofit;
        private final CallAdapter<?, ?> wrapped;
        private static Gson gson = new Gson ();

        public RxCallAdapterWrapper (Retrofit retrofit, CallAdapter<?, ?> wrapped) {
            this.retrofit = retrofit;
            this.wrapped = wrapped;
        }

        @Override
        public Type responseType () {
            return wrapped.responseType ();
        }

        @SuppressWarnings("unchecked")
        @Override
        public Single<?> adapt (Call call) {
            return ((Single) wrapped.adapt (call)).onErrorResumeNext ((throwable) -> {
                try {
                    return Single.error (asRetrofitException ((Throwable) throwable));
                } catch ( Exception e ) {
                    return Single.error (e);
                }

            });
        }

        private Throwable asRetrofitException (Throwable throwable) throws Exception {
            // We had non-200 http error
            if ( throwable instanceof HttpException ) {
                HttpException httpException = (HttpException) throwable;
                String responseBody = httpException.response ().errorBody ().string ();
                ErrorResponse errorResponse = gson.fromJson (responseBody, ErrorResponse.class);
                return new Throwable (errorResponse.statusMessage, throwable);
            }
            return RetrofitException.error (throwable);
        }
    }
}