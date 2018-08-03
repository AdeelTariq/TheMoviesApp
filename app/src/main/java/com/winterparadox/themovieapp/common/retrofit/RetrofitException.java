package com.winterparadox.themovieapp.common.retrofit;

import java.io.IOException;

/**
 * Created by Adeel on 1/16/2017.
 */

public class RetrofitException {

    public static Throwable error (Throwable exception) {
        if ( exception instanceof IOException ) {
            return new Exception ("There was a network problem\nPlease try again later",
                    exception);
        }
        return new Exception ("An unexpected error has occurred", exception);
    }
}