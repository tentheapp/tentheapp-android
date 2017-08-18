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

public class NewFeedPresenter {
    private NewsFeedCallback newsFeedCallback;
    private AppCommonCallback screen;

    public NewFeedPresenter(AppCommonCallback callback, NewsFeedCallback newsFeedCallback) {
        this.screen = callback;
        this.newsFeedCallback = newsFeedCallback;
    }

    public interface NewsFeedCallback {
        void onError(Call<NewsFeedOutput> call, Throwable t);

        void onSuccess(Response<NewsFeedOutput> response);
    }

    public void hitApi(String offset) {//) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).newsFeed(offset, "10");//(offset, count);
        response.enqueue(new Callback<NewsFeedOutput>() {
            @Override
            public void onResponse(Call<NewsFeedOutput> call, Response<NewsFeedOutput> response) {
                screen.dismiss();
                newsFeedCallback.onSuccess(response);
            }

            @Override
            public void onFailure(Call<NewsFeedOutput> call, Throwable t) {
                screen.dismiss();
                newsFeedCallback.onError(call, t);
            }
        });
    }
}
