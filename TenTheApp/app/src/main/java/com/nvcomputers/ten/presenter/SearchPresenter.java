package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.SearchUsersResponse;
import com.nvcomputers.ten.views.core.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by jindaldipanshu on 5/3/2017.
 */

public class SearchPresenter {


    private final SearchCallbacks mSearchCallbacks;
    private final BaseActivity mBaseActivity;

    public interface SearchCallbacks {

        void onUsersSearchResult(SearchUsersResponse searchUsersResponse);

        void onHashTagResult(NewsFeedOutput searchUsersResponse);
    }

    public SearchPresenter(BaseActivity baseActivity, SearchCallbacks searchCallbacks) {
        this.mSearchCallbacks = searchCallbacks;
        this.mBaseActivity = baseActivity;

    }
    
    public void getUserResults(String search){

        Call<SearchUsersResponse> response = GetRestAdapter.getRestAdapter(true).searchUser(search);
        response.enqueue(new Callback<SearchUsersResponse>() {
            @Override
            public void onResponse(Call<SearchUsersResponse> call, retrofit2.Response<SearchUsersResponse> response) {

                if (response!=null && response.body()!=null){
                    mSearchCallbacks.onUsersSearchResult(response.body());
                }else {
                    mBaseActivity.showToast(R.string.server_error_msg);
                }
            }

            @Override
            public void onFailure(Call<SearchUsersResponse> call, Throwable t) {
                mBaseActivity.showToast(t.getMessage());
            }
        });
        
    }


    public void getHashTagResults(String search){

        Call<NewsFeedOutput> response = GetRestAdapter.getRestAdapter(true).hashtagUser(search);
        response.enqueue(new Callback<NewsFeedOutput>() {
            @Override
            public void onResponse(Call<NewsFeedOutput> call, retrofit2.Response<NewsFeedOutput> response) {

                if (response!=null && response.body()!=null){
                    mSearchCallbacks.onHashTagResult(response.body());
                }else {
                    mBaseActivity.showToast(R.string.server_error_msg);
                }
            }

            @Override
            public void onFailure(Call<NewsFeedOutput> call, Throwable t) {
                mBaseActivity.showToast(t.getMessage());
            }
        });

    }



}
