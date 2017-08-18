package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.TopLikersResponse;
import com.nvcomputers.ten.views.core.BaseActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class is used to get profile response
 *
 * @author jindaldipanshu
 * @version 1.0
 */

public class ProfilePresenter {
    private final UsersProfileCallback usersProfileCallback;
    private BaseActivity baseActivity;

    /**
     * @param baseActivity:context of {@link BaseActivity}
     */
    public ProfilePresenter(BaseActivity baseActivity, UsersProfileCallback usersProfileCallback) {
        this.baseActivity = baseActivity;
        this.usersProfileCallback = usersProfileCallback;
    }

    public interface UsersProfileCallback {

        /*void setHorizontalAdapter(ArrayList<TopLikersResponse.Users> profileResponse);
        void setUsersError(String error);*/

        void setPostsAdapter(Response<NewsFeedOutput> list);

        void setPostsError(String error);

        void onActivePostError(Call<NewsFeedOutput> call, Throwable t);

        void onActivePostSuccess(Response<NewsFeedOutput> response);
    }


    /*public void getTopProfilesResponse(String username) {
        Call<TopLikersResponse> response = GetRestAdapter.getRestAdapter(true).likeResponse(username);
        response.enqueue(new Callback<TopLikersResponse>() {
            @Override
            public void onResponse(Call<TopLikersResponse> call, Response<TopLikersResponse> response) {
                baseActivity.hideDialog();
                if (response != null && response.body() != null) {
                    if (response.body().getUsers() != null && response.body().getUsers().size() > 0) {
                        usersProfileCallback.setHorizontalAdapter(response.body().getUsers());
                    } else {
                        usersProfileCallback.setUsersError("No data found");
                    }
                } else {
                    usersProfileCallback.setUsersError("No data found");
                }
            }

            @Override
            public void onFailure(Call<TopLikersResponse> call, Throwable t) {
                baseActivity.hideDialog();
                baseActivity.showToast(t.getMessage());
                usersProfileCallback.setUsersError("No data found");
            }
        });
    }*/


    public void getPosts(String username, String offset) {
        //String localUsername=
        Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).userPagePosts(username, offset, "10");
        //Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).userPosts(username);
        response.enqueue(new Callback<NewsFeedOutput>() {
            @Override
            public void onResponse(Call<NewsFeedOutput> call, Response<NewsFeedOutput> response) {
                baseActivity.hideDialog();
                usersProfileCallback.setPostsAdapter(response);
            }

            @Override
            public void onFailure(Call<NewsFeedOutput> call, Throwable t) {
                baseActivity.hideDialog();
                usersProfileCallback.setPostsError(t.getLocalizedMessage());
                baseActivity.showToast(t.getMessage());
            }
        });
    }

    public void getActivePost(String userName) {//input body-User LoginInput loginInput
        //screen.setProgressBar();
        Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).activePosts(userName);
        response.enqueue(new Callback<NewsFeedOutput>() {
            @Override
            public void onResponse(Call<NewsFeedOutput> call, Response<NewsFeedOutput> response) {
                //screen.dismiss();
                baseActivity.hideDialog();
                usersProfileCallback.onActivePostSuccess(response);
            }

            @Override
            public void onFailure(Call<NewsFeedOutput> call, Throwable t) {
                //screen.dismiss();
                baseActivity.hideDialog();
                usersProfileCallback.onActivePostError(call, t);
            }
        });
    }

}
