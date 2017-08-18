package com.nvcomputers.ten.views.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.DetailViewPagerAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.DeletePostResponse;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.presenter.DeletePostPresenter;
import com.nvcomputers.ten.presenter.SelectedPostPresenter;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.ProgressUtility;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Response;

public class PostDetailActivity extends BaseActivity implements AppCommonCallback,
        PopupMenu.OnMenuItemClickListener, View.OnClickListener, DeletePostPresenter.DeletePostCallback {

    private PopupMenu popup;
    private ViewPager pager;
    protected TextView leftBtn, topBtn, rightBtn, bottomBtn;

    protected SharedPrefsHelper sharedPrefsHelper;
    protected Timer timer;

    ///strings
    public String userId, postId, expirationTime, mUserId, username;
    public String followingUser;
    protected RelativeLayout leftScrollBtn, rightScrollBtn;
    //Lists
    protected SelectedPostPresenter mSelectedPostPresenter;
    protected ArrayList<NewsFeedOutput.Posts> mTopBtmList;
    protected ArrayList<NewsFeedOutput.Posts> mLeftRgtList;
    protected ArrayList<NewsFeedOutput.Posts> mList;

    protected int leftRightPosition;
    protected int topBtmPosition;


    public RelativeLayout parentLayout;
    public TextView imageBack;
    private ImageView action;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_selected_post_view;
    }

    protected void getDataFromBundle() {

        Bundle bundle = getIntent().getExtras();
        mList = bundle.getParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA);
        int listPosition = bundle.getInt("position", 0);
        if (listPosition == -1) listPosition = 0;
        //getDataFromList(mList, listPosition);
        userId = mList.get(listPosition).getUserPoster().getIdUser();
        postId = mList.get(listPosition).getIdPost();

        if (mSelectedPostPresenter == null)
            mSelectedPostPresenter = new SelectedPostPresenter();

        mTopBtmList = mSelectedPostPresenter.topBottomList(mList, listPosition);

        if (mTopBtmList != null && mTopBtmList.size() > 0) {
            for (int i = 0; i < mTopBtmList.size(); i++) {
                String user_id = mTopBtmList.get(i).getUserPoster().getIdUser();
                if (userId.equals(user_id)) {
                    topBtmPosition = i;
                    break;
                }
            }
        }

        mLeftRgtList = mSelectedPostPresenter.leftRightList(mList, userId);

        if (mLeftRgtList != null && mLeftRgtList.size() > 0) {
            for (int i = 0; i < mLeftRgtList.size(); i++) {
                String post_Id = mLeftRgtList.get(i).getIdPost();
                if (postId.equals(post_Id)) {
                    leftRightPosition = i;
                    break;
                }
            }
        }
        // Note that we are passing childFragmentManager, not FragmentManager
        setPager();
        pager.setCurrentItem(leftRightPosition);
    }

    @Override
    protected void initViews() {
        sharedPrefsHelper = new SharedPrefsHelper(PostDetailActivity.this);

        //frameLayout = (FrameLayout) findViewById(R.id.single_post_layout);

        leftScrollBtn = (RelativeLayout) findViewById(R.id.swipeLeftBtn);
        rightScrollBtn = (RelativeLayout) findViewById(R.id.swipeRightBtn);
        leftBtn = (TextView) findViewById(R.id.lefttbtn);
        topBtn = (TextView) findViewById(R.id.topbtn);
        rightBtn = (TextView) findViewById(R.id.rightbtn);
        bottomBtn = (TextView) findViewById(R.id.btnBottom);
        imageBack = (TextView) findViewById(R.id.image_back);
        action = (ImageView) findViewById(R.id.action);
        pager = (ViewPager) findViewById(R.id.view_pager_detail);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d("`````````````", "`````onPageScrolled```````" + position);
                leftRightPosition = position;
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("`````````````", "`````onPageSelected```````");
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.d("`````````````", "`````onPageScrollStateChanged```````" + state);

                updateArrowsUI();
            }
        });

        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing...
                Log.d("`````````````", "````````````");
                Utilities.hideKeypad(parentLayout);
            }
        });

        mUserId = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_ID);
        followingUser = sharedPrefsHelper.get(PreferenceKeys.PREF_FOLLOWING_LIST, "");
        //hideNavigation();
        getDataFromBundle();

        /*View left_layout = (View) findViewById(R.id.left_layout);
        left_layout.setOnClickListener(this);
        View right_layout = (View) findViewById(R.id.right_layout);
        right_layout.setOnClickListener(this);*/
        //GestureScrollView scrollView = (GestureScrollView) findViewById(R.id.scrollView);
        //GestureDetector gesture = new GestureDetector(getBaseActivity(), new GestureListener());
        //GestureRelativeLayout second_layout = (GestureRelativeLayout) findViewById(R.id.second_layout);
        //second_layout.update(gesture);

        //cancel timer...
        cancelTimer();
        //init timer....
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //checkPostExpiration();
                    }
                });
            }
        }, 10000, 10000);

        imageBack.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        rightBtn.setOnClickListener(this);
        topBtn.setOnClickListener(this);
        bottomBtn.setOnClickListener(this);
        leftScrollBtn.setOnClickListener(this);
        rightScrollBtn.setOnClickListener(this);
        action.setOnClickListener(this);
    }

    public void showPopup(View v) {
        popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();
        if (userId.equals(mUserId)) {
            inflater.inflate(R.menu.sameuser_menu, popup.getMenu());
        } else {
            inflater.inflate(R.menu.other_user_post_menu, popup.getMenu());
        }
        popup.show();
    }


    @Override
    public void onClick(View v) {
        Utilities.hideKeypad(v);
        switch (v.getId()) {

            case R.id.topbtn:
                previousUser();
                break;

            case R.id.btnBottom:
                nextUser();
                break;

            case R.id.lefttbtn:
                previousPost();
                break;

            case R.id.rightbtn:
                nextPost();
                break;

            case R.id.swipeLeftBtn:
                previousUser();
                break;

            case R.id.swipeRightBtn:
                nextUser();
                break;


            case R.id.image_back:
                finish();

                break;
            case R.id.action:
                showPopup(v);
                break;

        }
    }

    private void previousUser() {
        if (mTopBtmList != null && mTopBtmList.size() > 0) {
            topBtmPosition--;
            if (topBtmPosition != -1) {
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.down_anim);
                parentLayout.startAnimation(anim);
                // parentLayout.animate().translationY(parentLayout.getHeight());
                String user_id = mTopBtmList.get(topBtmPosition).getUserPoster().getIdUser();
                mLeftRgtList = mSelectedPostPresenter.leftRightList(mList, user_id);
                //getDataFromList(mLeftRgtList, 0);
                setPager();

            } else {
                topBtmPosition++;
            }
        }
    }

    private void nextUser() {
        if (mTopBtmList != null && mTopBtmList.size() > 0) {
            topBtmPosition++;
            if (topBtmPosition < mTopBtmList.size()) {
                Animation anim = AnimationUtils.loadAnimation(this, R.anim.up_anim);
                parentLayout.startAnimation(anim);
                String user_id = mTopBtmList.get(topBtmPosition).getUserPoster().getIdUser();
                mLeftRgtList = mSelectedPostPresenter.leftRightList(mList, user_id);
                //getDataFromList(mLeftRgtList, 0);
                setPager();
            } else {
                topBtmPosition--;
            }
        }
    }

    private void setPager() {
        DetailViewPagerAdapter adapter = new DetailViewPagerAdapter(mLeftRgtList, null);
        pager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        updateArrowsUI();
    }

    private void nextPost() {
        if (mLeftRgtList != null && mLeftRgtList.size() > 0) {
            leftRightPosition++;
            if (leftRightPosition < mLeftRgtList.size()) {
               /* Animation anim = AnimationUtils.loadAnimation(activity, R.anim.left_anim);
                parentLayout.startAnimation(anim);*/
                //getDataFromList(mLeftRgtList, leftRightPosition);
                pager.setCurrentItem(leftRightPosition, true);
                updateArrowsUI();
            } else {
                rightBtn.setVisibility(View.GONE);
                leftRightPosition--;
            }
        }
    }

    private void previousPost() {
        if (mLeftRgtList != null && mLeftRgtList.size() > 0) {
            leftRightPosition--;
            if (leftRightPosition != -1) {
                /*Animation anim = AnimationUtils.loadAnimation(activity, R.anim.right_anim);
                parentLayout.startAnimation(anim);*/
                pager.setCurrentItem(leftRightPosition, true);
                updateArrowsUI();
            } else {
                leftBtn.setVisibility(View.GONE);
                leftRightPosition++;
            }
        }
    }


    @Override
    public void setProgressBar() {
        //progressBar.setVisibility(View.VISIBLE);
        ProgressUtility.showProgress(this, "Loading...");
    }

    @Override
    public void dismiss() {
        //progressBar.setVisibility(View.GONE);
        ProgressUtility.dismissProgress();
    }

    @Override
    public void dispose() {
        /*if (mSuggestionDialog != null)
            mSuggestionDialog = null;*/
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.deletePost:
                popup.dismiss();
                if (!Utilities.checkInternet(PostDetailActivity.this)) {
                    showToast(R.string.no_internet_msg);
                } else
                    hitDeleteApi();
                break;
            case R.id.cancel:
                popup.dismiss();
                break;
            case R.id.report_post:
                showToast("Coming Soon");
                break;
        }
        return false;
    }

    private void hitDeleteApi() {
        DeletePostPresenter deletePostPresenter = new DeletePostPresenter(this, this);
        deletePostPresenter.responseCheck(postId);
    }

    @Override
    public void deletePostError(Call<DeletePostResponse> call, Throwable t) {
        Utilities.showMessage(this, getString(R.string.server_error_msg));
    }

    @Override
    public void onDeletePostSuccess(Response<DeletePostResponse> response) {

        DeletePostResponse body = response.body();
        if (body != null) {
            String status = body.getSuccess();
            if (status.equals("true")) {
                Utilities.showSmallToast(this, "Post deleted successfully");
                //replaceChildFragment(R.id.singlepostLayout, new PostListFragment(), null);
                //update();
                finish();

            } else {
                Utilities.showMessage(this, body.getMessage());
            }
        } else {
            Utilities.showMessage(this, getString(R.string.server_error_msg));
        }
    }


    /*protected void getDataFromList(ArrayList<NewsFeedOutput.Posts> list, int postion) {

        try {
            username = list.get(postion).getUserPoster().getUsername();
            postCaption = list.get(postion).getText();
            userId = list.get(postion).getUserPoster().getIdUser();
            postId = list.get(postion).getIdPost();
            likedPostStatus = list.get(postion).getUserPoster().getIdUser();
            remainingTime = list.get(postion).getDatestamp();
            expirationTime = list.get(postion).getExpiration();
            singlePost = list.get(postion);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            postion--;
            username = list.get(postion).getUserPoster().getUsername();
            postCaption = list.get(postion).getText();
            userId = list.get(postion).getUserPoster().getIdUser();
            postId = list.get(postion).getIdPost();
            likedPostStatus = list.get(postion).getUserPoster().getIdUser();
            remainingTime = list.get(postion).getDatestamp();
            expirationTime = list.get(postion).getExpiration();
            singlePost = list.get(postion);
        } catch (Exception e) {

        }
    }
*/


    protected void cancelTimer() {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateArrowsUI() {
        if (mTopBtmList != null && mTopBtmList.size() > 0) {
            if (mTopBtmList.size() == 1) {
                topBtn.setVisibility(View.GONE);
                bottomBtn.setVisibility(View.GONE);
            } else if (topBtmPosition == 0) {
                topBtn.setVisibility(View.GONE);
                bottomBtn.setVisibility(View.VISIBLE);
            } else if (topBtmPosition == mTopBtmList.size() - 1) {
                topBtn.setVisibility(View.VISIBLE);
                bottomBtn.setVisibility(View.GONE);
            } else {
                topBtn.setVisibility(View.VISIBLE);
                bottomBtn.setVisibility(View.VISIBLE);
            }
        }

        if (mLeftRgtList != null && mLeftRgtList.size() > 0) {
            if (mLeftRgtList.size() == 1) {
                leftBtn.setVisibility(View.GONE);
                rightBtn.setVisibility(View.GONE);
            } else if (leftRightPosition == 0) {
                leftBtn.setVisibility(View.GONE);
                rightBtn.setVisibility(View.VISIBLE);
            } else if (leftRightPosition == mLeftRgtList.size() - 1) {
                rightBtn.setVisibility(View.GONE);
                leftBtn.setVisibility(View.VISIBLE);
            } else {
                rightBtn.setVisibility(View.VISIBLE);
                leftBtn.setVisibility(View.VISIBLE);
            }
        }
    }

}