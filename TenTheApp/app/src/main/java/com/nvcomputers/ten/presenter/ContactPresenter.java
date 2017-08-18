package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.EmailVerificationInput;
import com.nvcomputers.ten.model.output.EmailVerificationResponse;
import com.nvcomputers.ten.model.output.ResendEmailOtpResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rkumar4 on 4/25/2017.
 */

public class ContactPresenter {

    private final AppCommonCallback screen;

    public ContactPresenter(AppCommonCallback callback, ContactCallback imageCallback) {
        this.screen = callback;
    }

    public interface ContactCallback {
        void onImageError(Call<ResendEmailOtpResponse> call, Throwable t);

        void onImageSuccess(Response<ResendEmailOtpResponse> response);
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
            public void onResponse(Call<EmailVerificationResponse> call, Response<EmailVerificationResponse> response) {
                screen.dismiss();
                //emailVerificationCallback.onEmailVerifySuccess(response);
            }

            @Override
            public void onFailure(Call<EmailVerificationResponse> call, Throwable t) {
                screen.dismiss();
                //emailVerificationCallback.emailVerifyError(call, t);
            }
        });
    }
}
