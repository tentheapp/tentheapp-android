package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.ForgotPasswordEmailInput;
import com.nvcomputers.ten.model.output.ForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thaparsneh on 5/2/2017.
 */

public class ForgotPasswordEmailPresenter {
    private ForgotPassEmailCallback forgotPassEmailCallback;
    private AppCommonCallback screen;

    public ForgotPasswordEmailPresenter(AppCommonCallback callback, ForgotPassEmailCallback forgotPassEmailCallback) {
        this.screen = callback;
        this.forgotPassEmailCallback = forgotPassEmailCallback;
    }

    public interface ForgotPassEmailCallback {
        void forgotPassEmailError(Call<ForgotPasswordResponse> call, Throwable t);

        void onForgotPassEmailSuccess(Response<ForgotPasswordResponse> response);
    }

    public void responseCheck(ForgotPasswordEmailInput forgotPasswordEmailInput) {
        //screen.setProgressBar();
        Call<ForgotPasswordResponse> response = GetRestAdapter.getRestAdapter(false).emailforgotPassword(forgotPasswordEmailInput);
        response.enqueue(new Callback<ForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                //screen.dismiss();
                forgotPassEmailCallback.onForgotPassEmailSuccess(response);
                //response.code()
            }

            @Override
            public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                //screen.dismiss();
                forgotPassEmailCallback.forgotPassEmailError(call, t);
            }
        });
    }
}

