package com.nvcomputers.ten.presenter;

import android.util.Log;

import com.google.gson.Gson;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.input.PhoneSignUpUserInput;
import com.nvcomputers.ten.model.output.SignUpUserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 4/25/2017.
 */

public class PhoneSignUpPresenter {
    private SignupCallback signUpPresenter;
    private AppCommonCallback screen;

    public PhoneSignUpPresenter(AppCommonCallback callback, SignupCallback signUpPresenter) {
        this.screen = callback;
        this.signUpPresenter = signUpPresenter;
    }

    public interface SignupCallback {
        void signUpError(Call<SignUpUserResponse> call, Throwable t);

        void onSignUpSuccess(Response<SignUpUserResponse> response);
    }

    public void responseCheck(final PhoneSignUpUserInput signUpUserInput) {   //input body-User LoginInput loginInput
        screen.setProgressBar();
        Gson gson = new Gson();
        String values = gson.toJson(signUpUserInput);
        Log.d("", "-Parameters->" + values);
        //output body-SignUpUserResponse
        Call<SignUpUserResponse> response = GetRestAdapter.getRestAdapter(false).phoneSignUpUser(signUpUserInput);
        response.enqueue(new Callback<SignUpUserResponse>() {
            @Override
            public void onResponse(Call<SignUpUserResponse> call, Response<SignUpUserResponse> response) {
                screen.dismiss();
                signUpPresenter.onSignUpSuccess(response);
            }

            @Override
            public void onFailure(Call<SignUpUserResponse> call, Throwable t) {
                screen.dismiss();
                signUpPresenter.signUpError(call, t);
            }
        });
    }

}
