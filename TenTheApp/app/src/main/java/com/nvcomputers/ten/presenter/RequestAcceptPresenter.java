package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.AcceptRequestResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 5/15/2017.
 */

public class RequestAcceptPresenter {
    private RequestAcceptCallback requestAcceptCallback;
    private AppCommonCallback screen;

    public RequestAcceptPresenter(AppCommonCallback callback, RequestAcceptCallback requestAcceptCallback) {
        this.screen = callback;
        this.requestAcceptCallback = requestAcceptCallback;
    }

    public interface RequestAcceptCallback {
        void requestAcceptError(Call<AcceptRequestResponse> call, Throwable t);

        void onRequestAcceptSuccess(Response<AcceptRequestResponse> response);
    }

    public void responseCheck(String userId) {
        //screen.setProgressBar();
        Call<AcceptRequestResponse> response = GetRestAdapter.getRestAdapter(true).acceptRequest(userId);
        response.enqueue(new Callback<AcceptRequestResponse>() {
            @Override
            public void onResponse(Call<AcceptRequestResponse> call, Response<AcceptRequestResponse> response) {
                //screen.dismiss();
                requestAcceptCallback.onRequestAcceptSuccess(response);
            }

            @Override
            public void onFailure(Call<AcceptRequestResponse> call, Throwable t) {
                //screen.dismiss();
                requestAcceptCallback.requestAcceptError(call, t);
            }
        });
    }
}