package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.ProfileResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 7/6/2017.
 */

public class GetUserProfilePresenter {
    private GetUserProfileCallback getUserProfileCallback;
    private AppCommonCallback screen;

    public GetUserProfilePresenter(AppCommonCallback callback, GetUserProfileCallback getUserProfileCallback) {
        this.screen = callback;
        this.getUserProfileCallback = getUserProfileCallback;
    }

    public interface GetUserProfileCallback {
        void userProfileError(Call<ProfileResponse> call, Throwable t);
        void onUserProfileSuccess(Response<ProfileResponse> response);
    }

    public void responseCheck(String userName) {
        screen.setProgressBar();
        Call<ProfileResponse> response = GetRestAdapter.getRestAdapter(true).userProfile(userName);
        response.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                screen.dismiss();
                getUserProfileCallback.onUserProfileSuccess(response);
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                screen.dismiss();
                getUserProfileCallback.userProfileError(call, t);
            }
        });
    }
}
