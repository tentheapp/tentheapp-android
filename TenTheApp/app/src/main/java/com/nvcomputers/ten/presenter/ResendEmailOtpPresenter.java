package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.ResendEmailOtpInput;
import com.nvcomputers.ten.model.output.ResendEmailOtpResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 4/25/2017.
 */

public class ResendEmailOtpPresenter {
    private final ResultCallbacks.ResendEmailOtpCallback resendEmailOtpCallback;
    private AppCommonCallback screen;

    public ResendEmailOtpPresenter(AppCommonCallback callback, ResultCallbacks.ResendEmailOtpCallback resendEmailOtpCallback) {
        this.screen = callback;
        this.resendEmailOtpCallback = resendEmailOtpCallback;
    }

    public void responseCheck(final ResendEmailOtpInput resendEmailOtpInput) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(resendEmailOtpInput);
        Log.d("", "-Parameters->" + values);
        //output body-ResendEmailOtpResponse
        Call<ResendEmailOtpResponse> response = GetRestAdapter.getRestAdapter(false).resendEmailOtp(resendEmailOtpInput);
        response.enqueue(new Callback<ResendEmailOtpResponse>() {
            @Override
            public void onResponse(Call<ResendEmailOtpResponse> call, retrofit2.Response<ResendEmailOtpResponse> response) {
                screen.dismiss();
                resendEmailOtpCallback.onResendEmailOtpSuccess(response);
            }

            @Override
            public void onFailure(Call<ResendEmailOtpResponse> call, Throwable t) {
                screen.dismiss();
                resendEmailOtpCallback.resendEmailOtpError(call, t);
            }
        });
    }
}


