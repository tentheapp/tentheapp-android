package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.ResultCallbacks;
import com.nvcomputers.ten.model.input.RegisterUsernameInput;
import com.nvcomputers.ten.model.output.RegisterUsernameOutput;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by kthakur on 4/21/2017.
 */

public class RegisterUsernamePresenter {

    private final ResultCallbacks.VerifyUserNameCallback verifyUserNameCallback;
    private AppCommonCallback screen;

    public RegisterUsernamePresenter(AppCommonCallback callback, ResultCallbacks.VerifyUserNameCallback verifyUserNameCallback) {
        this.screen = callback;
        this.verifyUserNameCallback = verifyUserNameCallback;
    }

    public void responseCheck(RegisterUsernameInput registerUsernameInput) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(registerUsernameInput);
        Log.d("", "-Parameters->" + values);
        //output body-RegisterUsernameOutput
        Call<RegisterUsernameOutput> response = GetRestAdapter.getRestAdapter(false).verifyUserName(registerUsernameInput);
        response.enqueue(new Callback<RegisterUsernameOutput>() {
            @Override
            public void onResponse(Call<RegisterUsernameOutput> call, retrofit2.Response<RegisterUsernameOutput> response) {
                screen.dismiss();
                verifyUserNameCallback.onUserNameSuccess(response);
            }

            @Override
            public void onFailure(Call<RegisterUsernameOutput> call, Throwable t) {
                screen.dismiss();
                verifyUserNameCallback.usernameError(call, t);
            }
        });
    }
}

