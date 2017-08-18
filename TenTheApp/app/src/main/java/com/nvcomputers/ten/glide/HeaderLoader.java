package com.nvcomputers.ten.glide;

import android.content.Context;

import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.nvcomputers.ten.views.TTApplication;

import okhttp3.Credentials;

/**
 * Created by Paulina Sadowska on 04.09.2016.
 */

class HeaderLoader extends BaseGlideUrlLoader<String> {

    private static String AUTHORIZATION = "QWERTYUIOPASDFGHJKL";
    private static Headers REQUEST_HEADERS = new LazyHeaders.Builder()
            .addHeader("Authorization", AUTHORIZATION)
            .build();

    HeaderLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(String model, int width, int height) {
        return model;
    }

    @Override
    protected Headers getHeaders(String model, int width, int height) {
        String authToken = Credentials.basic(TTApplication.userName, TTApplication.password);
        AUTHORIZATION = authToken;
        REQUEST_HEADERS = new LazyHeaders.Builder()
                .addHeader("Authorization", AUTHORIZATION)
                .build();
        return REQUEST_HEADERS;
    }
}