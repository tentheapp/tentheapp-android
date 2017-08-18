package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.output.GetAllPostCommentOutput;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KaurGurleen on 5/4/2017.
 */

public class GetCommentListPresenter {
    private final CommentsCallback commentsCallback;

    public GetCommentListPresenter(CommentsCallback commentsCallback) {
        this.commentsCallback = commentsCallback;
    }

    public interface CommentsCallback {
        void getCommentListError(Call<GetAllPostCommentOutput> call, Throwable t);

        void getCommentListSuccess(Response<GetAllPostCommentOutput> response);
    }

    public void responseCheck(String postId, String count) {
        //output body-LoginResponse
        Call<GetAllPostCommentOutput> response = GetRestAdapter.getRestAdapter(true).getAllCommentList(postId, count, "10");
        response.enqueue(new Callback<GetAllPostCommentOutput>() {
            @Override
            public void onResponse(Call<GetAllPostCommentOutput> call, Response<GetAllPostCommentOutput> response) {
                commentsCallback.getCommentListSuccess(response);
            }

            @Override
            public void onFailure(Call<GetAllPostCommentOutput> call, Throwable t) {
                commentsCallback.getCommentListError(call, t);
            }
        });

    }
}
