package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.DeleteCommentResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Thaparsneh on 6/6/2017.
 */

public class DeleteCommentPresenter {
    private DeleteCommentCallback deleteCommentCallback;
    private AppCommonCallback screen;

    public DeleteCommentPresenter(AppCommonCallback callback, DeleteCommentCallback deleteCommentCallback) {
        this.screen = callback;
        this.deleteCommentCallback = deleteCommentCallback;
    }

    public interface DeleteCommentCallback {
        void deleteCommentError(Call<DeleteCommentResponse> call, Throwable t);

        void onDeleteCommentSuccess(Response<DeleteCommentResponse> response);
    }

    public void responseCheck(String postId, String commentId) {
        //screen.setProgressBar();
        Call<DeleteCommentResponse> response = GetRestAdapter.getRestAdapter(true).deleteComment(postId, commentId);
        response.enqueue(new Callback<DeleteCommentResponse>() {
            @Override
            public void onResponse(Call<DeleteCommentResponse> call, Response<DeleteCommentResponse> response) {
                //screen.dismiss();
                deleteCommentCallback.onDeleteCommentSuccess(response);
            }

            @Override
            public void onFailure(Call<DeleteCommentResponse> call, Throwable t) {
                //screen.dismiss();
                deleteCommentCallback.deleteCommentError(call, t);
            }
        });
    }
}

