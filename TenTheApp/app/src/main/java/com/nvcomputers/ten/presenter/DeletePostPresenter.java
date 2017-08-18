package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.DeletePostResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 5/9/2017.
 */

public class DeletePostPresenter {
    private DeletePostCallback deletePostCallback;
    private AppCommonCallback screen;

    public DeletePostPresenter(AppCommonCallback callback, DeletePostCallback deletePostCallback) {
        this.screen = callback;
        this.deletePostCallback = deletePostCallback;
    }

    public interface DeletePostCallback {
        void deletePostError(Call<DeletePostResponse> call, Throwable t);

        void onDeletePostSuccess(Response<DeletePostResponse> response);
    }

    public void responseCheck(String postId) {
        //screen.setProgressBar();
        Call<DeletePostResponse> response = GetRestAdapter.getRestAdapter(true).deletePost(postId);
        response.enqueue(new Callback<DeletePostResponse>() {
            @Override
            public void onResponse(Call<DeletePostResponse> call, Response<DeletePostResponse> response) {
                //screen.dismiss();
                deletePostCallback.onDeletePostSuccess(response);
            }

            @Override
            public void onFailure(Call<DeletePostResponse> call, Throwable t) {
                //screen.dismiss();
                deletePostCallback.deletePostError(call, t);
            }
        });
    }
}