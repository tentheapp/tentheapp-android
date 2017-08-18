package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.RepostPostResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 5/10/2017.
 */

public class RepostPostPresenter {
    private final RepostCallback repostCallback;
    private AppCommonCallback screen;

    public RepostPostPresenter(AppCommonCallback callback, RepostCallback repostCallback) {
        this.screen = callback;
        this.repostCallback = repostCallback;
    }

    public interface RepostCallback {
        void repostError(Call<RepostPostResponse> call, Throwable t);

        void onRepostSuccess(Response<RepostPostResponse> response);
    }

    public void responseCheck(String postId) {//input body-User LoginInput loginInput
        screen.setProgressBar();
        Call<RepostPostResponse> response = GetRestAdapter.getRestAdapter(true).repost(postId);
        response.enqueue(new Callback<RepostPostResponse>() {
            @Override
            public void onResponse(Call<RepostPostResponse> call, Response<RepostPostResponse> response) {
                screen.dismiss();
                repostCallback.onRepostSuccess(response);
            }

            @Override
            public void onFailure(Call<RepostPostResponse> call, Throwable t) {
                screen.dismiss();
                repostCallback.repostError(call, t);
            }
        });
    }
}
