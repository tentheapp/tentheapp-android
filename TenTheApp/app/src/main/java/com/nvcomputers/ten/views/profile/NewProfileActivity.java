package com.nvcomputers.ten.views.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.google.gson.Gson;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.NewsPostAdapter;
import com.nvcomputers.ten.adapter.ProfileAdapter;
import com.nvcomputers.ten.adapter.UserProfileAdapter;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.api.ProfileApi;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.BlockUserOutput;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.ProfileResponse;
import com.nvcomputers.ten.model.output.TopLikersResponse;
import com.nvcomputers.ten.model.output.UnFollowResponse;
import com.nvcomputers.ten.presenter.GetActivePostsPresenter;
import com.nvcomputers.ten.presenter.GetUserProfilePresenter;
import com.nvcomputers.ten.presenter.ProfilePresenter;
import com.nvcomputers.ten.utils.DateTimeUtil;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.ProgressUtility;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewProfileActivity extends BaseActivity implements View.OnClickListener, ProfilePresenter.UsersProfileCallback,
        GetUserProfilePresenter.GetUserProfileCallback, AppCommonCallback, PopupMenu.OnMenuItemClickListener {

    private RecyclerView mPostsRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TextView mImageBackIcon;
    private TextView mUserNmTextView;
    private ImageView mReportImagView;
    private SharedPrefsHelper sharedPrefsHelper;
    public String mUserName;
    private String mBlocked;
    public String mScreen;
    private ProgressBar mPostsProgressBar;
    private ProfilePresenter mProfilePresenter;
    private TextView mNoPostTextView;
    private ProfileResponse.Profile profileData;
    private UserProfileAdapter profileAdapter;
    int lastSavedPosition = 0;
    boolean noMoreData = false;
    private ArrayList<NewsFeedOutput.Posts> mPosts;
    private String mIsPrivate;
    private String mNickName;
    private String mNotify;
    private String mDescription;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PopupMenu popup;
    public String mUserId;
    //public Context mContext;
    private Timer timer;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    protected void initViews() {
        mContext = this;
        mNoPostTextView = (TextView) findViewById(R.id.text_view_no_post);
        mPostsProgressBar = (ProgressBar) findViewById(R.id.pro_bar_profile_posts);
        mImageBackIcon = (TextView) findViewById(R.id.iv_back);
        mUserNmTextView = (TextView) findViewById(R.id.text_view_title);
        //mSettingsImageView = (ImageView) findViewById(R.id.image_view_settings);
        mReportImagView = (ImageView) findViewById(R.id.image_view_report);
        mPostsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_posts);
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mPostsRecyclerView.setLayoutManager(mLayoutManager);

        mImageBackIcon.setOnClickListener(this);
        //mSettingsImageView.setOnClickListener(this);
        mReportImagView.setOnClickListener(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mBlocked.contains("false")) {
                    getPostsApi(true);
                    //getTopLiker();
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                }

            }
        });
        sharedPrefsHelper = new SharedPrefsHelper(mContext);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mUserName = bundle.getString("username");
            mImageBackIcon.setVisibility(View.VISIBLE);
            mReportImagView.setVisibility(View.VISIBLE);
            //mSettingsImageView.setVisibility(View.GONE);
            if (mUserName.contains("@")) {
                mUserName = mUserName.replace("@", "");
            }
        } else {
            mUserName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
            mUserNmTextView.setText(mUserName);
            mBlocked = "false";
            mScreen = "1";
            mImageBackIcon.setVisibility(View.INVISIBLE);
            //mSettingsImageView.setVisibility(View.VISIBLE);
            mReportImagView.setVisibility(View.GONE);
        }
        mUserNmTextView.setText(mUserName);
        mPostsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View p_v, MotionEvent p_event) {
                // this will disallow the touch request for parent scroll on
                // touch of child view
                p_v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        getProfileApi();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //callProfileApi();
    }

    @Override
    public void reLoadList() {
        super.reLoadList();
        if (!Utilities.checkInternet(this)) {
            showToast(R.string.no_internet_msg);
        } else {
            getPostsApi(true);
        }
    }

    public void getProfileApi() {
        if (Utilities.checkInternet(mContext)) {
            GetUserProfilePresenter getUserProfilePresenter = new GetUserProfilePresenter(this, this);
            getUserProfilePresenter.responseCheck(mUserName);
        } else {
            Utilities.showMessage(mContext, getString(R.string.no_internet_msg));
        }
    }


    @Override
    public void dispose() {
        if (mProfilePresenter != null)
            mProfilePresenter = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void cancelTimer() {
        if (timer != null) {
            try {
                timer.cancel();
                timer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }


    public void showPopup(View v) {
        popup = new PopupMenu(mContext, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main2, popup.getMenu());
    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (Utilities.checkInternet(mContext)) {
            switch (id) {
                case R.id.report_user:
                    popup.dismiss();
                    showToast("coming soon");
                    break;

                case R.id.block:
                    popup.dismiss();
                    if (mBlocked.equals("true")) {
                        //  menuItem.setTitle("Block");
                        unblockUser(mUserId);
                    } else {
                        // menuItem.setTitle("Unblock");
                        blockUser(mUserId);
                        mBlocked = String.valueOf(true);
                    }
                    break;
                case R.id.cancel:
                    popup.dismiss();
                    break;

            }
        } else {
            Utilities.showMessage(mContext, mContext.getString(R.string.no_internet_msg));
        }

        popup.show();//showing popup menu
        return false;
        //216 test user
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_view_report:
                if (popup != null)
                    popup.show();
                break;

            /*case R.id.image_back:
                finish();
                break;*/
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void getPostsApi(boolean reload) {
        if (mUserName.equals(sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, ""))) {
            String profileDataStr = sharedPrefsHelper.get(PreferenceKeys.PREF_PROFILE_PAGE_DATA, "");
            if (reload) {
                loadPaginationData(0);
            } else if (profileDataStr != null && profileDataStr.length() > 0) {
                Gson gson = new Gson();
                NewsFeedOutput data = gson.fromJson(profileDataStr, NewsFeedOutput.class);
                if (data != null && data.getPosts() != null && data.getPosts().size() > 0) {
                    ArrayList<NewsFeedOutput.Posts> offlinePostList = data.getPosts();
                    mPosts = new ArrayList<>();
                    mPosts.clear();
                    for (int i = 0; i < offlinePostList.size(); i++) {
                        boolean isReposted = offlinePostList.get(i).getIsReposted();
                        if (isReposted) {
                            String expireTime = offlinePostList.get(i).getExpiration();
                            if (!DateTimeUtil.checkExpiration(expireTime)) {
                                mPosts.add(offlinePostList.get(i));
                            }
                        } else {
                            mPosts.add(offlinePostList.get(i));
                        }
                    }
                    data.setPosts(mPosts);
                    String jsonBody = gson.toJson(data, NewsFeedOutput.class);
                    sharedPrefsHelper.save(PreferenceKeys.PREF_PROFILE_PAGE_DATA, jsonBody);
                    if (mPosts.size() == 0) {
                        loadPaginationData(0);
                    } else {
                        handleBody(data, false);
                    }
                } else {
                    loadPaginationData(0);
                }
            } else {
                loadPaginationData(0);
            }
        } else {
            if (mProfilePresenter == null) {
                mProfilePresenter = new ProfilePresenter(baseActivity, this);
            }
            mProfilePresenter.getActivePost(mUserName);
        }
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }

    boolean loading = false;

    public void loadPaginationData(int listCount) {
        String localUsername = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        if (!mUserName.equals(localUsername)) {
            return;
        }
        if (mProfilePresenter == null) {
            mProfilePresenter = new ProfilePresenter(baseActivity, this);
        }
        if (listCount == 0) {
            //currentPageNumber = 1;
            lastSavedPosition = 0;
            noMoreData = false;
            mPosts = new ArrayList<>();
            mPosts.clear();
            mProfilePresenter.getPosts(mUserName, listCount + "");
        } else {
            lastSavedPosition = listCount - 1;
            if (!noMoreData && !loading) {
                loading = true;
                //int value = listCount / 10;
                //currentPageNumber = value + 1;
                mProfilePresenter.getPosts(mUserName, listCount + "");
            } else {
                ProgressUtility.dismissProgress();
            }
        }
    }

    @Override
    public void userProfileError(Call<ProfileResponse> call, Throwable t) {

    }

    @Override
    public void onUserProfileSuccess(Response<ProfileResponse> response) {
        ProfileResponse body = response.body();
        if (body != null) {
            showPopup(mReportImagView);
            profileData = body.getProfile();
            if (profileData != null) {
                mUserId = profileData.getUser().getIdUser();
                mIsPrivate = profileData.getUser().getIsPrivate();
                mNickName = profileData.getUser().getWebsite();
                mNotify = profileData.getUser().getIsNotify();
                mBlocked = profileData.getUser().getBlocked();
                mDescription = profileData.getUser().getDescription();
                mBlocked = profileData.getUser().getBlocked();
                if (mBlocked.contains("false")) {
                    profileAdapter = new UserProfileAdapter(this, this, profileData, R.id.profile_frame_layout, mScreen);
                    mPostsRecyclerView.setAdapter(profileAdapter);
                    getPostsApi(false);
                    //getTopLiker();
                } else {
                    profileAdapter = new UserProfileAdapter(this, this, null, R.id.profile_frame_layout, mScreen);
                    mPostsRecyclerView.setAdapter(profileAdapter);
                    popup.getMenu().getItem(1).setTitle("Unblock");
                }
            } else {
                Utilities.showMessage(mContext, "User Not Found");
            }
        } else {
            if (response.code() == 400) {
                Utilities.showMessage(mContext, "User Not Found");
            } else {
                Utilities.showMessage(mContext, getString(R.string.server_error_msg));
            }
        }
    }

    /*private void getTopLiker() {
        if (mProfilePresenter == null) {
            mProfilePresenter = new ProfilePresenter(baseActivity, this);
        }
        mProfilePresenter.getTopProfilesResponse(mUserName);
    }

    @Override
    public void setHorizontalAdapter(ArrayList<TopLikersResponse.Users> profileResponse) {
        if (profileResponse != null && profileResponse.size() > 0) {
            profileAdapter.updateTopTimer(profileResponse);
        } else {
            profileAdapter.updateTopTimer(new ArrayList<TopLikersResponse.Users>());
        }
    }

    @Override
    public void setUsersError(String error) {
        profileAdapter.updateTopTimer(new ArrayList<TopLikersResponse.Users>());
    }*/

    @Override
    public void setPostsAdapter(Response<NewsFeedOutput> response) {
        swipeRefreshLayout.setRefreshing(false);
        if (response != null) {
            NewsFeedOutput body = response.body();
            if (body != null) {
                handleBody(body, true);
            } else {
                mNoPostTextView.setVisibility(View.VISIBLE);
                mNoPostTextView.setText("No Post Found");
                mPostsProgressBar.setVisibility(View.INVISIBLE);
            }
            loading = false;
        } else {
            mNoPostTextView.setVisibility(View.VISIBLE);
            mNoPostTextView.setText("No Post Found");
            mPostsProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void handleBody(NewsFeedOutput body, boolean isOnline) {
        String localUsername = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        if (!mUserName.equals(localUsername)) {
            return;
        }
        if (mPosts == null) {
            mPosts = new ArrayList<>();
        }
        ArrayList<NewsFeedOutput.Posts> posts = body.getPosts();
        ArrayList<NewsFeedOutput.Posts> ownPosts = new ArrayList<>();
        if (posts != null && posts.size() > 0) {
            for (int i = 0; i < posts.size(); i++) {
                boolean isReposted = posts.get(i).getIsReposted();
                if (isReposted) {
                    String expireTime = posts.get(i).getExpiration();
                    if (!DateTimeUtil.checkExpiration(expireTime)) {
                        ownPosts.add(posts.get(i));
                    }
                } else {
                    ownPosts.add(posts.get(i));
                }
            }
            mPosts.addAll(ownPosts);
            if (profileAdapter == null) {
                profileAdapter = new UserProfileAdapter(this, this, profileData, R.id.profile_frame_layout, mScreen);
                mPostsRecyclerView.setAdapter(profileAdapter);
            }
            profileAdapter.updatePostList(mPosts);
            if (timer == null) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        checkPostExpiration();
                    }
                }, 500, 10000);
            }
            if (isOnline) {
                body.setPosts(mPosts);
                Gson gson = new Gson();
                String jsonBody = gson.toJson(body, NewsFeedOutput.class);
                sharedPrefsHelper.save(PreferenceKeys.PREF_PROFILE_PAGE_DATA, jsonBody);
            }
        } else {
            noMoreData = true;
            if (mPosts == null || mPosts.size() == 0) {
                mNoPostTextView.setVisibility(View.VISIBLE);
                mNoPostTextView.setText("No Post Found");
            }
        }
        mPostsProgressBar.setVisibility(View.INVISIBLE);
    }

    private void checkPostExpiration() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                profileAdapter.updateList(mLayoutManager);
            }
        });
    }

    @Override
    public void setPostsError(String error) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivePostError(Call<NewsFeedOutput> call, Throwable t) {
        hideDialog();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onActivePostSuccess(Response<NewsFeedOutput> response) {
        swipeRefreshLayout.setRefreshing(false);
        hideDialog();
        if (response != null) {
            NewsFeedOutput body = response.body();
            if (body != null) {
                ArrayList<NewsFeedOutput.Posts> posts = body.getPosts();
                //setAdapter(mPostsRecyclerView, posts);
                if (posts == null || posts.size() == 0) {
                    mNoPostTextView.setVisibility(View.VISIBLE);
                    mNoPostTextView.setText("No Active Posts");
                    mPostsProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(baseActivity, "No Active post.", Toast.LENGTH_LONG).show();
                } else {
                    mNoPostTextView.setVisibility(View.GONE);
                    mPostsProgressBar.setVisibility(View.VISIBLE);
                    if (profileAdapter == null) {
                        profileAdapter = new UserProfileAdapter(this, this, profileData, R.id.profile_frame_layout, mScreen);
                        mPostsRecyclerView.setAdapter(profileAdapter);
                    }
                    profileAdapter.updatePostList(posts);
                }
            } else {
                mNoPostTextView.setVisibility(View.VISIBLE);
                mNoPostTextView.setText("No Active Posts");
                mPostsProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(baseActivity, "No Active post.", Toast.LENGTH_LONG).show();
                //setHeight(200);
            }
        } else {
            mPostsProgressBar.setVisibility(View.INVISIBLE);
            showToast(R.string.no_data_found_msg);
        }
    }


    private void unblockUser(String idUser) {
        Call<BlockUserOutput> response = GetRestAdapter.getRestAdapter(true).unblockUser(idUser);
        response.enqueue(new Callback<BlockUserOutput>() {
            @Override
            public void onResponse(Call<BlockUserOutput> call,
                                   Response<BlockUserOutput> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getSuccess().contains("true")) {
                        mBlocked = String.valueOf(false);
                        popup.getMenu().getItem(1).setTitle("Block");
                        //Toast.makeText(activity, "Unblock", Toast.LENGTH_SHORT).show();
                        getProfileApi();
                    }
                } else {
                    showToast(R.string.server_error_msg);
                }
            }

            @Override
            public void onFailure(Call<BlockUserOutput> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

    public void blockUser(final String idUser) {
        Call<BlockUserOutput> response = GetRestAdapter.getRestAdapter(true).blockUser(idUser);
        response.enqueue(new Callback<BlockUserOutput>() {
            @Override
            public void onResponse(Call<BlockUserOutput> call,
                                   Response<BlockUserOutput> response) {
                if (response != null && response.body() != null) {

                    if (response.body().getSuccess().contains("true")) {

                        //   Toast.makeText(activity, "BLock", Toast.LENGTH_SHORT).show();
                        popup.getMenu().getItem(1).setTitle("Unblock");
                        //  Toast.makeText(activity, "BLock", Toast.LENGTH_SHORT).show();
                        //blocked = true;
                        mBlocked = String.valueOf(true);
                        // unblockUser(idUser);
                        profileAdapter = new UserProfileAdapter(NewProfileActivity.this, NewProfileActivity.this, null, R.id.profile_frame_layout, mScreen);
                        mPostsRecyclerView.setAdapter(profileAdapter);
                    } else {
                        mBlocked = String.valueOf(false);
                    }
                    // mActivity.showToast(response.body().getSuccess());
                } else {
                    showToast(R.string.server_error_msg);
                }
            }

            @Override
            public void onFailure(Call<BlockUserOutput> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }

}
