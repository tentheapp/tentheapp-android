package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.LogoutResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 4/28/2017.
 */

public class LogoutPresenter {
    private final LogoutCallback logoutCallback;
    private AppCommonCallback screen;

    public LogoutPresenter(AppCommonCallback callback, LogoutCallback logoutCallback) {
        this.screen = callback;
        this.logoutCallback = logoutCallback;
    }

    public interface LogoutCallback {
        void logoutError(Call<LogoutResponse> call, Throwable t);

        void onLogoutSuccess(Response<LogoutResponse> response);
    }

    public void responseCheck(String userName, String password) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Call<LogoutResponse> response = GetRestAdapter.getRestAdapter(true).logoutUser();
        response.enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                GetRestAdapter.retrofitInterface=null;
                screen.dismiss();
                logoutCallback.onLogoutSuccess(response);
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                screen.dismiss();
                logoutCallback.logoutError(call, t);
            }
        });
    }
}
