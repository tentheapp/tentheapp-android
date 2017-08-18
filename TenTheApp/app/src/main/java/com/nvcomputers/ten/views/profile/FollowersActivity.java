package com.nvcomputers.ten.views.profile;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.FollowersAdapter;
import com.nvcomputers.ten.model.output.FollowingResponse;
import com.nvcomputers.ten.presenter.FollowersPresenter;
import com.nvcomputers.ten.utils.ProgressUtility;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;

import java.util.ArrayList;

public class FollowersActivity extends BaseActivity implements View.OnClickListener, FollowersPresenter.FollowersCallback {

    private RecyclerView mFollowersRecyclerView;
    private String mUserId;
    private FollowersPresenter mFollowersPresenter;
    private String mFollowers, mUserName;
    private TextView mFollowersTitleTextView, mNameTitleTextView;
    private String offset, count;
    private ArrayList<FollowingResponse.Users> followersList;
    private FollowersAdapter followersAdapter;

    private LinearLayoutManager manager;
    int lastSavedPosition = 0;
    boolean noMoreData = false;
    int currentPageNumber = 1;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_followers;
    }

    @Override
    protected void initViews() {
        mUserId = getIntent().getStringExtra("user_id");
        mFollowers = getIntent().getStringExtra("followers");
        mUserName = getIntent().getStringExtra("name");
        mNameTitleTextView = (TextView) findViewById(R.id.text_view_name_followers);
        mFollowersTitleTextView = (TextView) findViewById(R.id.text_view_title_followers);
        if (mFollowers.equals("1")) { // following
            mFollowersTitleTextView.setText("Following");
        } else {
            mFollowersTitleTextView.setText("Follow");
        }

        mFollowersRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_followers);

        manager = new LinearLayoutManager(this);
        mFollowersRecyclerView.setLayoutManager(manager);

        findViewById(R.id.text_view_name_followers).setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!Utilities.checkInternet(this)) {
            showToast(R.string.no_internet_msg);
        } else {
//            showDialog();
//            if (mFollowersPresenter == null)
//                mFollowersPresenter = new FollowersPresenter(this, this);
//
//            if (mFollowers.equals("1")) {
//                mFollowersPresenter.getFollowingResponse(mUserId, offset, count);
//            } else {
//                mFollowersPresenter.getFollowersResponse(mUserId, offset, count);
//            }

            loadPaginationData(0);
        }
    }


    public void loadPaginationData(int listCount) {
        if (listCount == 0) {
            currentPageNumber = 1;
            lastSavedPosition = 0;
            noMoreData = false;
            followersList = new ArrayList<>();
           /* adapter = new NewsPostAdapter(this, newsFeedOutput, R.id.home_frame_layout, manager);
            adapter.isOnline(true);
            recyclerViewPosts.setAdapter(adapter);*/
            hitApi(listCount);
        } else {
            lastSavedPosition = listCount - 1;
            if (listCount >= 10 && !noMoreData && listCount % 10 == 0) {
                int value = listCount / 10;
                currentPageNumber = value + 1;
                hitApi(listCount);
            } else {
                ProgressUtility.dismissProgress();
            }
        }
    }

    boolean isLoading = false;

    private void hitApi(int listCount) {
        if (!Utilities.checkInternet(this)) {
            showToast(R.string.no_internet_msg);
        } else {
            if (!isLoading) {
                isLoading = true;
                if (mFollowersPresenter == null)
                    mFollowersPresenter = new FollowersPresenter(this, this);

                if (mFollowers.equals("1")) {
                    mFollowersPresenter.getFollowingResponse(mUserId, listCount + "", "10");
                } else {
                    mFollowersPresenter.getFollowersResponse(mUserId, listCount + "", "10");
                }

                //  NewFeedPresenter presenter = new NewFeedPresenter(this, this);
                //presenter.hitApi(listCount + "");
            }
        }
    }


    @Override
    public void setAdapter(RecyclerView recyclerView, ArrayList<?> mList) {
        //super.setAdapter(recyclerView, mList);
       // FollowersAdapter followersAdapter = new FollowersAdapter(this, mList);
      //  recyclerView.setAdapter(followersAdapter);
    }

    @Override
    public void dispose() {
        if (mFollowersPresenter != null)
            mFollowersPresenter = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_name_followers:
                finish();
                break;
        }
    }

    @Override
    public void onFollowersSuccess(FollowingResponse followingResponse) {
        ArrayList<FollowingResponse.Users> users = followingResponse.getUsers();
        isLoading = false;
        if (followingResponse != null) {
            if (followersList == null) {
                followersList = new ArrayList<>();
            }
            if (users != null && users.size() > 0) {
                followersList.addAll(users);
                //newsFeedOutput = posts;
                //user_badge_layout.setVisibility(View.GONE);
                // swipeRefreshLayout.setVisibility(View.VISIBLE);
                followersAdapter = new FollowersAdapter(this, followersList);
                mFollowersRecyclerView.setAdapter(followersAdapter);


                //mrecylerV.setAdapter(followersAdapter);

                // adapter = new NewsPostAdapter(this, newsFeedOutput, R.id.home_frame_layout, manager);
                // adapter.isOnline(isOnline);
                //recyclerViewPosts.setAdapter(adapter);
                mFollowersRecyclerView.scrollToPosition(lastSavedPosition);
            } else {
                noMoreData = true;
                //user_badge_layout.setVisibility(View.VISIBLE);
                //swipeRefreshLayout.setVisibility(View.GONE);
            }
        }



//        if (followingResponse.getUsers() != null && followingResponse.getUsers().size() > 0) {
//            setAdapter(mFollowersRecyclerView, followingResponse.getUsers());
//        }
    }


    @Override
    public void refreshList(int count) {
        super.refreshList(count);
        loadPaginationData(count);

    }

    @Override
    public void onFollowingError(Throwable t) {
        showToast(t.getMessage());
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }
}
