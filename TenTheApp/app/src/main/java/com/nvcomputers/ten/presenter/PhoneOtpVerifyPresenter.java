package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.PhoneOtpVerifyInput;
import com.nvcomputers.ten.model.output.PhoneOtpVerifyResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 4/25/2017.
 */

public class PhoneOtpVerifyPresenter {
    private final ResultCallbacks.PhoneOtpVerifyCallback phoneOtpVerifyCallback;
    private AppCommonCallback screen;

    public PhoneOtpVerifyPresenter(AppCommonCallback callback, ResultCallbacks.PhoneOtpVerifyCallback phoneOtpVerifyCallback) {
        this.screen = callback;
        this.phoneOtpVerifyCallback = phoneOtpVerifyCallback;
    }

    public void responseCheck(final PhoneOtpVerifyInput phoneOtpVerifyInput) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(phoneOtpVerifyInput);
        Log.d("", "-Parameters->" + values);
        //output body-PhoneOtpVerifyResponse
        Call<PhoneOtpVerifyResponse> response = GetRestAdapter.getRestAdapter(false).phoneOtpVerify(phoneOtpVerifyInput);
        response.enqueue(new Callback<PhoneOtpVerifyResponse>() {
            @Override
            public void onResponse(Call<PhoneOtpVerifyResponse> call, retrofit2.Response<PhoneOtpVerifyResponse> response) {
                screen.dismiss();
                phoneOtpVerifyCallback.onPhoneOtpSuccess(response);
            }

            @Override
            public void onFailure(Call<PhoneOtpVerifyResponse> call, Throwable t) {
                screen.dismiss();
                phoneOtpVerifyCallback.phoneOtpError(call, t);
            }
        });
    }
}


