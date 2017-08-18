package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.SearchFollowingUserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 5/23/2017.
 */

public class SearchFollowingPresenter {
    private SearchFollowingCallback searchFollowingCallback;
    private AppCommonCallback screen;

    public SearchFollowingPresenter(AppCommonCallback callback, SearchFollowingCallback searchFollowingCallback) {
        this.screen = callback;
        this.searchFollowingCallback = searchFollowingCallback;
    }

    public interface SearchFollowingCallback {
        void searchFollowingnError(Call<SearchFollowingUserResponse> call, Throwable t);

        void onSearchFollowingSuccess(Response<SearchFollowingUserResponse> response);
    }

    public void responseCheck(String userId, String username ) {
        screen.setProgressBar();
        Call<SearchFollowingUserResponse> response = GetRestAdapter.getRestAdapter(true).searchFollowings(userId, username);
        response.enqueue(new Callback<SearchFollowingUserResponse>() {
            @Override
            public void onResponse(Call<SearchFollowingUserResponse> call, Response<SearchFollowingUserResponse> response) {
                screen.dismiss();
                searchFollowingCallback.onSearchFollowingSuccess(response);
            }

            @Override
            public void onFailure(Call<SearchFollowingUserResponse> call, Throwable t) {
                screen.dismiss();
                searchFollowingCallback.searchFollowingnError(call, t);
            }
        });
    }
}

