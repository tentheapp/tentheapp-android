package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.PhoneVerifyInput;
import com.nvcomputers.ten.model.output.PhoneVerifyResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 4/25/2017.
 */

public class PhoneVerifyPresenter {

    private final ResultCallbacks.PhoneVerifyCallback phoneVerifyCallback;
    private AppCommonCallback screen;

    public PhoneVerifyPresenter(AppCommonCallback callback, ResultCallbacks.PhoneVerifyCallback phoneVerifyCallback) {
        this.screen = callback;
        this.phoneVerifyCallback = phoneVerifyCallback;
    }

    public void responseCheck(final PhoneVerifyInput phoneVerifyInput) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(phoneVerifyInput);
        Log.d("", "-Parameters->" + values);
        Call<PhoneVerifyResponse> response = GetRestAdapter.getRestAdapter(false).phoneVerify(phoneVerifyInput);
        response.enqueue(new Callback<PhoneVerifyResponse>() {
            @Override
            public void onResponse(Call<PhoneVerifyResponse> call, retrofit2.Response<PhoneVerifyResponse> response) {
                screen.dismiss();
                phoneVerifyCallback.onPhoneVerifySuccess(response);
            }

            @Override
            public void onFailure(Call<PhoneVerifyResponse> call, Throwable t) {
                screen.dismiss();
                phoneVerifyCallback.phoneVerifyError(call, t);
            }
        });
    }
}

