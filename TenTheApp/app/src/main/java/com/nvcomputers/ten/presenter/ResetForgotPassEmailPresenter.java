package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.ResetForgotPassEmailInput;
import com.nvcomputers.ten.model.output.ResetForgotPasswordResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by thaparsneh on 5/2/2017.
 */

public class ResetForgotPassEmailPresenter {
    private ResetForgotPassEmailCallback resetForgotPassEmailCallback;
    private AppCommonCallback screen;

    public ResetForgotPassEmailPresenter(AppCommonCallback callback, ResetForgotPassEmailCallback resetForgotPassEmailCallback) {
        this.screen = callback;
        this.resetForgotPassEmailCallback = resetForgotPassEmailCallback;
    }

    public interface ResetForgotPassEmailCallback {
        void resetPasswordEmailError(Call<ResetForgotPasswordResponse> call, Throwable t);

        void onResetPasswordEmailSuccess(Response<ResetForgotPasswordResponse> response);
    }

    public void responseCheck(ResetForgotPassEmailInput resetForgotPassEmailInput) {
        //screen.setProgressBar();
        Call<ResetForgotPasswordResponse> response = GetRestAdapter.getRestAdapter(false).resetforgotPassEmail(resetForgotPassEmailInput);
        response.enqueue(new Callback<ResetForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ResetForgotPasswordResponse> call, Response<ResetForgotPasswordResponse> response) {
                //screen.dismiss();
                resetForgotPassEmailCallback.onResetPasswordEmailSuccess(response);
            }

            @Override
            public void onFailure(Call<ResetForgotPasswordResponse> call, Throwable t) {
                //screen.dismiss();
                resetForgotPassEmailCallback.resetPasswordEmailError(call, t);
            }
        });
    }
}
