package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.PopularTimersResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 4/21/2017.
 */

public class PopularTimersPresenter {
    private PopularTimersCallback populartimersCallback;
    private AppCommonCallback screen;

    public PopularTimersPresenter(AppCommonCallback callback, PopularTimersCallback populartimersCallback) {
        this.screen = callback;
        this.populartimersCallback = populartimersCallback;
    }

    public interface PopularTimersCallback {
        void onTimersError(Call<PopularTimersResponse> call, Throwable t);
        void onTimersSuccess(Response<PopularTimersResponse> response);
    }

    public void hitPopularTimersApi(String tabValue, String type) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Call<PopularTimersResponse> response = GetRestAdapter.getRestAdapter(true).popularTimers(tabValue,type);
        response.enqueue(new Callback<PopularTimersResponse>() {
            @Override
            public void onResponse(Call<PopularTimersResponse> call, Response<PopularTimersResponse> response) {
                screen.dismiss();
                populartimersCallback.onTimersSuccess(response);
            }

            @Override
            public void onFailure(Call<PopularTimersResponse> call, Throwable t) {
                screen.dismiss();
                populartimersCallback.onTimersError(call, t);
            }
        });
    }
}
