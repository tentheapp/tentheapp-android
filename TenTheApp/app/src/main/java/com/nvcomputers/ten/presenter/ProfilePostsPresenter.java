package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.NewsFeedOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 7/7/2017.
 */

/*public class ProfilePostsPresenter {
    private ProfilePostsCallback profilePostsCallback;
    private AppCommonCallback screen;

    public ProfilePostsPresenter(AppCommonCallback callback, ProfilePostsCallback profilePostsCallback) {
        this.screen = callback;
        this.profilePostsCallback = profilePostsCallback;
    }

    public interface ProfilePostsCallback {
        void setPostsError(Call<NewsFeedOutput> call, Throwable t);

        void setPostsAdapter(Response<NewsFeedOutput> response);
    }

    public void responseCheck(String username, String offset) {
        //screen.setProgressBar();
        Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).userPagePosts(username, offset, "10");
        response.enqueue(new Callback<NewsFeedOutput>() {
            @Override
            public void onResponse(Call<NewsFeedOutput> call, retrofit2.Response<NewsFeedOutput> response) {
                //screen.dismiss();
                profilePostsCallback.setPostsAdapter(response);
            }

            @Override
            public void onFailure(Call<NewsFeedOutput> call, Throwable t) {
                //screen.dismiss();
                profilePostsCallback.setPostsError(call, t);
            }
        });
    }
}*/
