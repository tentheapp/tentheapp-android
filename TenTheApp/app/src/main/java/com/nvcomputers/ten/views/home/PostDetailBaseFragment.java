package com.nvcomputers.ten.views.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.model.output.GetAllPostCommentOutput;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.presenter.SelectedPostPresenter;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.profile.NewProfileFragment;
import com.nvcomputers.ten.views.profile.ZoomImageActivity;
import com.nvcomputers.ten.views.search.SearchFragment;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by jindaldipanshu on 6/7/2017.
 */
/*
public class PostDetailBaseFragment extends BaseFragment {


    //Lists
    protected SelectedPostPresenter mSelectedPostPresenter;
    protected ArrayList<NewsFeedOutput.Posts> mTopBtmList;
    protected ArrayList<NewsFeedOutput.Posts> mLeftRgtList;
    protected ArrayList<NewsFeedOutput.Posts> mList;

    protected int leftRightPosition;
    protected int topBtmPosition;

    protected NewsFeedOutput.Posts singlePost;
    protected LikeCommentsCountsPresenter likeCommentsCountsPresenter;
    protected SuggestionFollowingAdapter followingListAdapter;
    public RecyclerView mCommentsRecyclerView;
    public TextView text;
    public RelativeLayout parentLayout;
    public TextView imageBack;
    private RelativeLayout bottom_layout;
    private RelativeLayout postLayout;

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


    protected void getDataFromBundle() {
        sharedPrefsHelper = new SharedPrefsHelper(getActivity());
        Bundle bundle = getArguments();
        mList = bundle.getParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA);
        int listPosition = bundle.getInt("position", 0);
        if (listPosition == -1) listPosition = 0;
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

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initViews(View view) {
        leftBtn = (TextView) findViewById(R.id.lefttbtn);
        topBtn = (TextView) findViewById(R.id.topbtn);
        rightBtn = (TextView) findViewById(R.id.rightbtn);
        bottomBtn = (TextView) findViewById(R.id.btnBottom);
        leftScrollBtn = (RelativeLayout) findViewById(R.id.swipeLeftBtn);
        rightScrollBtn = (RelativeLayout) findViewById(R.id.swipeRightBtn);
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
        //parentLayout.animate().translationX(parentLayout.getWidth());
        postLayout = (RelativeLayout) findViewById(R.id.post_layout_detail);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        PostImage = (ImageView) findViewById(R.id.postimage);
        playBtn = (ImageView) findViewById(R.id.playBtn1);
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
        captionText = (TextView) findViewById(R.id.captionText);
        mediaCaptionText = (TextView) findViewById(R.id.mediaCaptionText);
        text = (TextView) findViewById(R.id.text);

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
                Utilities.hideKeypad(PostImage);
                Intent intent = new Intent(getBaseActivity(), ZoomImageActivity.class);
                intent.putExtra("image", HttpUtils.getProfileImageURL(username));
                getBaseActivity().startActivity(intent);
                return false;
            }
        });


        PostImage.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Utilities.hideKeypad(PostImage);
            }

            @Override
            public void onDoubleClick(View v) {
                Utilities.hideKeypad(PostImage);
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
                        LikeImage.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.likeselected));
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


    @Override
    public void dispose() {

    }


    protected void showSuggestionDialog() {
        if (mSuggestionDialog == null)
            mSuggestionDialog = new Dialog(getActivity());
        mSuggestionDialog.setContentView(R.layout.suggestion_name_layout);
        // set the custom dialog components - text, image and button
        suggestionRV = (RecyclerView) mSuggestionDialog.findViewById(R.id.suggestionRV);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
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
                                Bundle bundle = new Bundle();
                                bundle.putString("username", taggedName);
                                replaceChildFragment(R.id.single_post_layout, new NewProfileFragment(), bundle);
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
                        ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.RED);
                        BackgroundColorSpan backgroundSpan = new BackgroundColorSpan(Color.YELLOW);

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
                                        replaceChildFragment(R.id.single_post_layout, new SearchFragment(),
                                                bundle);
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
        if (adapter != null) {
            adapter.refreshTime(linearLayoutManager);
        }
    }

    protected void updatePostData() {
        if (singlePost.getHasImage()) {
            postLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            PostImage.setVisibility(View.VISIBLE);

            Glide.with(mContext)
                    .load(HttpUtils.getPostImageURL(singlePost.getIdPost()))
                    .bitmapTransform(new RoundedCornersTransformation(mContext, 20, 15))
                    .into(PostImage);

            playBtn.setVisibility(View.INVISIBLE);
            if (singlePost.getVideoUrl() != null && !singlePost.getVideoUrl().equals("") && !singlePost.equals("null")) {
                playBtn.setVisibility(View.VISIBLE);
            }
        } else {
            postLayout.setVisibility(View.GONE);
            playBtn.setVisibility(View.GONE);//Visibility = ViewStates.Invisible;
            progressBar.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
            PostImage.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
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

    public CommentListAdapter adapter;

    protected void playVideo(String url) {
        try {
            postLayout.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.VISIBLE);
            playBtn.setVisibility(View.GONE);
            videoView.setVideoPath(url);
            videoView.requestFocus();
            videoView.start();
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.setVisibility(View.GONE);
                    playBtn.setVisibility(View.VISIBLE);
                    videoView = null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
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


}
}*/