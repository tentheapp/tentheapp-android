package com.nvcomputers.ten.views.profile;//package com.nvcomputers.ten.views.profile;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.RequiresApi;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.OnScrollListener;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.signature.StringSignature;
//import com.nvcomputers.ten.R;
//import com.nvcomputers.ten.adapter.NewsPostAdapter;
//import com.nvcomputers.ten.adapter.ProfileAdapter;
//import com.nvcomputers.ten.api.GetRestAdapter;
//import com.nvcomputers.ten.api.ProfileApi;
//import com.nvcomputers.ten.model.output.BlockUserOutput;
//import com.nvcomputers.ten.model.output.GetActivePostListResponse;
//import com.nvcomputers.ten.model.output.NewsFeedOutput;
//import com.nvcomputers.ten.model.output.ProfileResponse;
//import com.nvcomputers.ten.model.output.TopLikersResponse;
//import com.nvcomputers.ten.model.output.UnFollowResponse;
//import com.nvcomputers.ten.presenter.GetActivePostsPresenter;
//import com.nvcomputers.ten.presenter.ProfilePresenter;
//import com.nvcomputers.ten.utils.DateTimeUtil;
//import com.nvcomputers.ten.utils.HttpUtils;
//import com.nvcomputers.ten.utils.PreferenceKeys;
//import com.nvcomputers.ten.utils.ProgressUtility;
//import com.nvcomputers.ten.utils.SharedPrefsHelper;
//import com.nvcomputers.ten.utils.Utilities;
//import com.nvcomputers.ten.views.core.BaseFragment;
//
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//
///**
// * Created by jindaldipanshu on 4/28/2017.
// */
//
//
//public class ProfileFragment extends BaseFragment implements View.OnClickListener, ProfileApi.ProfileCallbacks,
//        View.OnLongClickListener, ProfilePresenter.UsersProfileCallback, PopupMenu.OnMenuItemClickListener,
//        GetActivePostsPresenter.ActivePostCallback {
//
//    private String mType = null;
//    private ImageView mUserImagView;
//    private TextView mTimeTextView, mPostTextView, mFollowersTextView, mFollowingTextView, mEditProfileTextView, mStatusTextView;
//    private TextView mUserNmTextView, mTextDescription, mImageBackIcon;
//    private RecyclerView mPostsRecyclerView, mTimerRecyclerView;
//    private ProfilePresenter mProfilePresenter;
//    private String mUserName, mUserId, friendId, mIsPrivate, mScreen, mDescription, mNickName, mNotify, mBlocked = "";
//    private ImageView mSettingsImageView, mReportImagView;
//    private ProgressBar mPostsProgressBar, mTopTimersProgressView;
//    private ArrayList<NewsFeedOutput.Posts> newsFeedOutput;
//    private ArrayList<GetActivePostListResponse.Posts> activePostsList;
//    private NewsPostAdapter adapter;
//    private Timer timer;
//    private PopupMenu popup;
//    private TextView mUserBlockedTextView, mNoPostTextView;
//    private boolean blocked;
//    private BaseFragment baseFrament;
//    private SharedPrefsHelper sharedPrefsHelper;
//    private ArrayList<NewsFeedOutput.Posts> mPosts;
//    int lastSavedPosition = 0;
//    boolean noMoreData = false;
//    int currentPageNumber = 1;
//    private RelativeLayout profile_parent_layout;
//    private ScrollView sv_2;
//    private int existingHeight = 0;
//
//    public ProfileFragment() {
//    }
//
//
//    @Override
//    protected int getLayoutId() {
//        return R.layout.fragment_profile;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    @Override
//    public void initViews(View view) {
//        mPostsProgressBar = (ProgressBar) findViewById(R.id.pro_bar_profile_posts);
//        mTopTimersProgressView = (ProgressBar) findViewById(R.id.pro_bar_profile_top_timers);
//        findViewById(R.id.profile_frame_layout);
//        baseFrament = this;
//        profile_parent_layout = (RelativeLayout) view.findViewById(R.id.profile_parent_layout);
//        profile_parent_layout.setOnClickListener(this);
//        mSettingsImageView = (ImageView) findViewById(R.id.image_view_settings);
//        mReportImagView = (ImageView) findViewById(R.id.image_view_report);
//       // mTextDescription = (TextView) findViewById(R.id.text_description);
//        mImageBackIcon = (TextView) findViewById(R.id.image_back);
//        mUserNmTextView = (TextView) findViewById(R.id.text_view_title);
//       // mUserImagView = (ImageView) findViewById(R.id.image_view_profile);
//       // mTimeTextView = (TextView) findViewById(R.id.text_view_time);
//       // mPostTextView = (TextView) findViewById(R.id.text_view_post);
//       // mFollowersTextView = (TextView) findViewById(R.id.text_view_followers);
//       // mUserBlockedTextView = (TextView) findViewById(R.id.text_view_user_blocked);
//        mNoPostTextView = (TextView) findViewById(R.id.text_view_no_post);
//       // mFollowingTextView = (TextView) findViewById(R.id.text_view_following);
//       // mEditProfileTextView = (TextView) findViewById(R.id.text_view_edit_profile);
//       // mStatusTextView = (TextView) findViewById(R.id.text_nickName);
//        mPostsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_posts);
//        //mTimerRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_top_timers);
//      //  sv_2 = (ScrollView) findViewById(R.id.sv_2);
//       // mImageBackIcon.setOnClickListener(this);
//       // mFollowersTextView.setOnClickListener(this);
//       // mFollowingTextView.setOnClickListener(this);
//       // findViewById(R.id.tv_followers).setOnClickListener(this);
//        //findViewById(R.id.tv_following).setOnClickListener(this);
//       // mUserImagView.setOnLongClickListener(this);
//        sharedPrefsHelper = new SharedPrefsHelper(getBaseActivity());
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            mUserName = bundle.getString("username");
//            mImageBackIcon.setVisibility(View.VISIBLE);
//            mReportImagView.setVisibility(View.VISIBLE);
//            mSettingsImageView.setVisibility(View.GONE);
//
//        } else {
//            mUserName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
//            mUserNmTextView.setText(mUserName);
//            mBlocked = "false";
//            mScreen = "1";
//            mEditProfileTextView.setText("Edit profile");
//            mEditProfileTextView.setTextColor(getResources().getColor(R.color.white));
//            mImageBackIcon.setVisibility(View.INVISIBLE);
//            mSettingsImageView.setVisibility(View.VISIBLE);
//            mReportImagView.setVisibility(View.GONE);
//        }
//        mSettingsImageView.setOnClickListener(this);
//        mReportImagView.setOnClickListener(this);
//        callProfileApi();
//        mPostsRecyclerView.addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                Log.e("onScrollStateChanged--", newState + "");
//                super.onScrollStateChanged(recyclerView, newState);
//                //Log.e("onScrollStateChanged--", newState + "");
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //Log.e("onSCrooled--", dx + "");
//            }
//        });
//        mPostsRecyclerView.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View p_v, MotionEvent p_event) {
//                // this will disallow the touch request for parent scroll on
//                // touch of child view
//                p_v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//
//        sv_2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                v.getParent().requestDisallowInterceptTouchEvent(true);
//                return false;
//            }
//        });
//    }
//
//    public void callProfileApi() {
//        mUserBlockedTextView.setVisibility(View.GONE);
//        mNoPostTextView.setVisibility(View.GONE);
//        if (!Utilities.checkInternet(mContext)) {
//            showToast(R.string.no_internet_msg);
//            mPostsProgressBar.setVisibility(View.INVISIBLE);
//            mTopTimersProgressView.setVisibility(View.INVISIBLE);
//        } else {
//            if (mProfilePresenter == null) {
//
//                mProfilePresenter = new ProfilePresenter(getBaseActivity(), this);
//            }
//            ProfileApi profileApi = new ProfileApi(getBaseActivity(), this);
//            try {
//                profileApi.responseCheck(mUserName);
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void dispose() {
//        if (mProfilePresenter != null)
//            mProfilePresenter = null;
//    }
//
//    @Override
//    public void onClick(View view) {
//        if (Utilities.checkInternet(mContext)) {
//            int id = view.getId();
//            if (id == R.id.image_view_report) {
//                popup.show();
//            } else if (id == R.id.image_view_settings) {
//                Bundle bundle = new Bundle();
//                bundle.putString("username", mUserName);
//                bundle.putString("nick_name", mNickName);
//                bundle.putString("description", mDescription);
//                bundle.putString("isnotify", mNotify);
//                bundle.putString("isPrivate", mIsPrivate);
//                replaceChildFragment(R.id.profile_frame_layout, new SettingsFragment(), bundle);
//            } else if (id == R.id.text_view_edit_profile) {
//                if (mBlocked.contains("false")) {
//                    if (TextUtils.isEmpty(mScreen)) {
//                        if (friendId.contains(mContext.getResources().getString(R.string.friended_friended))) {
//                            unFollowResponse(mUserId);
//                        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_pending))) {
//                            unFollowResponse(mUserId);
//                        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_rejected)) ||
//                                friendId.contains(mContext.getResources().getString(R.string.friended_none))
//                                ) {
//                            followResponse(mUserId);
//                        }
//                    } else {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("username", mUserName);
//                        bundle.putString("nick_name", mNickName);
//                        bundle.putString("description", mDescription);
//                        bundle.putString("isnotify", mNotify);
//                        bundle.putString("isPrivate", mIsPrivate);
//                        replaceChildFragment(R.id.profile_frame_layout, new EditProfileFragment(), bundle);
//                    }
//                }
//            } else if (id == R.id.tv_following || id == R.id.text_view_following) {
//                Bundle bundle = new Bundle();
//                bundle.putString("followers", "1");
//                bundle.putString("user_id", mUserId);
//                bundle.putString("name", mUserName);
//                replaceChildFragment(R.id.profile_frame_layout, new FollowersFragment(), bundle);
//            } else if (id == R.id.tv_followers || id == R.id.text_view_followers) {
//                Bundle bundle = new Bundle();
//                bundle.putString("followers", "2");
//                bundle.putString("user_id", mUserId);
//                bundle.putString("name", mUserName);
//                replaceChildFragment(R.id.profile_frame_layout, new FollowersFragment(), bundle);
//            } else if (id == R.id.image_back) {
//                manualBackPressed();
//            }
//        } else {
//            Utilities.showMessage(mContext, getString(R.string.no_internet_msg));
//        }
//
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_main2, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    public void createPopup(View v) {
//        popup = new PopupMenu(getBaseActivity(), v);
//        popup.setOnMenuItemClickListener(this);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_main2, popup.getMenu());
//    }
//
//    private void unFollowResponse(String idUser) {
//        Call<UnFollowResponse> response = GetRestAdapter.getRestAdapter(true).unfollowUser(idUser);
//        response.enqueue(new Callback<UnFollowResponse>() {
//            @Override
//            public void onResponse(Call<UnFollowResponse> call,
//                                   retrofit2.Response<UnFollowResponse> response) {
//                if (response != null && response.body() != null) {
//                    if (response.body().getSuccess().contains("true")) {
//                        friendId = mContext.getResources().getString(R.string.friended_none);
//                        setFriended();
//                        callProfileApi();
//                    }
//                } else {
//                    showToast(R.string.server_error_msg);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UnFollowResponse> call, Throwable t) {
//                showToast(t.getMessage());
//            }
//        });
//    }
//
//
//    private void followResponse(String idUser) {
//        Call<UnFollowResponse> response = GetRestAdapter.getRestAdapter(true).followUser(idUser);
//        response.enqueue(new Callback<UnFollowResponse>() {
//            @Override
//            public void onResponse(Call<UnFollowResponse> call,
//                                   retrofit2.Response<UnFollowResponse> response) {
//                if (response != null && response.body() != null) {
//                    if (response.body().getSuccess().contains("true")) {
//                        if (TextUtils.isEmpty(mIsPrivate) || mIsPrivate.equals("false")) {
//                            friendId = mContext.getResources().getString(R.string.friended_friended);
//                        } else {
//                            friendId = mContext.getResources().getString(R.string.friended_friended);
//                        }
//                        setFriended();
//                        callProfileApi();

//                    } else {
//                        showToast(response.body().getSuccess());
//                    }
//                } else {
//                    showToast(R.string.server_error_msg);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UnFollowResponse> call, Throwable t) {
//                showToast(t.getMessage());
//            }
//        });
//    }
//
//    @Override
//    public void setAdapter(RecyclerView recyclerView, ArrayList<?> posts) {
//        hideDialog();
//        super.setAdapter(recyclerView, posts);
//        mPostsProgressBar.setVisibility(View.INVISIBLE);
//        if (posts != null && posts.size() > 0) {
//            newsFeedOutput = (ArrayList<NewsFeedOutput.Posts>) posts;
//            adapter = new NewsPostAdapter(this, posts, R.id.profile_frame_layout, linearLayoutManager);
//            mPostsRecyclerView.setAdapter(adapter);
//            mPostsRecyclerView.scrollToPosition(lastSavedPosition);
//            if (existingHeight == 0)
//                existingHeight = mPostsRecyclerView.getMeasuredHeight();
//            setHeight(existingHeight);
//           // mPostsRecyclerView.setLayoutParams(params);
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    checkPostExpiration();
//                }
//            }, 5000, 10000);
//        } else {
//            posts = new ArrayList<>();
//            adapter = new NewsPostAdapter(this, posts, R.id.profile_frame_layout, linearLayoutManager);
//            mPostsRecyclerView.setAdapter(adapter);
//            setHeight(200);
//            cancelTimer();
//        }
//    }
//
//    private void setHeight(int height) {
//        ViewGroup.LayoutParams params = mPostsRecyclerView.getLayoutParams();
//        if (existingHeight == 0)
//            existingHeight = mPostsRecyclerView.getMeasuredHeight();
//        params.height = height;
//        mPostsRecyclerView.setLayoutParams(params);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        cancelTimer();
//    }
//
//    @Override
//    public void onProfileResponse(ProfileResponse.Profile data) {
//        if (data != null) {
//            createPopup(mReportImagView);
//
//            if (data != null) {
//                mUserId = data.getUser().getIdUser();
//                mIsPrivate = data.getUser().getIsPrivate();
//                mNickName = data.getUser().getWebsite();
//                mNotify = data.getUser().getIsNotify();
//                mBlocked = data.getUser().getBlocked();
//                mDescription = data.getUser().getDescription();
//                mUserNmTextView.setText(mUserName);
//                friendId = data.getUser().getFriended();
//                if (TextUtils.isEmpty(mScreen)) {
//                    setFriended();
//                } else {
//                    mEditProfileTextView.setText("Edit profile");
//                    mEditProfileTextView.setTextColor(getResources().getColor(R.color.white));
//                }
//
//                String imageTag = sharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
//                Glide.with(mContext)
//                        .load(HttpUtils.getProfileImageURL(mUserName))
//                        .error(getBaseActivity().getResources().getDrawable(R.drawable.myprofilelarge))
//                        .bitmapTransform(new RoundedCornersTransformation(mContext, 20, 15))
//                        .placeholder(R.drawable.myprofilelarge)
//                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                        .signature(new StringSignature(imageTag))
//                        .into(mUserImagView);
//
//
//                if (mBlocked.contains("false")) {
//                    if (!Utilities.checkInternet(getBaseActivity())) {
//                        showToast(R.string.no_internet_msg);
//                        mPostsProgressBar.setVisibility(View.INVISIBLE);
//                        mTopTimersProgressView.setVisibility(View.INVISIBLE);
//
//                    } else {
//                        if (mProfilePresenter == null) {
//                            mProfilePresenter = new ProfilePresenter(getBaseActivity(), this);
//                        }
//                        mProfilePresenter.getTopProfilesResponse(mUserName);
//                        String localUsername = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
//                        if (mUserName.equals(localUsername)) {
//                            loadPaginationData(0);
//                        } else {
//                            GetActivePostsPresenter getActivePostsPresenter = new GetActivePostsPresenter(this, this);
//                            getActivePostsPresenter.hitApi(mUserName);
//                        }
//                    }
//                    popup.getMenu().getItem(1).setTitle("Block");
//                    setDataOnUi(data);
//                } else {
//                    popup.getMenu().getItem(1).setTitle("Unblock");
//                    setEmptyDataOnUi();
//                    dismiss();
//                }
//            }
//        } else {
//            dismiss();
//        }
//    }
//
//    @Override
//    public void reLoadList() {
//        super.reLoadList();
//        if (!Utilities.checkInternet(getBaseActivity())) {
//            showToast(R.string.no_internet_msg);
//            mPostsProgressBar.setVisibility(View.INVISIBLE);
//            mTopTimersProgressView.setVisibility(View.INVISIBLE);
//        } else {
//            loadPaginationData(0);
//        }
//    }
//
//    @Override
//    public void profileError(String t) {
//        showToast(t);
//        mPostsProgressBar.setVisibility(View.INVISIBLE);
//        mTopTimersProgressView.setVisibility(View.INVISIBLE);
//    }
//
//    @Override
//    public void setHorizontalAdapter(ArrayList<TopLikersResponse.Users> mList) {
//        dismiss();
//        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        mTimerRecyclerView.setLayoutManager(linearLayoutManager);
//        ProfileAdapter profileAdapter = new ProfileAdapter(this, mList, 2);
//        mTimerRecyclerView.setAdapter(profileAdapter);
//
//    }
//
//
//    @Override
//    public void setPostsError(String error) {
//        hideDialog();
//    }
//
//    @Override
//    public void setUsersError(String error) {
//        mUserBlockedTextView.setVisibility(View.VISIBLE);
//        mUserBlockedTextView.setText("No Users Found");
//    }
//
//
//    private void checkPostExpiration() {
//        final ArrayList<NewsFeedOutput.Posts> postList = adapter.getPostList();
//        if (postList != null) {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    adapter.updateList(postList);
//                }
//            });
//        }
//    }
//
//    private void cancelTimer() {
//        if (timer != null) {
//            try {
//                timer.cancel();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//
//    /**
//     * This method is used to set Data on ui
//     *
//     * @param data
//     */
//    private void setDataOnUi(ProfileResponse.Profile data) {
//        mEditProfileTextView.setVisibility(View.VISIBLE);
//
//        if (data.getFollowersCount() != null) {   //show Followers count
//            mFollowersTextView.setText(data.getFollowersCount());
//        } else {
//            mFollowersTextView.setText("0");
//        }
//        if (data.getFollowingCount() != null) {   //show Following count
//            mFollowingTextView.setText(data.getFollowingCount());
//        } else {
//            mFollowingTextView.setText("0");
//        }
//        if (!TextUtils.isEmpty(data.getAllPostCount())) {     //show Allcount
//            mPostTextView.setText(data.getAllPostCount());
//        } else {
//            mPostTextView.setText("0");
//        }
//
//        if (TextUtils.isEmpty(mNickName)) {
//            mStatusTextView.setText("");
//        } else {
//            mStatusTextView.setText(mNickName);
//        }
//        if (TextUtils.isEmpty(mDescription)) {
//            mTextDescription.setText("");
//        } else {
//            mTextDescription.setText(mDescription);
//        }
//
//    }
//
//    private void setFriended() {
//        if (TextUtils.isEmpty(friendId)) {
//            mEditProfileTextView.setText("Follow");
//            mEditProfileTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_white));
//        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_friended))) {
//            mEditProfileTextView.setText("Following");
//            mEditProfileTextView.setTextColor(mContext.getResources().getColor(android.R.color.white));
//            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_green));
//        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_pending))) {
//            mEditProfileTextView.setText("Requested");
//            mEditProfileTextView.setTextColor(mContext.getResources().getColor(android.R.color.white));
//            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_black_filled));
//        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_rejected)) ||
//                friendId.contains(mContext.getResources().getString(R.string.friended_none))
//                ) {
//            mEditProfileTextView.setText("Follow");
//            mEditProfileTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
//            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_white));
//        }
//    }
//
//    public void blockUser(final String idUser) {
//        Call<BlockUserOutput> response = GetRestAdapter.getRestAdapter(true).blockUser(idUser);
//        response.enqueue(new Callback<BlockUserOutput>() {
//            @Override
//            public void onResponse(Call<BlockUserOutput> call,
//                                   retrofit2.Response<BlockUserOutput> response) {
//                if (response != null && response.body() != null) {
//
//                    if (response.body().getSuccess().contains("true")) {
//
//                        //   Toast.makeText(activity, "BLock", Toast.LENGTH_SHORT).show();
//                        popup.getMenu().getItem(1).setTitle("Unblock");
//                        //  Toast.makeText(activity, "BLock", Toast.LENGTH_SHORT).show();
//                        blocked = true;
//                        setEmptyDataOnUi();
//                        mBlocked = String.valueOf(true);
//                        // unblockUser(idUser);
//
//                    } else {
//                        mBlocked = String.valueOf(false);
//                    }
//                    // mActivity.showToast(response.body().getSuccess());
//                } else {
//                    showToast(R.string.server_error_msg);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BlockUserOutput> call, Throwable t) {
//                showToast(t.getMessage());
//            }
//        });
//    }
//
//    private void setEmptyDataOnUi() {
//        mUserBlockedTextView.setText("User Is Blocked");
//        mEditProfileTextView.setVisibility(View.INVISIBLE);
//        mFollowersTextView.setText("");
//        mFollowingTextView.setText("");
//        mTimeTextView.setText("");
//        mPostTextView.setText("");
//        mStatusTextView.setText("");
//        mTextDescription.setText("");
//        //horizontal recycler view
//        ArrayList<TopLikersResponse.Users> mList = null;
//        setHorizontalAdapter(mList);
//
//        mPostsProgressBar.setVisibility(View.INVISIBLE);
//        mUserBlockedTextView.setVisibility(View.VISIBLE);
//        mNoPostTextView.setVisibility(View.VISIBLE);
//        //post recycler view]
//        ArrayList<?> posts = new ArrayList<>();
//        adapter = new NewsPostAdapter(this, posts, R.id.profile_frame_layout, linearLayoutManager);
//        mPostsRecyclerView.setAdapter(adapter);
//    }
//
//
//    private void unblockUser(String idUser) {
//        Call<BlockUserOutput> response = GetRestAdapter.getRestAdapter(true).unblockUser(idUser);
//        response.enqueue(new Callback<BlockUserOutput>() {
//            @Override
//            public void onResponse(Call<BlockUserOutput> call,
//                                   retrofit2.Response<BlockUserOutput> response) {
//                if (response != null && response.body() != null) {
//                    if (response.body().getSuccess().contains("true")) {
//                        mBlocked = String.valueOf(false);
//                        popup.getMenu().getItem(1).setTitle("Block");
//                        //Toast.makeText(activity, "Unblock", Toast.LENGTH_SHORT).show();
//                        callProfileApi();
//                    }
//                } else {
//                    showToast(R.string.server_error_msg);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BlockUserOutput> call, Throwable t) {
//                showToast(t.getMessage());
//            }
//        });
//    }
//
//
//    @Override
//    public boolean onLongClick(View view) {
//        if (view.getId() == R.id.image_view_profile) {
//            //  getBaseActivity().addFragment(R.id.main_frame_layout, new ProfileFragment(), null);
//            Intent intent = new Intent(getBaseActivity(), ZoomImageActivity.class);
//            intent.putExtra("username", mUserName);
//            startActivity(intent);
//        }
//        return false;
//    }
//
//    @Override
//    public boolean onMenuItemClick(MenuItem menuItem) {
//        int id = menuItem.getItemId();
//        if (Utilities.checkInternet(mContext)) {
//            switch (id) {
//                case R.id.report_user:
//                    showToast("coming soon");
//                    break;
//
//                case R.id.block:
//                    if (mBlocked.equals("true")) {
//                        //  menuItem.setTitle("Block");
//                        unblockUser(mUserId);
//                    } else {
//                        // menuItem.setTitle("Unblock");
//                        blockUser(mUserId);
//                        mBlocked = String.valueOf(true);
//                    }
//                    break;
//                case R.id.cancel:
//                    popup.dismiss();
//                    break;
//            }
//        } else {
//            Utilities.showMessage(mContext, mContext.getString(R.string.no_internet_msg));
//        }
//
//        popup.show();//showing popup menu
//        return false;
//        //216 test user
//    }
//
//    @Override
//    public void notifyList(int position) {
//        newsFeedOutput.get(position).setLiked(true);
//    }
//
//    private void report(String idPost) {
//
//    }
//
//    @Override
//    public void showDialog() {
//        mPostsProgressBar.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * This method is used to hide posts progress bar
//     */
//    @Override
//    public void hideDialog() {
//        mPostsProgressBar.setVisibility(View.INVISIBLE);
//    }
//
//    @Override
//    public void setProgressBar() {
//        mTopTimersProgressView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void dismiss() {
//        mTopTimersProgressView.setVisibility(View.INVISIBLE);
//    }
//
//
//    @Override
//    public void onActivePostError(Call<NewsFeedOutput> call, Throwable t) {
//        hideDialog();
//
//    }
//
//    @Override
//    public void onActivePostSuccess(Response<NewsFeedOutput> response) {
//        hideDialog();
//        if (response != null) {
//            NewsFeedOutput body = response.body();
//            if (body != null) {
//                ArrayList<NewsFeedOutput.Posts> posts = body.getPosts();
//                setAdapter(mPostsRecyclerView, posts);
//                if (posts == null || posts.size() == 0) {
//                    mNoPostTextView.setVisibility(View.VISIBLE);
//                    mNoPostTextView.setText("No Active Posts");
//                    mPostsProgressBar.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getBaseActivity(), "No Active post.", Toast.LENGTH_LONG).show();
//                }
//            } else {
//                mNoPostTextView.setVisibility(View.VISIBLE);
//                mNoPostTextView.setText("No Active Posts");
//                mPostsProgressBar.setVisibility(View.INVISIBLE);
//                Toast.makeText(getBaseActivity(), "No Active post.", Toast.LENGTH_LONG).show();
//                setHeight(200);
//            }
//
//        } else {
//            mPostsProgressBar.setVisibility(View.INVISIBLE);
//            showToast(R.string.no_data_found_msg);
//        }
//    }
//
//    @Override
//    public void setPostsAdapter(Response<NewsFeedOutput> response) {
//        if (response != null) {
//
//            NewsFeedOutput body = response.body();
//            if (body != null) {
//
//                String localUsername = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
//                if (!mUserName.equals(localUsername)) {
//                    return;
//                }
//                if (mPosts == null) {
//                    mPosts = new ArrayList<>();
//                }
//                ArrayList<NewsFeedOutput.Posts> posts = body.getPosts();
//                ArrayList<NewsFeedOutput.Posts> activePosts = new ArrayList<>();
//                if (posts != null && posts.size() > 0) {
//                    for (int i = 0; i < posts.size(); i++) {
//                        boolean isReposted = posts.get(i).getIsReposted();
//                        if (isReposted) {
//                            String expireTime = posts.get(i).getExpiration();
//                            if (!DateTimeUtil.checkExpiration(expireTime)) {
//                                activePosts.add(posts.get(i));
//                            }
//                        } else {
//                            activePosts.add(posts.get(i));
//                        }
//                    }
//                    mPosts.addAll(activePosts);
//                    setAdapter(mPostsRecyclerView, mPosts);
//                } else {
//                    noMoreData = true;
//                    if (mPosts == null || mPosts.size() == 0) {
//                        mNoPostTextView.setVisibility(View.VISIBLE);
//                        mNoPostTextView.setText("No Post Found");
//                    }
//                }
//                mPostsProgressBar.setVisibility(View.INVISIBLE);
//            } else {
//                mNoPostTextView.setVisibility(View.VISIBLE);
//                mNoPostTextView.setText("No Post Found");
//                mPostsProgressBar.setVisibility(View.INVISIBLE);
//            }
//            loading = false;
//        } else {
//            mNoPostTextView.setVisibility(View.VISIBLE);
//            mNoPostTextView.setText("No Post Found");
//            mPostsProgressBar.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    boolean loading = false;
//
//    public void loadPaginationData(int listCount) {
//        String localUsername = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
//        if (!mUserName.equals(localUsername)) {
//            return;
//        }
//        if (mProfilePresenter == null) {
//            mProfilePresenter = new ProfilePresenter(getBaseActivity(), this);
//        }
//        if (listCount == 0) {
//            currentPageNumber = 1;
//            lastSavedPosition = 0;
//            noMoreData = false;
//            mPosts = new ArrayList<>();
//            mPosts.clear();
//            mProfilePresenter.getPosts(mUserName, listCount + "");
//        } else {
//            lastSavedPosition = listCount - 1;
//            if (/*listCount >= 10 &&*/ !noMoreData && !loading/*&& listCount % 10 == 0*/) {
//                loading = true;
//                int value = listCount / 10;
//                currentPageNumber = value + 1;
//                mProfilePresenter.getPosts(mUserName, listCount + "");
//            } else {
//                ProgressUtility.dismissProgress();
//            }
//        }
//    }
//
//
//    public void scrollUp() {
//        if (adapter != null && adapter.getItemCount() > 0) {
//            mPostsRecyclerView.smoothScrollToPosition(0);
//        }
//    }
//}
