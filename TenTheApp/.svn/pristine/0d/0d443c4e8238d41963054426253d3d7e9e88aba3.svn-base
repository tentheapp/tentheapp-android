package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.output.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 4/21/2017.
 */

public class LoginPresenter {
    private final ResultCallbacks.LoginCallback loginCallback;
    private AppCommonCallback screen;

    public LoginPresenter(AppCommonCallback callback, ResultCallbacks.LoginCallback loginCallback) {
        this.screen = callback;
        this.loginCallback = loginCallback;
    }

    public void responseCheck(String userName, String password) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Call<LoginResponse> response = GetRestAdapter.getRestAdapter(true).loginUser(userName, password);
        response.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                screen.dismiss();
                loginCallback.onLoginSuccess(response);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                screen.dismiss();
                loginCallback.loginError(call, t);
            }
        });
    }
}
