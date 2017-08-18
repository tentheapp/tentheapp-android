package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.EmailVerificationInput;
import com.nvcomputers.ten.model.output.EmailVerificationResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 4/24/2017.
 */

public class VerifyEmailPresenter {

    private final ResultCallbacks.EmailVerificationCallback emailVerificationCallback;
    private AppCommonCallback screen;

    public VerifyEmailPresenter(AppCommonCallback callback, ResultCallbacks.EmailVerificationCallback emailVerificationCallback) {
        this.screen = callback;
        this.emailVerificationCallback = emailVerificationCallback;
    }

    public void responseCheck(final EmailVerificationInput emailVerificationInput) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(emailVerificationInput);
        Log.d("", "-Parameters->" + values);
        //output body-EmailVerificationResponse
        Call<EmailVerificationResponse> response = GetRestAdapter.getRestAdapter(false).verifyEmail(emailVerificationInput);
        response.enqueue(new Callback<EmailVerificationResponse>() {
            @Override
            public void onResponse(Call<EmailVerificationResponse> call, retrofit2.Response<EmailVerificationResponse> response) {
                screen.dismiss();
                emailVerificationCallback.onEmailVerifySuccess(response);
            }

            @Override
            public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                screen.dismiss();
                emailVerificationCallback.emailVerifyError(call, t);
            }
        });
    }
}

