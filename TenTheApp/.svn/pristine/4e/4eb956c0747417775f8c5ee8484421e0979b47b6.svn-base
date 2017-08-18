package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.ResendPhoneOtpInput;
import com.nvcomputers.ten.model.output.ResendPhoneOtpResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 4/25/2017.
 */

public class ResendPhoneOtpPresenter {
    private final ResultCallbacks.ResendPhoneOtpCallback resendPhoneOtpCallback;
    private AppCommonCallback screen;

    public ResendPhoneOtpPresenter(AppCommonCallback callback, ResultCallbacks.ResendPhoneOtpCallback resendPhoneOtpCallback) {
        this.screen = callback;
        this.resendPhoneOtpCallback = resendPhoneOtpCallback;
    }

    public void responseCheck(final ResendPhoneOtpInput resendPhoneOtpInput) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(resendPhoneOtpInput);
        Log.d("", "-Parameters->" + values);
        //output body-ResendPhoneOtpResponse
        Call<ResendPhoneOtpResponse> response = GetRestAdapter.getRestAdapter(false).resendPhoneOtp(resendPhoneOtpInput);
        response.enqueue(new Callback<ResendPhoneOtpResponse>() {
            @Override
            public void onResponse(Call<ResendPhoneOtpResponse> call, retrofit2.Response<ResendPhoneOtpResponse> response) {
                screen.dismiss();
                resendPhoneOtpCallback.onResendPhoneOtpSuccess(response);
            }

            @Override
            public void onFailure(Call<ResendPhoneOtpResponse> call, Throwable t) {
                screen.dismiss();
                resendPhoneOtpCallback.resendPhoneOtpError(call, t);
            }
        });
    }
}

