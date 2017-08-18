package com.nvcomputers.ten.views.home;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.CommentListAdapter;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.DoubleClickListener;
import com.nvcomputers.ten.model.output.GetAllPostCommentOutput;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.UnFollowResponse;
import com.nvcomputers.ten.presenter.LikeCommentsCountsPresenter;
import com.nvcomputers.ten.presenter.SelectedPostPresenter;
import com.nvcomputers.ten.utils.DateTimeUtil;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.profile.NewProfileActivity;
import com.nvcomputers.ten.views.profile.ZoomImageActivity;
import com.nvcomputers.ten.views.search.SearchActivity;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Timer;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by jindaldipanshu on 6/9/2017.
 */

public class PostDetailBaseActivity extends BaseActivity {

    //protected RelativeLayout videoRelative, pofileRelativeLayout;
    protected ImageView PostImage, UserImage, ClockImageView, LikeImage, RepostImage, CommentImage;
    protected ImageView commentImage, /*imageBack,*/
            action, playBtn;
    TextView leftBtn, topBtn, rightBtn, bottomBtn;
    protected TextView UsernameTextView, TimeRemainingTextView, TimeElapasedTextView, RepostedTextView;
    protected TextView likeCount, repostCount, commentCount, captionText, mediaCaptionText;
    protected RelativeLayout leftScrollBtn, rightScrollBtn;
    protected PopupMenu popup;
    protected VideoView videoView;
    protected ProgressBar progressBar;
    protected SharedPrefsHelper sharedPrefsHelper;
    protected Timer timer;
    ///strings
    public String userId, postId, likedPostStatus, remainingTime, expirationTime, mUserId, username;
    public String postCaption, followingUser;

    //Lists
    protected SelectedPostPresenter mSelectedPostPresenter;
    protected ArrayList<NewsFeedOutput.Posts> mTopBtmList;
    protected ArrayList<NewsFeedOutput.Posts> mLeftRgtList;
    protected ArrayList<NewsFeedOutput.Posts> mList;

    protected int leftRightPosition;
    protected int topBtmPosition;

    protected NewsFeedOutput.Posts singlePost;
    protected LikeCommentsCountsPresenter likeCommentsCountsPresenter;

    public Dialog mSuggestionDialog;

    public RelativeLayout parentLayout;
    public RecyclerView mCommentsRecyclerView;
    ///******************************************************************

    public EditText edit_text_comment;
    protected ArrayList<GetAllPostCommentOutput.Comments> getlist;
    protected ArrayList<GetAllPostCommentOutput.Comments> getcommentList;
    protected TextView text;
    protected CommentListAdapter adapter;
    public Context mContext;
    protected RelativeLayout swipeBtnslayout;
    protected ImageButton iv_bottom;
    protected int listPosition;
    protected ImageView commentUserImage;
    public RecyclerView suggestionRV;
    private RelativeLayout bottom_layout;
    public TextView imageBack;
    private RelativeLayout postLayout;


    protected void getDataFromBundle() {
        sharedPrefsHelper = new SharedPrefsHelper(this);
        mList = getIntent().getParcelableArrayListExtra(PreferenceKeys.PREF_HOME_PAGE_DATA);
        listPosition = getIntent().getIntExtra("position", 0);

        if (listPosition == -1) {
            listPosition = 0;
        }
        if (listPosition >= mList.size()) {
            listPosition = mList.size() - 1;
        }
        getDataFromList(mList, listPosition);

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
    }


    protected void getDataFromList(ArrayList<NewsFeedOutput.Posts> list, int postion) {
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


    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews() {
        swipeBtnslayout = (RelativeLayout) findViewById(R.id.swipeBtns);
        leftBtn = (TextView) findViewById(R.id.lefttbtn);
        topBtn = (TextView) findViewById(R.id.topbtn);
        rightBtn = (TextView) findViewById(R.id.rightbtn);
        bottomBtn = (TextView) findViewById(R.id.btnBottom);
        leftScrollBtn = (RelativeLayout) findViewById(R.id.swipeLeftBtn);
        rightScrollBtn = (RelativeLayout) findViewById(R.id.swipeRightBtn);

        postLayout = (RelativeLayout) findViewById(R.id.post_layout_detail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        PostImage = (ImageView) findViewById(R.id.postimage);
        playBtn = (ImageView) findViewById(R.id.playBtn1);
        text = (TextView) findViewById(R.id.text);
        UserImage = (ImageView) findViewById(R.id.post_userimage);
        commentImage = (ImageView) findViewById(R.id.comment_image);
        imageBack = (TextView) findViewById(R.id.image_back);

        action = (ImageView) findViewById(R.id.action);
        edit_text_comment = (EditText) findViewById(R.id.edit_text_comment);
        UsernameTextView = (TextView) findViewById(R.id.username);
        TimeRemainingTextView = (TextView) findViewById(R.id.timeremaining);
        TimeElapasedTextView = (TextView) findViewById(R.id.timeelapsed);
        RepostedTextView = (TextView) findViewById(R.id.repostlabel);
        ClockImageView = (ImageView) findViewById(R.id.clock);
        LikeImage = (ImageView) findViewById(R.id.like);
        RepostImage = (ImageView) findViewById(R.id.repost);
        likeCount = (TextView) findViewById(R.id.post_like_count);
        repostCount = (TextView) findViewById(R.id.repost_count);
        commentCount = (TextView) findViewById(R.id.comment_count);
        CommentImage = (ImageView) findViewById(R.id.comment);
        //videoRelative = (RelativeLayout) findViewById(R.id.videorelative);
        videoView = (VideoView) findViewById(R.id.postvideo);
        commentUserImage = (ImageView) findViewById(R.id.post_user_image);
        mCommentsRecyclerView = (RecyclerView) findViewById(R.id.comment_list);
        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        parentLayout = (RelativeLayout) findViewById(R.id.parentLayout);
        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do nothing...
                Log.d("`````````````", "````````````");
                Utilities.hideKeypad(parentLayout);
            }
        });
        /*GestureDetector gesture = new GestureDetector(this, new GestureListener());
        GestureRelativeLayout second_layout = (GestureRelativeLayout) findViewById(R.id.second_layout);
        second_layout.update(gesture);*/

        captionText = (TextView) findViewById(R.id.captionText);
        mediaCaptionText = (TextView) findViewById(R.id.mediaCaptionText);

        edit_text_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (mSuggestionDialog == null)
                        showSuggestionDialog();
                }
            }
        });

        PostImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent = new Intent(PostDetailBaseActivity.this, ZoomImageActivity.class);
                intent.putExtra("image", HttpUtils.getPostImageURL(postId));
                startActivity(intent);
                return false;
            }
        });

        PostImage.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {

            }

            @Override
            public void onDoubleClick(View v) {
                if (!mUserId.equals(userId)) {
                    if (!Utilities.checkInternet(PostDetailBaseActivity.this)) {
                        showToast(R.string.no_internet_msg);
                    } else {
                        likePost(postId);
                    }
                }
            }
        });
        registerCommentChangeListsner();

    }


    private void registerCommentChangeListsner() {
        edit_text_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    commentImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.commentselected));
                } else {
                    commentImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.comment));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });

    }

    protected void likePost(String idPost) {
        Call<UnFollowResponse> response = GetRestAdapter.getRestAdapter(true).likePost(idPost);
        response.enqueue(new Callback<UnFollowResponse>() {
            @Override
            public void onResponse(Call<UnFollowResponse> call,
                                   retrofit2.Response<UnFollowResponse> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getSuccess().contains("true")) {
                        singlePost.setLiked(true);
                        MediaPlayer mp = MediaPlayer.create(mContext, R.raw.ticksound);
                        mp.start();
                        LikeImage.setBackground(getResources().getDrawable(R.drawable.likeselected));
                        likeCommentsCountsPresenter.countsApi(0, postId);
                    } else {
                        showToast(response.message());
                    }
                }
            }


            @Override
            public void onFailure(Call<UnFollowResponse> call, Throwable t) {
                showToast(t.getMessage());
            }
        });
    }


    protected void showSuggestionDialog() {
        if (mSuggestionDialog == null)
            mSuggestionDialog = new Dialog(this);
        mSuggestionDialog.setContentView(R.layout.suggestion_name_layout);
        // set the custom dialog components - text, image and button
        suggestionRV = (RecyclerView) mSuggestionDialog.findViewById(R.id.suggestionRV);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        suggestionRV.setLayoutManager(mLayoutManager);
    }

    protected void cancelTimer() {
        if (timer != null) {
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void setHashTag() {
        SpannableString styledString = new SpannableString(postCaption);
        if (postCaption != null && postCaption.length() > 0) {
            if (followingUser != null && followingUser.length() > 0) {
                String[] parts = followingUser.split(",");
                for (int i = 0; i < parts.length; i++) {
                    final String taggedName = parts[i];
                    if (postCaption.contains(taggedName)) {
                        // clickable text
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                Intent intent = new Intent(PostDetailBaseActivity.this, NewProfileActivity.class);
                                intent.putExtra("username", taggedName);
                                startActivity(intent);
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setColor(mContext.getResources().getColor(R.color.tenBlue));
                                ds.setUnderlineText(true);

                            }

                        };
                        int startIndex = postCaption.indexOf(taggedName);
                        int endIndex = startIndex + taggedName.length();
                        styledString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }
        //set style for hashtag--
        if (postCaption.contains("#")) {
            String[] allToken = postCaption.split(" ");
            if (allToken != null) {
                for (int i = 0; i < allToken.length; i++) {
                    String spaceToken = allToken[i];
                    if (spaceToken.contains("#")) {
                        StringTokenizer st = new StringTokenizer(spaceToken, "#");
                        while (st.hasMoreTokens()) {
                            final String hashToken = st.nextToken().trim();
                            if (hashToken.length() > 0) {
                                ClickableSpan clickableSpan = new ClickableSpan() {

                                    @Override
                                    public void onClick(View widget) {
                                        //Toast.makeText(baseActivity, "Clicked", Toast.LENGTH_SHORT).show();
                                        Bundle bundle = new Bundle();
                                        bundle.putString("hashTag", hashToken);

                                        Intent intent = new Intent(PostDetailBaseActivity.this, SearchActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(mContext.getResources().getColor(R.color.tenBlue));
                                        ds.setUnderlineText(true);

                                    }

                                };
                                int startIndex = postCaption.indexOf("#" + hashToken);
                                int endIndex = startIndex + hashToken.length() + 1;
                                styledString.setSpan(clickableSpan, startIndex, endIndex, 0);
                            }
                        }
                    }
                }
            }
        }
        if (singlePost.getHasImage() || (singlePost.getVideoUrl() != null && singlePost.getVideoUrl().length() > 0)) {
            mediaCaptionText.setText(styledString);
            mediaCaptionText.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            captionText.setText(styledString);
            captionText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    protected void checkPostExpiration() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (DateTimeUtil.checkExpiration(expirationTime)) {
            bottom_layout.setVisibility(View.GONE);
            TimeRemainingTextView.setText("11m");
            ClockImageView.setVisibility(View.GONE);
            TimeRemainingTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_btn_black_filled));
            TimeRemainingTextView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            //update time
            String expTime = DateTimeUtil.getRemainingTimeInMinutes(expirationTime);
            if (expTime.equals("Exp")) {
                bottom_layout.setVisibility(View.GONE);
                TimeRemainingTextView.setText("11m");
                ClockImageView.setVisibility(View.GONE);
                TimeRemainingTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_btn_black_filled));
                TimeRemainingTextView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                TimeRemainingTextView.setText(expTime);
                String xTime = expTime.substring(0, expTime.length() - 1);
                int intTime = Integer.parseInt(xTime);
                if (expTime.contains("s") || intTime < 5) {
                    TimeRemainingTextView.setTextColor(ContextCompat.getColor(mContext, R.color.red_color));
                } else if (intTime < 7) {
                    TimeRemainingTextView.setTextColor(ContextCompat.getColor(mContext, R.color.yellow_color));
                } else {
                    TimeRemainingTextView.setTextColor(ContextCompat.getColor(mContext, R.color.green_color));
                }
            }
        }
        TimeElapasedTextView.setText(DateTimeUtil.getElapseTimeInMinutes(remainingTime));
    }

    protected void updatePostData() {
        if (singlePost.getHasImage()) {
            playBtn.setVisibility(View.INVISIBLE);//Visibility = ViewStates.Invisible;
            progressBar.setVisibility(View.VISIBLE);//Visibility = ViewStates.Visible;
            PostImage.setVisibility(View.VISIBLE);//Visibility = ViewStates.Visible;
            postLayout.setVisibility(View.VISIBLE);
            Glide.with(mContext)
                    .load(HttpUtils.getPostImageURL(singlePost.getIdPost()))
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 5, 5))
                    .into(PostImage);
            if (singlePost.getVideoUrl() != null && !singlePost.getVideoUrl().equals("") && !singlePost.equals("null")) {
                playBtn.setVisibility(View.VISIBLE);
                //videoRelative.setVisibility(View.GONE);
            }
        } else {
            playBtn.setVisibility(View.GONE);//Visibility = ViewStates.Invisible;
            progressBar.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
            PostImage.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
            postLayout.setVisibility(View.GONE);
        }
        String commentCounts = String.valueOf(singlePost.getCommentCount());
        String repostCounts = String.valueOf(singlePost.getRepostCount());
        String likeCounts = String.valueOf(singlePost.getLikeCount());
        if (likeCounts.equals("0"))
            likeCounts = "";
        if (repostCounts.equals("0"))
            repostCounts = "";
        if (commentCounts.equals("0"))
            commentCounts = "";

        commentCount.setText(commentCounts);
        repostCount.setText(repostCounts);
        likeCount.setText(likeCounts);
        if (singlePost.getUserReposter() != null) {
            RepostedTextView.setVisibility(View.VISIBLE);
            if (singlePost.getUserReposter().getIdUser().equals(userId)) {
                RepostedTextView.setText("by you");// = "by you";
            } else {
                RepostedTextView.setText("by " + singlePost.getUserReposter().getUsername());//Text = "by " + post.userReposter.username;
            }
        } else {
            RepostedTextView.setVisibility(View.INVISIBLE);
            RepostedTextView.setText("");
        }

        if (singlePost.getIsReposted()) {
            RepostImage.setImageResource(R.drawable.repost_selected);
        } else {
            RepostImage.setImageResource(R.drawable.repost);
        }
        if (singlePost.getLiked() == true) {
            LikeImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.likeselected));
        } else {
            LikeImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.like));
        }
        if (singlePost.getCommented()) {
            commentImage.setImageResource(R.drawable.commentselected);

        } else {
            commentImage.setImageResource(R.drawable.comment);
        }
    }

    protected void playVideo(String url) {
        //videoRelative.setVisibility(View.VISIBLE);//;Visibility = ViewStates.Visible;
        videoView.setVisibility(View.VISIBLE);
        playBtn.setVisibility(View.GONE);

        videoView.setVideoPath(url);
        videoView.requestFocus();
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                //videoRelative.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
                videoView = null;
            }
        });
    }

    protected void updateList() {
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

    @Override
    public void dispose() {
    }


    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }
}
