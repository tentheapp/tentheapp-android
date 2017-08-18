package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.OtpEmailVerifyInput;
import com.nvcomputers.ten.model.output.OtpEmailVerifyResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 4/24/2017.
 */

public class OtpEmailVerifyPresenter {
    private final ResultCallbacks.OtpEmailVerifyCallback otpEmailVerifyCallback;
    private AppCommonCallback screen;

    public OtpEmailVerifyPresenter(AppCommonCallback callback, ResultCallbacks.OtpEmailVerifyCallback otpEmailVerifyCallback) {
        this.screen = callback;
        this.otpEmailVerifyCallback = otpEmailVerifyCallback;
    }

    public void responseCheck(final OtpEmailVerifyInput otpEmailVerifyInput) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(otpEmailVerifyInput);
        Log.d("", "-Parameters->" + values);
        //output body-OtpEmailVerifyResponse
        Call<OtpEmailVerifyResponse> response = GetRestAdapter.getRestAdapter(false).emailOtp(otpEmailVerifyInput);
        response.enqueue(new Callback<OtpEmailVerifyResponse>() {
            @Override
            public void onResponse(Call<OtpEmailVerifyResponse> call, retrofit2.Response<OtpEmailVerifyResponse> response) {
                screen.dismiss();
                otpEmailVerifyCallback.onEmailOtpSuccess(response);
            }

            @Override
            public void onFailure(Call<OtpEmailVerifyResponse> call, Throwable t) {
                screen.dismiss();
                otpEmailVerifyCallback.emailOtpError(call, t);
            }
        });
    }
}

