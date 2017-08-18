package com.nvcomputers.ten.presenter;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.output.PostCountResponse;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.core.BaseFragment;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * This class is used to get comments,like and reposts counts
 */


public class LikeCommentsCountsPresenter {

    private LikeCommentsCallback likeCommentsCallback;
    private BaseActivity mBaseActivity;
    private BaseFragment mBaseFragment;

    public interface LikeCommentsCallback {
        void getCounts(int position, PostCountResponse postCountResponse);
        void countsError(String error);
    }

    public LikeCommentsCountsPresenter(BaseActivity baseActivity, LikeCommentsCallback likeCommentsCallback) {
        this.mBaseActivity = baseActivity;
        this.likeCommentsCallback = likeCommentsCallback;
    }

    public LikeCommentsCountsPresenter(BaseFragment baseFragment, LikeCommentsCallback likeCommentsCallback) {
        this.likeCommentsCallback = likeCommentsCallback;
        this.mBaseFragment = baseFragment;
        this.mBaseActivity = baseFragment.getBaseActivity();
    }


    /**
     * This method is used to get the like,comments,repost count
     *
     * @param idPost:post id
     */
    public void countsApi(final int  position,String idPost) {
        if (!Utilities.checkInternet(mBaseActivity)) {
            mBaseActivity.showToast(R.string.no_internet_msg);
        } else {
            Call<PostCountResponse> response = GetRestAdapter.getRestAdapter(true).getPostsCount(idPost);
            response.enqueue(new Callback<PostCountResponse>() {
                @Override
                public void onResponse(Call<PostCountResponse> call,
                                       retrofit2.Response<PostCountResponse> response) {
                    if (response != null && response.body() != null && response.body().getPost() != null) {
                        likeCommentsCallback.getCounts(position,response.body());
                    }
                }

                @Override
                public void onFailure(Call<PostCountResponse> call, Throwable t) {
                    mBaseActivity.showToast(t.getMessage());
                }
            });
        }
    }

}
