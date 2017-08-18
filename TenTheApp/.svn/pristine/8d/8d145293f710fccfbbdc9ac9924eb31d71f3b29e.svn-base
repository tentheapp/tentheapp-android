package com.nvcomputers.ten.api;

import com.nvcomputers.ten.views.TTApplication;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by KaurGurleen on 11/28/2016.
 */

public class GetRestAdapter {

    // private static final String QA = "http://dotnetstg2.seasiaconsulting.com/RconnectQA/";
    //ten-spring-qa-2.us-east-1.elasticbeanstalk.com
    private static final String UAT = "http://tentheapp-qa.com/";

    public static final String HOST_URL = UAT;
    public static GitHubService retrofitInterface;
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static GitHubService getRestAdapter(boolean isAuthRequired) {

        if (retrofitInterface == null || !isAuthRequired) {
            if (isAuthRequired && TTApplication.userName.length() > 0) {
                String authToken = Credentials.basic(TTApplication.userName, TTApplication.password);
                AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
                httpClient.addInterceptor(interceptor);
            } else {
                httpClient = new OkHttpClient.Builder();
            }
            retrofitInterface = new Retrofit.Builder()
                    .baseUrl(HOST_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
                    .create(GitHubService.class);
        }
        return retrofitInterface;
    }

    public static GitHubService getVidoRestAdapter() {
        GitHubService retrofitInterface = null;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (retrofitInterface == null) {
            if (TTApplication.userName.length() > 0) {
                String authToken = Credentials.basic(TTApplication.userName, TTApplication.password);
                AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);
                httpClient.addInterceptor(interceptor);
            }
            retrofitInterface = new Retrofit.Builder()
                    .baseUrl("http://ten-spring-qa-2.us-east-1.elasticbeanstalk.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
                    .create(GitHubService.class);
        }
        return retrofitInterface;
    }
}
