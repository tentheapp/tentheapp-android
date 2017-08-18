package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.NewsFeedOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 6/2/2017.
 */

public class GetActivePostsPresenter {

    private ActivePostCallback activePostCallback;
    private AppCommonCallback screen;


    public GetActivePostsPresenter(AppCommonCallback callback, ActivePostCallback activePostCallback) {
        this.screen = callback;
        this.activePostCallback = activePostCallback;
    }

    public interface ActivePostCallback {
        void onActivePostError(Call<NewsFeedOutput> call, Throwable t);
        void onActivePostSuccess(Response<NewsFeedOutput> response);
    }

    public void hitApi(String userName) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).activePosts(userName);
        response.enqueue(new Callback<NewsFeedOutput>() {
            @Override
            public void onResponse(Call<NewsFeedOutput> call, Response<NewsFeedOutput> response) {
                screen.dismiss();
                activePostCallback.onActivePostSuccess(response);
            }

            @Override
            public void onFailure(Call<NewsFeedOutput> call, Throwable t) {
                screen.dismiss();
                activePostCallback.onActivePostError(call, t);
            }
        });
    }
}
