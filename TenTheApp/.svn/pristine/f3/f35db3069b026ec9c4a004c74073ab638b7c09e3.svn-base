package com.nvcomputers.ten.views.home;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.TimersAdapter;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.output.TimersResponse;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.post.CameraActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class TimersActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mTimersRecylerView;
    private TextView mNoUsersTextView;
    private ProgressBar mProgressbar;
    private String postId;
    private Bundle bundle;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timer;
    }

    @Override
    protected void initViews() {
        bundle = getIntent().getExtras();
        postId = bundle.getString("postId");
        mProgressbar = (ProgressBar) findViewById(R.id.progress_bar_timers);
        mNoUsersTextView = (TextView) findViewById(R.id.tv_no_users_found);
        mTimersRecylerView = (RecyclerView) findViewById(R.id.recycler_view_timers);
        findViewById(R.id.tv_back).setOnClickListener(this);
        CameraActivity activity = new CameraActivity();
        activity.getData();
        activity.getFinalMethod();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!Utilities.checkInternet(this)) {
            showToast(R.string.no_internet_msg);
        } else {
            callApi();
        }

    }

    private void callApi() {
        showDialog();
        Call<TimersResponse> response = GetRestAdapter.getRestAdapter(true).getTimers(postId);
        response.enqueue(new Callback<TimersResponse>() {
            @Override
            public void onResponse(Call<TimersResponse> call, retrofit2.Response<TimersResponse> response) {
                hideDialog();
                if (response != null && response.body() != null) {
                    mNoUsersTextView.setVisibility(View.GONE);
                    setAdapter(mTimersRecylerView, response.body().getUsers());
                } else {
                    mNoUsersTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TimersResponse> call, Throwable t) {
                hideDialog();
                showToast(t.getMessage());
                mNoUsersTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override

    public void showDialog() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        mProgressbar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void dispose() {

    }


    @Override
    public void setAdapter(RecyclerView recyclerView, ArrayList<?> mList) {
        super.setAdapter(recyclerView, mList);
        if (mList != null && mList.size() > 0) {
            mNoUsersTextView.setVisibility(View.INVISIBLE);
            TimersAdapter timersAdapter = new TimersAdapter(this, mList);
            mTimersRecylerView.setAdapter(timersAdapter);
        } else {
            mNoUsersTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_back) {
            finish();
        }
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }
}
