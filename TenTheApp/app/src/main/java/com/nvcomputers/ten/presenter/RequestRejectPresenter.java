package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.RejectRequestResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 5/15/2017.
 */

public class RequestRejectPresenter {
    private RequestRejectCallback requestRejectCallback;
    private AppCommonCallback screen;

    public RequestRejectPresenter(AppCommonCallback callback, RequestRejectCallback requestRejectCallback) {
        this.screen = callback;
        this.requestRejectCallback = requestRejectCallback;
    }

    public interface RequestRejectCallback {
        void requestRejectError(Call<RejectRequestResponse> call, Throwable t);

        void onRequestRejectSuccess(Response<RejectRequestResponse> response);
    }

    public void responseCheck(String userId) {
        //screen.setProgressBar();
        Call<RejectRequestResponse> response = GetRestAdapter.getRestAdapter(true).rejectResquest(userId);
        response.enqueue(new Callback<RejectRequestResponse>() {
            @Override
            public void onResponse(Call<RejectRequestResponse> call, Response<RejectRequestResponse> response) {
                //screen.dismiss();
                requestRejectCallback.onRequestRejectSuccess(response);
            }

            @Override
            public void onFailure(Call<RejectRequestResponse> call, Throwable t) {
                //screen.dismiss();
                requestRejectCallback.requestRejectError(call, t);
            }
        });
    }
}