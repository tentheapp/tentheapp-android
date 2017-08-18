package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.NewsFeedOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 4/21/2017.
 */

public class PopularPostsPresenter {
    private PopularPostCallback popularpostsCallback;
    private AppCommonCallback screen;

    public PopularPostsPresenter(AppCommonCallback callback, PopularPostCallback popularpostsCallback) {
        this.screen = callback;
        this.popularpostsCallback = popularpostsCallback;
    }

    public interface PopularPostCallback {
        void onError(Call<NewsFeedOutput> call, Throwable t);
        void onSuccess(Response<NewsFeedOutput> response);
    }

    public void hitPopularPostsApi(String tabValue, String type) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).popularPostsTimed(tabValue,type);
        response.enqueue(new Callback<NewsFeedOutput>() {
            @Override
            public void onResponse(Call<NewsFeedOutput> call, Response<NewsFeedOutput> response) {
                screen.dismiss();
                popularpostsCallback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<NewsFeedOutput> call, Throwable t) {
                screen.dismiss();
                popularpostsCallback.onError(call, t);
            }
        });
    }
}
