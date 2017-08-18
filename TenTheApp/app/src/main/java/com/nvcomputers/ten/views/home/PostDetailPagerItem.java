package com.nvcomputers.ten.views.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.CommentListAdapter;
import com.nvcomputers.ten.adapter.SuggestionFollowingAdapter;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.interfaces.DoubleClickListener;
import com.nvcomputers.ten.model.output.GetAllPostCommentOutput;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.PostCommentOutput;
import com.nvcomputers.ten.model.output.PostCountResponse;
import com.nvcomputers.ten.model.output.RepostPostResponse;
import com.nvcomputers.ten.model.output.SearchFollowingUserResponse;
import com.nvcomputers.ten.model.output.UnFollowResponse;
import com.nvcomputers.ten.presenter.GetCommentListPresenter;
import com.nvcomputers.ten.presenter.LikeCommentsCountsPresenter;
import com.nvcomputers.ten.presenter.PostCommentPresenter;
import com.nvcomputers.ten.presenter.RepostPostPresenter;
import com.nvcomputers.ten.presenter.SearchFollowingPresenter;
import com.nvcomputers.ten.utils.DateTimeUtil;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.ProgressUtility;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.profile.NewProfileFragment;
import com.nvcomputers.ten.views.profile.ZoomImageActivity;
import com.nvcomputers.ten.views.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by rkumar4 on 7/5/2017.
 */

public class PostDetailPagerItem extends BaseFragment implements View.OnClickListener, AppCommonCallback,
        PostCommentPresenter.CommentCallback, RepostPostPresenter.RepostCallback, GetCommentListPresenter.CommentsCallback,
        View.OnLongClickListener, TextWatcher, SearchFollowingPresenter.SearchFollowingCallback, LikeCommentsCountsPresenter.LikeCommentsCallback {

    private FragmentManager framentManager;
    protected ImageView postImage, userImage, clockImageView, likeImage, repostImage, playBtn, addCommentImage, commentImage;
    protected TextView UsernameTextView, TimeRemainingTextView, TimeElapasedTextView, RepostedTextView;
    protected TextView likeCount, repostCount, commentCount, captionText, mediaCaptionText;
    public String userId, postId, likedPostStatus, remainingTime, expirationTime, mUserId, username;
    protected LikeCommentsCountsPresenter likeCommentsCountsPresenter;
    protected SuggestionFollowingAdapter followingListAdapter;
    protected NewsFeedOutput.Posts singlePost;
    public String postCaption;

    public RecyclerView mCommentsRecyclerView;
    private RelativeLayout bottom_layout;
    private RelativeLayout postLayout;
    protected VideoView videoView;
    protected ProgressBar progressBar;
    public TextView text;
    private boolean isMyPost = false;

    public RecyclerView suggestionRV;
    SharedPrefsHelper sharedPrefsHelper;//protected PopupMenu popup;
    public EditText edit_text_comment;
    private Timer timer;
    private int lastSavedPosition = 0;
    private boolean noMoreData = false;
    private boolean isLoading = false;
    private ProgressBar videoProgressBar;
    private int pagePosition;

    public PostDetailPagerItem() {

    }

    public PostDetailPagerItem(FragmentManager fm) {
        framentManager = fm;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPrefsHelper = new SharedPrefsHelper(mContext);
        mUserId = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_ID, "");
        Bundle bundle = getArguments();
        ArrayList<NewsFeedOutput.Posts> mList = bundle.getParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA);
        pagePosition = bundle.getInt("position", 0);
        getDataFromList(mList, pagePosition);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

   /* protected View findViewById(int id) {
        return mView.findViewById(id);
    }*/

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //cancel timer...
        cancelTimer();
        //init timer....
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        checkPostExpiration();
                    }
                });
            }
        }, 10000, 10000);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancelTimer();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.pager_item;
    }

    @Override
    protected void initViews(View view) {

        suggestionRV = (RecyclerView) findViewById(R.id.suggestionRV);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        suggestionRV.setLayoutManager(mLayoutManager);
        mCommentsRecyclerView = (RecyclerView) findViewById(R.id.comment_list);
        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        postLayout = (RelativeLayout) findViewById(R.id.post_layout_detail);
        userImage = (ImageView) findViewById(R.id.post_userimage);
        addCommentImage = (ImageView) findViewById(R.id.comment_image);
        edit_text_comment = (EditText) findViewById(R.id.edit_text_comment);
        captionText = (TextView) findViewById(R.id.captionText);
        mediaCaptionText = (TextView) findViewById(R.id.mediaCaptionText);
        likeCount = (TextView) findViewById(R.id.post_like_count);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        videoProgressBar = (ProgressBar) findViewById(R.id.video_progressBar);
        postImage = (ImageView) findViewById(R.id.postimage);
        playBtn = (ImageView) findViewById(R.id.playBtn1);
        UsernameTextView = (TextView) findViewById(R.id.username);
        TimeRemainingTextView = (TextView) findViewById(R.id.timeremaining);
        TimeElapasedTextView = (TextView) findViewById(R.id.timeelapsed);
        //RepostedTextView = (TextView) findViewById(R.id.repostlabel);
        //RepostedTextView.setOnClickListener(this);
        clockImageView = (ImageView) findViewById(R.id.clock);
        likeImage = (ImageView) findViewById(R.id.like);
        repostImage = (ImageView) findViewById(R.id.repost);

        repostCount = (TextView) findViewById(R.id.repost_count);
        commentCount = (TextView) findViewById(R.id.comment_count);
        commentImage = (ImageView) findViewById(R.id.comment);
        //videoRelative = (RelativeLayout) findViewById(R.id.videorelative);
        videoView = (VideoView) findViewById(R.id.postvideo);
        text = (TextView) findViewById(R.id.text);
        registerCommentChangeListsner();
        registerClickListener();
        setDataOnUi();
        if (singlePost != null) {
            updatePostData();
        }
        edit_text_comment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    //TODO-- showSuggestionDialog();
                }
            }
        });

        if (mUserId.equals(userId)) {
            likeImage.setImageResource(R.drawable.like);
            repostImage.setImageResource(R.drawable.repost);
        }

        postImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Utilities.hideKeypad(postImage);
                Intent intent = new Intent(getBaseActivity(), ZoomImageActivity.class);
                intent.putExtra("image", HttpUtils.getProfileImageURL(username));
                getBaseActivity().startActivity(intent);
                return false;
            }
        });


        postImage.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Utilities.hideKeypad(postImage);
            }

            @Override
            public void onDoubleClick(View v) {
                Utilities.hideKeypad(postImage);
                if (!mUserId.equals(userId)) {
                    if (!Utilities.checkInternet(getBaseActivity())) {
                        showToast(R.string.no_internet_msg);
                    } else {
                        if (!singlePost.getLiked())
                            likePost(postId);
                    }
                }
            }
        });

        if (likeCommentsCountsPresenter == null) {
            likeCommentsCountsPresenter = new LikeCommentsCountsPresenter(this, this);
        }
        likeCommentsCountsPresenter.countsApi(0, postId);
    }

    private void registerClickListener() {
        repostImage.setOnClickListener(this);
        addCommentImage.setOnClickListener(this);
        addCommentImage.setOnLongClickListener(this);
        UsernameTextView.setOnClickListener(this);
        likeImage.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        TimeRemainingTextView.setOnClickListener(this);
        findViewById(R.id.clock).setOnClickListener(this);
        edit_text_comment.addTextChangedListener(this);
    }

    private void setDataOnUi() {
        //other Components
        ImageView commentUserImage = (ImageView) findViewById(R.id.post_user_image);
        String localUser = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        String imageTag = sharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(mContext)
                .load(HttpUtils.getProfileImageURL(localUser))
                .bitmapTransform(new RoundedCornersTransformation(getBaseActivity(), 15, 2))
                .error(R.drawable.myprofilelarge)
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(commentUserImage);

        if (likeCommentsCountsPresenter == null)
            likeCommentsCountsPresenter = new LikeCommentsCountsPresenter(PostDetailPagerItem.this, PostDetailPagerItem.this);

        UsernameTextView.setText(username);
       /* Glide.with(mContext)
                .load(HttpUtils.getPostImageURL(postId))
                .into(postImage);*/

        Glide.with(mContext)
                .load(HttpUtils.getProfileImageURL(username))
                .bitmapTransform(new RoundedCornersTransformation(getBaseActivity(), 15, 0))
                .error(R.drawable.myprofilelarge)
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(userImage);
        setHashTag();
        checkPostExpiration();
        hitCommentApi();
    }

    protected void likePost(String idPost) {
        Call<UnFollowResponse> response = GetRestAdapter.getRestAdapter(true).likePost(idPost);
        response.enqueue(new Callback<UnFollowResponse>() {
            @Override
            public void onResponse(Call<UnFollowResponse> call,
                                   Response<UnFollowResponse> response) {
                if (response != null && response.body() != null) {
                    if (response.body().getSuccess().contains("true")) {
                        singlePost.setLiked(true);
                        MediaPlayer mp = MediaPlayer.create(mContext, R.raw.ticksound);
                        mp.start();
                        likeImage.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.likeselected));
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


   /* protected void showSuggestionDialog() {
        if (mSuggestionDialog == null)
            mSuggestionDialog = new Dialog(getActivity());
        mSuggestionDialog.setContentView(R.layout.suggestion_name_layout);
        // set the custom dialog components - text, image and button
        suggestionRV = (RecyclerView) mSuggestionDialog.findViewById(R.id.suggestionRV);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        suggestionRV.setLayoutManager(mLayoutManager);
    }*/

    private void registerCommentChangeListsner() {
        edit_text_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    addCommentImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.circle_selected_img));
                } else {
                    suggestionRV.setVisibility(View.GONE);
                    addCommentImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.circle_image));
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
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

    protected void setHashTag() {
        if (postCaption == null || postCaption.length() == 0) {
            return;
        }
        SpannableString styledString = new SpannableString(postCaption);
        if (postCaption != null && postCaption.length() > 0) {

            if (/*followingUser != null && followingUser.length() > 0*/postCaption.contains("@")) {
                String[] parts = postCaption.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    final String taggedName = parts[i];
                    if (/*postCaption.contains(taggedName)*/taggedName.contains("@")) {
                        // clickable text
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View widget) {
                                Bundle bundle = new Bundle();
                                bundle.putString("username", taggedName);
                                //replaceChildFragment(R.id.single_post_layout, new ProfileFragment(), bundle);
                                replacePostChildFragment(PostDetailFragment.frameLayout.getId(), new NewProfileFragment(), bundle);
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
                        //styledString.setS
                        //ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.RED);
                        //BackgroundColorSpan backgroundSpan = new BackgroundColorSpan(Color.YELLOW);
                        if (startIndex != -1 && endIndex <= styledString.length()) {
                            styledString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
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
                                        //replaceChildFragment(R.id.single_post_layout, new SearchFragment(),bundle);
                                        replacePostChildFragment(PostDetailFragment.frameLayout.getId(), new SearchFragment(), bundle);
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
                                if (startIndex != -1 && endIndex <= styledString.length()) {
                                    styledString.setSpan(clickableSpan, startIndex, endIndex, 0);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (singlePost.getHasImage() || (singlePost.getVideoUrl() != null && singlePost.getVideoUrl().length() > 0)) {
            captionText.setText("");
            mediaCaptionText.setText(styledString);
            mediaCaptionText.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mediaCaptionText.setText("");
            captionText.setText(styledString);
            captionText.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    protected void checkPostExpiration() {
        if (expirationTime == null || expirationTime.length() == 0) {
            return;
        }
        if (DateTimeUtil.checkExpiration(expirationTime)) {
            bottom_layout.setVisibility(View.GONE);
            TimeRemainingTextView.setText("11m");
            clockImageView.setVisibility(View.GONE);
            TimeRemainingTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_btn_black_filled));
            TimeRemainingTextView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            //update time
            String expTime = DateTimeUtil.getRemainingTimeInMinutes(expirationTime);
            if (expTime.equals("Exp")) {
                bottom_layout.setVisibility(View.GONE);
                TimeRemainingTextView.setText("11m");
                clockImageView.setVisibility(View.GONE);
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
        if (adapter != null) {
            adapter.refreshTime(linearLayoutManager);
        }
    }

    protected void updatePostData() {
        if (singlePost.getHasImage()) {
            postLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            postImage.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(HttpUtils.getPostImageURL(singlePost.getIdPost()))
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 15, 5))
                    .into(postImage);

            playBtn.setVisibility(View.GONE);
            if (singlePost.getVideoUrl() != null && !singlePost.getVideoUrl().equals("") && !singlePost.equals("null")) {
                playBtn.setVisibility(View.VISIBLE);
                if (PostDetailFragment.leftRightPosition == 0 && pagePosition == 0) {
                    playVideo(singlePost.getVideoUrl());
                }
            }
        } else {
            postLayout.setVisibility(View.GONE);
            playBtn.setVisibility(View.GONE);//Visibility = ViewStates.Invisible;
            progressBar.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
            postImage.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
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
//        if (singlePost.getUserReposter() != null) {
//            RepostedTextView.setVisibility(View.VISIBLE);
//            if (singlePost.getUserReposter().getIdUser().equals(userId)) {
//                RepostedTextView.setText("by you");// = "by you";
//            } else {
//                RepostedTextView.setText("by " + singlePost.getUserReposter().getUsername());//Text = "by " + post.userReposter.username;
//            }
//        } else {
//            RepostedTextView.setVisibility(View.INVISIBLE);
//            RepostedTextView.setText("");
//        }
        if (singlePost.getIsReposted()) {
            repostImage.setImageResource(R.drawable.repost_selected);
        } else {
            if (mUserId.equals(userId)) {
                repostImage.setImageResource(R.drawable.repost);
            } else {
                repostImage.setImageResource(R.drawable.repost);
            }

        }
        if (singlePost.getLiked() == true) {
            likeImage.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.likeselected));
        } else {
            if (mUserId.equals(userId)) {
                likeImage.setImageResource(R.drawable.like);
            } else {
                likeImage.setImageResource(R.drawable.like);
            }
        }
        if (singlePost.getCommented()) {
            addCommentImage.setImageResource(R.drawable.circle_selected_img);

        } else {
            addCommentImage.setImageResource(R.drawable.circle_image);
        }
    }

    public CommentListAdapter adapter;

    protected void playVideo(String url) {
        try {
            postLayout.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            videoProgressBar.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.GONE);
            videoView.setVideoPath(url);
            videoView.requestFocus();
            videoView.start();
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            videoProgressBar.setVisibility(View.GONE);
                        }
                    });
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    videoView.setVisibility(View.GONE);
                    playBtn.setVisibility(View.VISIBLE);
                    //videoView = null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispose() {

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String comment = edit_text_comment.getText().toString();
        if (!TextUtils.isEmpty(comment)) {
            if (comment.contains("@")) {
                if (comment.length() > 1) {
                    int index = comment.indexOf("@");
                    Log.e("index--", index + "");
                    StringTokenizer stringTokenizer = new StringTokenizer(comment, "@");
                    String token = null;
                    while (stringTokenizer.hasMoreTokens()) {
                        token = stringTokenizer.nextToken();
                        Log.e("first--", token);
                    }
                    if (!token.contains(" ")) {
                        hitFollowingSuggestionsApi(token, index);
                        Log.e("last--", token);
                    }
                } else {
                    suggestionRV.setVisibility(View.GONE);
                }
            } else {
                suggestionRV.setVisibility(View.GONE);
            }
        }
    }


    private void hitFollowingSuggestionsApi(String suggestion, int index) {
        Log.e("suggesstion--", suggestion);
        if (Utilities.checkInternet(getBaseActivity())) {
            SearchFollowingPresenter searchFollowingPresenter = new SearchFollowingPresenter(this, this);
            searchFollowingPresenter.responseCheck(mUserId, suggestion);
        } else {
            Utilities.showSmallToast(getBaseActivity(), getString(R.string.no_internet_msg));
        }
    }

    private static final int VIDEO_CODE = 676;

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.comment_image:
                Intent intent = new Intent(getBaseActivity(), VideoCommentsActivity.class);
                startActivityForResult(intent, VIDEO_CODE);
                break;
        }
        return false;
    }

    public void gotoProfile(Bundle bundle) {
        replaceChildFragment(PostDetailFragment.frameLayout.getId(), new NewProfileFragment(), bundle);
    }

    @Override
    public void onClick(View v) {

        Utilities.hideKeypad(v);
        String commentText = edit_text_comment.getText().toString();
        switch (v.getId()) {
            case R.id.like:
                if (!userId.equals(mUserId)) {
                    if (!Utilities.checkInternet(getBaseActivity())) {
                        showToast(R.string.no_internet_msg);
                    } else {
                        if (!singlePost.getLiked()) {
                            likePost(postId);
                        }
                    }
                }
                break;
            case R.id.repostlabel:
                Intent intent = new Intent(mContext, RepostedUserListActivity.class);
                intent.putExtra("postId", postId);
                startActivity(intent);
                break;
            case R.id.playBtn1:
                playVideo(singlePost.getVideoUrl());
                break;
            case R.id.title_layout:
                break;
            case R.id.repost:
                if (singlePost.getUserPoster().getIdUser().equals(mUserId)) {
                    Utilities.showlongToast(getBaseActivity(), "You can't re-post your own post.");
                    return;
                }
                if (singlePost.getIsReposted()) {
                    Utilities.showlongToast(getBaseActivity(), "You already re-post this post.");
                    return;
                }
                new AlertDialog.Builder(getBaseActivity()).setTitle("Repost?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            repostImage.setImageResource(R.drawable.refresh);
                            repostPost(singlePost.getIdPost());
                        } catch (Exception eee) {
                            eee.printStackTrace();
                        } finally {
                            if (!singlePost.getIsReposted()) {
                                repostImage.setImageResource(R.drawable.repost);
                            }
                        }
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
                break;
            case R.id.clock:
                Bundle bundles = new Bundle();
                bundles.putString("postId", postId);
                //replaceChildFragment(R.id.single_post_layout, new TimersFragment(), bundles);
                replacePostChildFragment(PostDetailFragment.frameLayout.getId(), new TimersFragment(), bundles);
                break;

            case R.id.timeremaining:
                Bundle bundless = new Bundle();
                bundless.putString("postId", postId);
                //replaceChildFragment(R.id.single_post_layout, new TimersFragment(), bundless);
                replacePostChildFragment(PostDetailFragment.frameLayout.getId(), new TimersFragment(), bundless);
                break;

            case R.id.username:
                if (!mUserId.equals(userId)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", username);
                    //replaceChildFragment(PostDetailFragment.frameLayout.getId(), new ProfileFragment(), bundle);
                    replacePostChildFragment(PostDetailFragment.frameLayout.getId(), new NewProfileFragment(), bundle);
                }
                break;
            case R.id.comment_image:
                Utilities.hideKeypad(addCommentImage);
                if (!Utilities.checkInternet(getBaseActivity())) {
                    Utilities.showSmallToast(getBaseActivity(), getString(R.string.no_internet_msg));
                } else if (DateTimeUtil.checkExpiration(expirationTime)) {
                    Utilities.showSmallToast(getBaseActivity(), "You can't add comment on expired post.");
                } else {
                    if (!TextUtils.isEmpty(commentText)) {
                        edit_text_comment.setText("");
                        ProgressUtility.showProgress(getBaseActivity(), getString(R.string.please_wait_meassge));
                        PostCommentPresenter presenter = new PostCommentPresenter(this);
                        presenter.responseCheck(postId, commentText, null);
                    }
                }
                break;
//                Intent intent1 = new Intent(getActivity(), CommentsActivity.class);
//                intent1.putExtra("postId", postId);
//                intent1.putExtra("expirationTime", expirationTime);
//                if (userId.equals(mUserId))
//                    intent1.putExtra("isMyPost", true);
//                else
//                    intent1.putExtra("isMyPost", false);
//                startActivity(intent1);
//                break;
            case R.id.postimage:
                Intent i = new Intent(getActivity(), ZoomImageActivity.class);
                i.putExtra("image", HttpUtils.getPostImageURL(postId));// HttpUtils.getProfileImageURL(username));
                i.putExtra("username", username);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == VIDEO_CODE) {
            if (data != null) {
               /* mVideoPath = data.getStringExtra("recordedPath");
                ProgressUtility.showProgress(getActivity(), getString(R.string.please_wait_meassge));
                PostCommentPresenter presenter = new PostCommentPresenter(this);
                presenter.responseCheck(postId, "Video Comment", mVideoPath);*/
                String mVideoPath = data.getStringExtra("recordedPath");
                String mImagePath = data.getStringExtra("imagePath");
                ProgressUtility.showProgress(getBaseActivity(), getString(R.string.please_wait_meassge));
                if (mVideoPath == null || mVideoPath.equals("")) {
                    PostCommentPresenter presenter = new PostCommentPresenter(this);
                    presenter.responseCheck(postId, "Image_Comment", mImagePath);
                } else {
                    PostCommentPresenter presenter = new PostCommentPresenter(this);
                    presenter.responseCheck(postId, "Video_Comment", mVideoPath);
                }
            }
        }
    }

    @Override
    public void setAdapter(RecyclerView recyclerView, ArrayList<?> mList) {
        if (userId.equals(mUserId))
            isMyPost = true;
        else
            isMyPost = false;
        super.setAdapter(recyclerView, mList);
        adapter = new CommentListAdapter(this, mList, postId, PostDetailFragment.frameLayout.getId(), isMyPost);
        mCommentsRecyclerView.setAdapter(adapter);
    }

    private void repostPost(String postId) {
        RepostPostPresenter repostPostPresenter = new RepostPostPresenter(this, this);
        repostPostPresenter.responseCheck(postId);
    }

    @Override
    public void setProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        //ProgressUtility.showProgress(activity, "Loading...");
    }

    @Override
    public void dismiss() {
        progressBar.setVisibility(View.GONE);
        ProgressUtility.dismissProgress();
    }

    @Override
    public void postCommmentError(Call<PostCommentOutput> call, Throwable t) {
        Utilities.showMessage(getBaseActivity(), getString(R.string.server_error_msg));
        dismiss();
    }

    @Override
    public void postCommmentSuccess(Response<PostCommentOutput> response) {
        ProgressUtility.dismissProgress();
        if (response != null && response.body() != null) {
            if (response.code() == 200) {
                //singlePost.setCommented(true);
                edit_text_comment.setText("");
                /*setProgressBar();
                GetCommentListPresenter presenter = new GetCommentListPresenter(this);
                presenter.responseCheck(postId);*/
                hitCommentApi();
                //likeCommentsCountsPresenter.countsApi(0, postId);
            } else {
                Utilities.showSmallToast(getBaseActivity(), getString(R.string.no_data_found_msg));
            }
        } else {
            Utilities.showSmallToast(getBaseActivity(), getString(R.string.server_error_msg));
        }
    }

    @Override
    public void repostError(Call<RepostPostResponse> call, Throwable t) {
        showToast(R.string.server_error_msg);
    }

    @Override
    public void onRepostSuccess(Response<RepostPostResponse> response) {
        if (response != null && response.body() != null) {
            singlePost.setIsReposted(true);
            likeCommentsCountsPresenter.countsApi(0, postId);
        } else {
            Utilities.showMessage(getActivity(), getString(R.string.server_error_msg));
        }
    }

    private ArrayList<GetAllPostCommentOutput.Comments> getcommentList;

    public void hitCommentApi() {
        setProgressBar();
        if (getcommentList == null)
            loadMoreItems(0);
        else
            loadMoreItems(getcommentList.size());
    }

    private void loadMoreItems(int listCount) {
        if (listCount == 0 && !isLoading) {
            //currentPageNumber = 1;
            isLoading = true;
            lastSavedPosition = 0;
            noMoreData = false;
            getcommentList = new ArrayList<>();
           /* adapter = new NewsPostAdapter(this, newsFeedOutput, R.id.home_frame_layout, manager);
            adapter.isOnline(true);
            recyclerViewPosts   .setAdapter(adapter);*/
            GetCommentListPresenter presenter = new GetCommentListPresenter(this);
            presenter.responseCheck(postId, listCount + "");
        } else {
            lastSavedPosition = listCount - 1;
            if (/*listCount >= 10 &&*/ !noMoreData && !isLoading/*listCount % 10 == 0*/) {
                isLoading = true;
                //int value = listCount / 10;
                //currentPageNumber = value + 1;
                GetCommentListPresenter presenter = new GetCommentListPresenter(this);
                presenter.responseCheck(postId, listCount + "");
            } else {
                ProgressUtility.dismissProgress();
            }
        }


    }

    @Override
    public void getCommentListError(Call<GetAllPostCommentOutput> call, Throwable t) {
        Utilities.showSmallToast(getBaseActivity(), t.getMessage());
        isLoading = false;
        dismiss();
    }

    @Override
    public void getCommentListSuccess(Response<GetAllPostCommentOutput> response) {
        dismiss();
        isLoading = false;
        if (response != null) {
            GetAllPostCommentOutput body = response.body();
            if (body != null) {
                ArrayList<GetAllPostCommentOutput.Comments> getlist = body.getComments();
                if (getlist != null && getlist.size() > 0) {
                    getcommentList.addAll(getlist);
                    mCommentsRecyclerView.setVisibility(View.VISIBLE);
                    text.setVisibility(View.GONE);
                } else {
                    text.setVisibility(View.VISIBLE);
                    if (expirationTime != null) {
                        if (DateTimeUtil.checkExpiration(expirationTime)) {
                            text.setText("No comments or reactions");
                        } else {
                            text.setText("Be the first one to add comment or reaction");
                        }
                    } else {
                        text.setText("No comments or reactions");
                    }
                    mCommentsRecyclerView.setVisibility(View.GONE);
                }
            } else {
                showToast(R.string.server_error_msg);
            }
            setAdapter(mCommentsRecyclerView, getcommentList);
        } else {
            showToast(R.string.server_error_msg);
        }
    }


    @Override
    public void searchFollowingnError(Call<SearchFollowingUserResponse> call, Throwable t) {
        Utilities.showMessage(getActivity(), getString(R.string.server_error_msg));
    }

    @Override
    public void onSearchFollowingSuccess(Response<SearchFollowingUserResponse> response) {
        SearchFollowingUserResponse body = response.body();
        if (body != null) {
            List<SearchFollowingUserResponse.Users> usersDataList = body.getUsers();
            suggestionRV.setVisibility(View.VISIBLE);
            if (usersDataList == null || usersDataList.size() == 0) {
                suggestionRV.setVisibility(View.GONE);
                return;
            }
            followingListAdapter = new SuggestionFollowingAdapter(this, usersDataList);
            suggestionRV.setAdapter(followingListAdapter);
        } else {
            Utilities.showMessage(getActivity(), getString(R.string.server_error_msg));
        }
    }

    @Override
    public void countsError(String error) {
    }

    @Override
    public void getCounts(int pos, PostCountResponse postCountResponse) {
        likeCount.setText(postCountResponse.getPost().getLikeCount() + "");
        commentCount.setText(postCountResponse.getPost().getCommentCount() + "");
        repostCount.setText(postCountResponse.getPost().getRepostCount() + "");
        if (singlePost != null) {
            singlePost.setRepostCount(postCountResponse.getPost().getRepostCount());
            singlePost.setLikeCount(postCountResponse.getPost().getLikeCount());
            singlePost.setCommentCount(postCountResponse.getPost().getCommentCount());
            updatePostData();
        }
    }

    /**
     * This method is used to replace Child Fragment
     *
     * @param childFragment
     * @param bundle
     */
    public void replacePostChildFragment(int frameLayout, Fragment childFragment, Bundle bundle) {

        FragmentTransaction transaction = framentManager.beginTransaction();
        if (bundle != null) {
            childFragment.setArguments(bundle);
        }
        transaction.addToBackStack(null);
        transaction.replace(frameLayout, childFragment).commit();//R.id.fragment_mainLayout
    }

    public void refreshStopVideo() {
        try {
            videoView.stopPlayback();
            videoView.setVisibility(View.GONE);
            videoProgressBar.setVisibility(View.GONE);
            playBtn.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshPlayVideo() {
        if (!videoView.isPlaying() && singlePost.getVideoUrl() != null && singlePost.getVideoUrl().contains("http")) {
            playVideo(singlePost.getVideoUrl());
        }
    }
}
