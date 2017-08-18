package com.nvcomputers.ten.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.imagechooser.api.ChooserType;
import com.nvcomputers.ten.interfaces.DoubleClickListener;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.PostCountResponse;
import com.nvcomputers.ten.model.output.ProfileResponse;
import com.nvcomputers.ten.model.output.RepostPostResponse;
import com.nvcomputers.ten.model.output.TopLikersResponse;
import com.nvcomputers.ten.model.output.UnFollowResponse;
import com.nvcomputers.ten.presenter.LikeCommentsCountsPresenter;
import com.nvcomputers.ten.utils.DateTimeUtil;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.home.PostDetailActivity;
import com.nvcomputers.ten.views.home.PostDetailFragment;
import com.nvcomputers.ten.views.home.PostListFragment;
import com.nvcomputers.ten.views.home.TimersActivity;
import com.nvcomputers.ten.views.home.TimersFragment;
import com.nvcomputers.ten.views.profile.FollowersActivity;
import com.nvcomputers.ten.views.profile.FollowersFragment;
import com.nvcomputers.ten.views.profile.NewProfileActivity;
import com.nvcomputers.ten.views.profile.NewProfileFragment;
import com.nvcomputers.ten.views.profile.ZoomImageActivity;
import com.nvcomputers.ten.views.search.SearchFragment;

import java.util.ArrayList;
import java.util.StringTokenizer;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 7/6/2017.
 */

public class UserProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements
        LikeCommentsCountsPresenter.LikeCommentsCallback {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private NewProfileActivity newProfileActivity;
    private String mScreen;
    private String mUsername;
    private String mBlocked = "true";
    private String mUserId, friendId, mIsPrivate, mNickName, mNotify, mDescription;
    private ArrayList<NewsFeedOutput.Posts> postsList;
    private int frameLayout;
    private String followingUser;
    private String localUserId, userName;
    private BaseFragment baseFragment;
    private SharedPrefsHelper sharedPrefsHelper;
    private BaseActivity baseActivity;
    private VideoView currentVideoView;
    private ProfileResponse.Profile profileData;
    private Context mContext;
    private ArrayList<TopLikersResponse.Users> topProfileList;
    private NewProfileFragment newProfileFragment;
    private LinearLayoutManager mLinearManager;
    private HeaderViewHolder headerView;

    public UserProfileAdapter(NewProfileFragment newProfileFragment, BaseFragment baseFragment, ProfileResponse.Profile profileData, int frameLayout, String screenName) {
        this.mContext = baseFragment.mContext;
        this.mScreen = screenName;
        this.newProfileFragment = newProfileFragment;
        this.baseFragment = baseFragment;
        this.baseActivity = baseFragment.getBaseActivity();
        this.frameLayout = frameLayout;
        this.profileData = profileData;
        sharedPrefsHelper = new SharedPrefsHelper(mContext);
        getData();
    }

    public UserProfileAdapter(NewProfileActivity newProfileActivity, BaseActivity baseActivity, ProfileResponse.Profile profileData, int frameLayout, String screenName) {
        this.mContext = baseActivity.mContext;
        this.newProfileActivity = newProfileActivity;
        this.mScreen = screenName;
        this.baseActivity = baseActivity;
        this.frameLayout = frameLayout;
        this.profileData = profileData;
        sharedPrefsHelper = new SharedPrefsHelper(baseActivity.mContext);
        getData();
    }

    private void getData() {

        localUserId = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_ID, "");
        userName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        followingUser = sharedPrefsHelper.get(PreferenceKeys.PREF_FOLLOWING_LIST, "");
        if (profileData == null || profileData.getUser() == null) {
            if (newProfileActivity == null) {
                mUserId = newProfileFragment.mUserId;
                mUsername = newProfileFragment.mUserName;
            } else {
                mUserId = newProfileActivity.mUserId;
                mUsername = newProfileActivity.mUserName;
            }
            return;
        }
        mUserId = profileData.getUser().getIdUser();
        mUsername = profileData.getUser().getUsername();
        mBlocked = profileData.getUser().getBlocked();
        friendId = profileData.getUser().getFriended();
        mIsPrivate = profileData.getUser().getIsPrivate();
        mNickName = profileData.getUser().getWebsite();
        mNotify = profileData.getUser().getIsNotify();
        mDescription = profileData.getUser().getDescription();
    }

    public ArrayList<NewsFeedOutput.Posts> getPostList() {
        return postsList;
    }

    public void updatePostList(ArrayList<NewsFeedOutput.Posts> posts) {
        this.postsList = posts;
        notifyDataSetChanged();
    }

   /* public void updateTopTimer(ArrayList<TopLikersResponse.Users> profileResponse) {
        this.topProfileList = profileResponse;
        if (headerView != null) {
           // headerView.topLikerProgress.setVisibility(View.GONE);
            if (topProfileList != null && topProfileList.size() > 0) {
                ProfileAdapter adapter;
                if (baseFragment == null) {
                    adapter = new ProfileAdapter(baseActivity, topProfileList, 1);
                } else {
                    adapter = new ProfileAdapter(baseFragment, topProfileList, 2);
                }
               // headerView.topTimersRV.setAdapter(adapter);
              //  headerView.topLikerProgress.setVisibility(View.GONE);
            } else if (topProfileList != null && topProfileList.size() == 0) {
               // headerView.topLikerProgress.setVisibility(View.GONE);
                headerView.text_view_user_blocked.setVisibility(View.VISIBLE);
                headerView.text_view_user_blocked.setText("No Top Timers");
            }
        }
        //notifyItemChanged(0);
    }*/

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_header_layout, viewGroup, false);
            return new HeaderViewHolder(view);

        } else if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news_feed_layout, viewGroup, false);
            return new ItemViewHolder(view);

        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    public void updateProfileImage(Bitmap bitmap){
        if (headerView!=null){
            headerView.userImage.setImageBitmap(bitmap);
        }
    }

    public ImageView getImageView(){
        return headerView.userImage;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof HeaderViewHolder) {
            //set the Value from List to corresponding UI component as shown below.
            headerView = (HeaderViewHolder) holder;
            setHeaderData(headerView);
            //ProfileAdapter profileAdapter = new ProfileAdapter(this, mList, 2);
            //((HeaderViewHolder) holder).topTimersRV.setAdapter(profileAdapter);

        } else if (holder instanceof ItemViewHolder) {
            // Your code here
            int pos = position - 1;
            setPostData((ItemViewHolder) holder, pos);
        }
    }

    private void setHeaderData(HeaderViewHolder holder) {

        //mHeaderHolder = holder;
        if (profileData == null || profileData.getUser() == null) {
            if (mScreen != null && mScreen.equals("1")) {
                setOwnProfile(holder);
            }
            return;
        }
        if (mScreen != null && mScreen.equals("1")) {
            sharedPrefsHelper.save(PreferenceKeys.PREF_USER_FOLLOWERS, profileData.getFollowersCount());
            sharedPrefsHelper.save(PreferenceKeys.PREF_USER_FOLLOWING, profileData.getFollowingCount());
            sharedPrefsHelper.save(PreferenceKeys.PREF_USER_POST_COUNT, profileData.getAllPostCount());
            sharedPrefsHelper.save(PreferenceKeys.PREF_USER_DESC_NICK, profileData.getUser().getWebsite());
            sharedPrefsHelper.save(PreferenceKeys.PREF_USER_DESC, profileData.getUser().getDescription());
        }
        holder.editProfilText.setVisibility(View.VISIBLE);
        if (profileData.getFollowersCount() != null) {   //show Followers count
            holder.followersText.setText(profileData.getFollowersCount());
        } else {
            holder.followersText.setText("0");
        }
        if (profileData.getFollowingCount() != null) {   //show Following count
            holder.followingText.setText(profileData.getFollowingCount());
        } else {
            holder.followingText.setText("0");
        }
        if (!TextUtils.isEmpty(profileData.getAllPostCount())) {     //show Allcount
            holder.postsCountText.setText(profileData.getAllPostCount());
        } else {
            holder.postsCountText.setText("0");
        }

        //CharSequence mNickName = profileData.getUser().getWebsite();
        if (TextUtils.isEmpty(mScreen)) {
            setFriended(profileData.getUser().getFriended(), holder.editProfilText);
        } else {
            holder.editProfilText.setText("edit");
            holder.editProfilText.setTextColor(baseActivity.getResources().getColor(R.color.black));
        }
        String imageTag = sharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(mContext)
                .load(HttpUtils.getProfileImageURL(profileData.getUser().getUsername()))
                .error(baseActivity.getResources().getDrawable(R.drawable.myprofilelarge))
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .centerCrop()
                .into(holder.userImage);

    }

    private void setOwnProfile(HeaderViewHolder holder) {
        holder.editProfilText.setVisibility(View.VISIBLE);
        mBlocked = "false";
        holder.followersText.setText("" + sharedPrefsHelper.get(PreferenceKeys.PREF_USER_FOLLOWERS, "0"));
        holder.followingText.setText("" + sharedPrefsHelper.get(PreferenceKeys.PREF_USER_FOLLOWING, "0"));
        holder.postsCountText.setText("" + sharedPrefsHelper.get(PreferenceKeys.PREF_USER_POST_COUNT, "0"));
        holder.editProfilText.setText("edit");
        holder.editProfilText.setTextColor(baseActivity.getResources().getColor(R.color.black));

        String imageTag = sharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(mContext)
                .load(HttpUtils.getProfileImageURL(sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "")))
                .error(baseActivity.getResources().getDrawable(R.drawable.myprofilelarge))
                .bitmapTransform(new RoundedCornersTransformation(mContext, 20, 15))
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(holder.userImage);
    }

    private void setFriended(String friendId, TextView mEditProfileTextView) {
        if (TextUtils.isEmpty(friendId)) {
            mEditProfileTextView.setText("Follow");
            mEditProfileTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_white));
        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_friended))) {
            mEditProfileTextView.setText("Following");
            mEditProfileTextView.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_green));
        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_pending))) {
            mEditProfileTextView.setText("Requested");
            mEditProfileTextView.setTextColor(mContext.getResources().getColor(android.R.color.white));
            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_black_filled));
        } else if (friendId.contains(mContext.getResources().getString(R.string.friended_rejected)) ||
                friendId.contains(mContext.getResources().getString(R.string.friended_none))
                ) {
            mEditProfileTextView.setText("Follow");
            mEditProfileTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mEditProfileTextView.setBackground(mContext.getResources().getDrawable(R.drawable.rounded_btn_white));
        }
    }

    public void updateTimeData(ItemViewHolder mViewHolder, int mPosition) {
        if (mViewHolder == null || postsList == null || postsList.size() == 0) {
            return;
        }

        NewsFeedOutput.Posts post = postsList.get(mPosition);
        try {
            String expTime = DateTimeUtil.getRemainingTimeInMinutes(post.getExpiration());
            if (expTime.equals("Exp")) {
                mViewHolder.timeRemainingTextView.setText(DateTimeUtil.getTopTime(post.getExpiration(), post.getDatestamp()));
                mViewHolder.timeRemainingTextView.setBackground(ContextCompat.getDrawable(baseActivity, R.drawable.rounded_btn_black_filled));
                mViewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.white));
                mViewHolder.clockImageView.setVisibility(View.GONE);
            } else {
                mViewHolder.timeRemainingTextView.setText(expTime);
                mViewHolder.timeRemainingTextView.setBackground(null);
                mViewHolder.clockImageView.setVisibility(View.VISIBLE);
                String xTime = expTime.substring(0, expTime.length() - 1);
                int intTime = Integer.parseInt(xTime);
                if (expTime.contains("s") || intTime < 5) {
                    mViewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.red_color));
                } else if (intTime < 7) {
                    mViewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.yellow_color));
                } else {
                    mViewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.green_color));
                }
            }
            // mViewHolder.timeRemainingTextView.setText(DateTimeUtil.getRemainingTimeInMinutes(post.getExpiration()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mViewHolder.timeElapasedTextView.setText(DateTimeUtil.getElapseTimeInMinutes(post.getDatestamp()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPostData(ItemViewHolder viewHolder, int position) {
        if (postsList == null || postsList.size() - 1 < position)
            return;
        NewsFeedOutput.Posts post = null;
        try {
            post = postsList.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String url = HttpUtils.getProfileImageURL(post.getUserPoster().getUsername());
        String imageTag = sharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(baseActivity)
                .load(url)
                //.bitmapTransform(new RoundedCornersTransformation(baseActivity, 15, 5))
                .error(R.drawable.myprofilelarge)
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(viewHolder.userImage);

        viewHolder.playBtn.setVisibility(View.INVISIBLE);//Visibility = ViewStates.Invisible;
        viewHolder.usernameTextView.setText(post.getUserPoster().getUsername()); //= post.userPoster.username;

//        if (post.getUserReposter() != null) {
//            viewHolder.repostedTextView.setVisibility(View.VISIBLE);
//            if (post.getUserReposter().getIdUser().equals(localUserId)) {
//                viewHolder.repostedTextView.setText("by you");// = "by you";
//            } else {
//                viewHolder.repostedTextView.setText("by " + post.getUserReposter().getUsername());//Text = "by " + post.userReposter.username;
//            }
//        } else {
//            viewHolder.repostedTextView.setVisibility(View.INVISIBLE);
//            viewHolder.repostedTextView.setText("");
//        }

        if (post.getIsReposted()) {
            viewHolder.repostIcon.setImageResource(R.drawable.repost_selected);
        } else {
            viewHolder.repostIcon.setImageResource(R.drawable.repost);
        }

        if (post.getUserPoster().getIsPrivate()) {
            viewHolder.repostIcon.setEnabled(false);
        } else {
            viewHolder.repostIcon.setEnabled(true);
        }

        viewHolder.mUserNameTextView.setText(post.getUserPoster().getUsername());
        SpannableString styledString = new SpannableString(post.getText());
        //styledString.setSpan(new ForegroundColorSpan(R.color.tenBlue), 15, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //set style for user name--
        if (/*followingUser != null && followingUser.length() > 0 &&*/ post.getText().contains("@")) {

            String[] parts = post.getText().split(" ");

            for (int i = 0; i < parts.length; i++) {
                final String taggedName = parts[i];
                if (/*post.getText().contains(taggedName)*/taggedName.contains("@")) {
                    // clickable text
                    ClickableSpan clickableSpan = new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            // Toast.makeText(baseActivity, "Clicked", Toast.LENGTH_SHORT).show();
                            Bundle bundle = new Bundle();
                            bundle.putString("username", taggedName);
                            baseFragment.replaceChildFragment(frameLayout, new NewProfileFragment(),
                                    bundle);
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setColor(baseActivity.getResources().getColor(R.color.tenBlue));
                            ds.setUnderlineText(false);

                        }
                    };
                    if (taggedName != null && taggedName.length() > 0) {
                        int startIndex = post.getText().indexOf(/*"@" +*/ taggedName);
                        int endIndex = startIndex + taggedName.length()/* + 1*/;
                        if (startIndex != -1 && endIndex <= styledString.length()) {
                            //styledString.setSpan(clickableSpan, startIndex, endIndex, 0);
                            styledString.setSpan(clickableSpan, startIndex, endIndex, 0);
                        }
                    }
                }
            }

        }
        //set style for hashtag--
        if (post.getText().contains("#")) {
            String[] allToken = post.getText().split(" ");
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
                                        baseFragment.replaceChildFragment(frameLayout, new SearchFragment(),
                                                bundle);
                                    }

                                    @Override
                                    public void updateDrawState(TextPaint ds) {
                                        super.updateDrawState(ds);
                                        ds.setColor(baseActivity.getResources().getColor(R.color.tenBlue));
                                        ds.setUnderlineText(false);

                                    }

                                };
                                int startIndex = post.getText().indexOf("#" + hashToken);
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

        if (post.getPostTypeImage() || (post.getVideoUrl() != null && post.getVideoUrl().length() > 0)) {
            viewHolder.captionTextView.setText("");
            viewHolder.mediaCaption.setText(styledString);
            viewHolder.mediaCaption.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            viewHolder.mediaCaption.setText("");
            viewHolder.captionTextView.setText(styledString);
            viewHolder.captionTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        String expTime = DateTimeUtil.getRemainingTimeInMinutes(post.getExpiration());
        if (expTime.equals("Exp")) {
            viewHolder.clockImageView.setVisibility(View.GONE);
            viewHolder.timeRemainingTextView.setText("expired");
            // viewHolder.timeRemainingTextView.setBackground(ContextCompat.getDrawable(baseActivity, R.drawable.rounded_btn_black_filled));
            viewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.red_color));
        } else {
            viewHolder.timeRemainingTextView.setText(expTime);
            viewHolder.timeRemainingTextView.setBackground(null);
            String xTime = expTime.substring(0, expTime.length() - 1);
            int intTime = Integer.parseInt(xTime);
            if (expTime.contains("s") || intTime < 5) {
                viewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.red_color));
            } else if (intTime < 7) {
                viewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.yellow_color));
            } else {
                viewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.green_color));
            }

        }
        viewHolder.timeElapasedTextView.setText(DateTimeUtil.getElapseTimeInMinutes(post.getDatestamp()));

        if (post.getLiked() == true) {
            viewHolder.likeImage.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.likeselected));
            //
        } else {
            viewHolder.likeImage.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.like));
        }
        viewHolder.commentImage.setClickable(false);// = false;
        if (post.getCommented()) {
            viewHolder.commentImage.setImageResource(R.drawable.commentselected);
        } else {
            viewHolder.commentImage.setImageResource(R.drawable.comment);
        }
        if (post.getIsReposted()) {
            viewHolder.repostIcon.setImageResource(R.drawable.repost_selected);
        } else {
            viewHolder.repostIcon.setImageResource(R.drawable.repost);
        }

        String commentCount = String.valueOf(post.getCommentCount());
        String repostCount = String.valueOf(post.getRepostCount());
        if (repostCount.equals("0"))
            repostCount = "";
        String likeCount = String.valueOf(post.getLikeCount());
        if (likeCount.equals("0"))
            likeCount = "";

        if (commentCount.equals("0")) {
            viewHolder.commentCount.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.commentCount.setText(commentCount);
            viewHolder.commentCount.setVisibility(View.VISIBLE);
        }
        viewHolder.repostCount.setText(repostCount);
        viewHolder.likeCount.setText(likeCount);
        viewHolder.postImage.setImageBitmap(null);
        viewHolder.postImage.setImageDrawable(null);

        if (post.getPostTypeImage() == true) {
            if (post.getHasImage()) {
                viewHolder.playBtn.setVisibility(View.INVISIBLE);//Visibility = ViewStates.Invisible;
                viewHolder.progressBar.setVisibility(View.VISIBLE);//Visibility = ViewStates.Visible;
                viewHolder.postImage.setVisibility(View.VISIBLE);//Visibility = ViewStates.Visible;
                //viewHolder.postLayout.setVisibility(View.VISIBLE);
                //viewHolder.postImage.BindDataToView(post, true, ProgressBar);
                Glide.with(baseActivity)
                        .load(HttpUtils.getPostImageURL(post.getIdPost()))
                        .bitmapTransform(new RoundedCornersTransformation(baseActivity, 5, 5))
                        .into(viewHolder.postImage);
            } else {
                viewHolder.playBtn.setVisibility(View.GONE);//Visibility = ViewStates.Invisible;
                viewHolder.progressBar.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                viewHolder.postImage.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                //viewHolder.postLayout.setVisibility(View.GONE);
            }
        } else if (post.getVideoUrl() == null || post.getVideoUrl().equals("") || post.equals("null")) {
            viewHolder.playBtn.setVisibility(View.GONE);//Visibility = ViewStates.Invisible;
            viewHolder.progressBar.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
            viewHolder.postImage.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
            //viewHolder.postLayout.setVisibility(View.GONE);
        } else {
            viewHolder.progressBar.setVisibility(View.VISIBLE);//Visibility = ViewStates.Visible;
            viewHolder.postImage.setVisibility(View.VISIBLE);//Visibility = ViewStates.Visible;
            //viewHolder.postLayout.setVisibility(View.VISIBLE);
            //viewHolder.postImage.BindDataToView(post, true, ProgressBar);
            Glide.with(baseActivity)
                    .load(HttpUtils.getPostImageURL(post.getIdPost()))
                    .bitmapTransform(new RoundedCornersTransformation(baseActivity, 5, 5))
                    .into(viewHolder.postImage);
            viewHolder.playBtn.setVisibility(View.VISIBLE);//Visibility = ViewStates.Visible;
            //viewHolder.videoRelative.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
            viewHolder.postVideoView.setVisibility(View.GONE);
        }

        try {
            if (currentVideoView != null)//&& currentVideoView.IsPlaying
            {
                currentVideoView.stopPlayback();
                currentVideoView.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                currentVideoView = null;
            }
        } catch (Exception ex) {
            Log.d("--", "-ex-" + ex);
        }
        if (position == postsList.size() - 1 && baseFragment != null) {
            //baseFragment.loadPaginationData(mList.size());
            if (baseFragment instanceof NewProfileFragment) {
                NewProfileFragment NewProfileFragment = (NewProfileFragment) baseFragment;
               /* int lastItem = mLinearManager.findLastVisibleItemPosition();
                if (lastItem == mList.size() - 1) {*/
                NewProfileFragment.loadPaginationData(postsList.size());
                //}
            } else if (baseFragment instanceof PostListFragment) {
                PostListFragment postListFragment = (PostListFragment) baseFragment;
                postListFragment.loadPaginationData(postsList.size());
            }
        }
    }


    @Override
    public int getItemCount() {
        // Add two more counts to accomodate header and footer
        if (postsList == null || postsList.size() == 0) {
            return 1;
        } else {
            return (postsList.size() + 1);
        }
    }

    @Override
    public void getCounts(int pos, PostCountResponse postCountResponse) {
        postsList.get(pos).setCommentCount(postCountResponse.getPost().getCommentCount());
        postsList.get(pos).setLikeCount(postCountResponse.getPost().getLikeCount());
        postsList.get(pos).setRepostCount(postCountResponse.getPost().getRepostCount());
        if (baseFragment == null) {
            baseActivity.notifyList(pos);
        } else {
            baseFragment.notifyList(pos);
        }
        //notifyDataSetChanged();
        notifyItemChanged(pos);
    }

    @Override
    public void countsError(String error) {

    }

    public void updateList(LinearLayoutManager manager) {
        mLinearManager = manager;
        try {
            int firstItemPos = manager.findFirstVisibleItemPosition();
            int lastItemPos = manager.findLastVisibleItemPosition();
            while (firstItemPos <= lastItemPos) {
                View view = manager.findViewByPosition(firstItemPos);
                ItemViewHolder holder = (ItemViewHolder) view.getTag();
                updateTimeData(holder, firstItemPos);
                firstItemPos++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateProfile(ProfileResponse.Profile profileData) {
        this.profileData = profileData;
        getData();
        notifyItemChanged(0);
    }

    // The ViewHolders for Header, Item and Footer
    class HeaderViewHolder extends RecyclerView.ViewHolder implements android.view.View.OnClickListener, android.view.View.OnLongClickListener {
        //private final RecyclerView topTimersRV;
        private final LinearLayoutManager mLayoutManager;
        private final TextView editProfilText;
        private final ImageView userImage;
        private final TextView topTimeText, postsCountText, followersText, followingText;
        public final View View;
        private TextView text_view_user_blocked, tv_followers, tv_following;
        private ProgressBar topLikerProgress;

        //private

        public HeaderViewHolder(View itemView) {
            super(itemView);
            View = itemView;
            text_view_user_blocked = (TextView) itemView.findViewById(R.id.text_view_user_blocked);
            userImage = (ImageView) itemView.findViewById(R.id.image_view_profile);
            userImage.setOnLongClickListener(this);
            topTimeText = (TextView) itemView.findViewById(R.id.text_view_time);
            postsCountText = (TextView) itemView.findViewById(R.id.text_view_post);
            followersText = (TextView) itemView.findViewById(R.id.text_view_followers);
            followingText = (TextView) itemView.findViewById(R.id.text_view_following);
            editProfilText = (TextView) itemView.findViewById(R.id.text_view_edit_profile);
            mLayoutManager = new LinearLayoutManager(mContext);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            tv_followers = (TextView) itemView.findViewById(R.id.tv_followers);
            tv_following = (TextView) itemView.findViewById(R.id.tv_following);
            tv_followers.setOnClickListener(this);
            tv_following.setOnClickListener(this);
            editProfilText.setOnClickListener(this);
            followersText.setOnClickListener(this);
            followingText.setOnClickListener(this);
            itemView.findViewById(R.id.tv_followers).setOnClickListener(this);
            itemView.findViewById(R.id.tv_following).setOnClickListener(this);
            userImage.setOnLongClickListener(this);
//            sv_2.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    v.getParent().requestDisallowInterceptTouchEvent(true);
//                    return false;
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.text_view_edit_profile:
                    if (mBlocked.contains("false")) {
                        if (TextUtils.isEmpty(mScreen)) {
                            if (friendId.contains(mContext.getResources().getString(R.string.friended_friended))) {
                                unFollowResponse(mUserId);
                            } else if (friendId.contains(mContext.getResources().getString(R.string.friended_pending))) {
                                unFollowResponse(mUserId);
                            } else if (friendId.contains(mContext.getResources().getString(R.string.friended_rejected)) ||
                                    friendId.contains(mContext.getResources().getString(R.string.friended_none))
                                    ) {
                                followResponse(mUserId);
                            }
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("username", mUsername);
                            bundle.putString("nick_name", mNickName);
                            bundle.putString("description", mDescription);
                            bundle.putString("isnotify", mNotify);
                            bundle.putString("isPrivate", mIsPrivate);
                            if (baseFragment == null) {
                                //there is no option for edit in activity screen
                            } else {
                                //baseFragment.replaceChildFragment(frameLayout, new EditProfileFragment(), bundle);
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                baseFragment.startActivityForResult(intent, ChooserType.REQUEST_PICK_PICTURE);
                            }
                        }
                    }
                    break;
                case R.id.text_view_followers:

                    if (baseFragment == null) {
                        Intent followersIntent = new Intent(mContext, FollowersActivity.class);
                        followersIntent.putExtra("followers", "2");
                        followersIntent.putExtra("user_id", mUserId);
                        followersIntent.putExtra("name", mUsername);
                        mContext.startActivity(followersIntent);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("followers", "2");
                        if (mUserId.equals(localUserId) && mUsername.equals(userName)) {
                            bundle.putString("user_id", localUserId);
                            bundle.putString("name", userName);
                        } else {
                            bundle.putString("user_id", mUserId);
                            bundle.putString("name", mUsername);
                        }
                        baseFragment.replaceChildFragment(frameLayout, new FollowersFragment(), bundle);
                    }
                    break;
                case R.id.tv_followers:

                    if (baseFragment == null) {
                        Intent followersIntent = new Intent(mContext, FollowersActivity.class);
                        followersIntent.putExtra("followers", "2");
                        followersIntent.putExtra("user_id", mUserId);
                        followersIntent.putExtra("name", mUsername);
                        mContext.startActivity(followersIntent);
                    } else {
                        Bundle bun = new Bundle();
                        bun.putString("followers", "2");
                        if (mUserId.equals(localUserId) && mUsername.equals(userName)) {
                            bun.putString("user_id", localUserId);
                            bun.putString("name", userName);
                        } else {
                            bun.putString("user_id", mUserId);
                            bun.putString("name", mUsername);
                        }
                        baseFragment.replaceChildFragment(frameLayout, new FollowersFragment(), bun);
                    }
                    break;
                case R.id.text_view_following:

                    if (baseFragment == null) {
                        Intent followersIntent = new Intent(mContext, FollowersActivity.class);
                        followersIntent.putExtra("followers", "1");
                        followersIntent.putExtra("user_id", mUserId);
                        followersIntent.putExtra("name", mUsername);
                        mContext.startActivity(followersIntent);
                    } else {
                        Bundle bundle1 = new Bundle();
                        bundle1.putString("followers", "1");
                        if (mUserId.equals(localUserId) && mUsername.equals(userName)) {
                            bundle1.putString("user_id", localUserId);
                            bundle1.putString("name", userName);
                        } else {
                            bundle1.putString("user_id", mUserId);
                            bundle1.putString("name", mUsername);
                        }
                        baseFragment.replaceChildFragment(frameLayout, new FollowersFragment(), bundle1);
                    }
                    break;
                case R.id.tv_following:

                    if (baseFragment == null) {
                        Intent followersIntent = new Intent(mContext, FollowersActivity.class);
                        followersIntent.putExtra("followers", "1");
                        followersIntent.putExtra("user_id", mUserId);
                        followersIntent.putExtra("name", mUsername);
                        mContext.startActivity(followersIntent);
                    } else {
                        Bundle bundle3 = new Bundle();
                        bundle3.putString("followers", "1");
                        if (mUserId.equals(localUserId) && mUsername.equals(userName)) {
                            bundle3.putString("user_id", localUserId);
                            bundle3.putString("name", userName);
                        } else {
                            bundle3.putString("user_id", mUserId);
                            bundle3.putString("name", mUsername);
                        }
                        baseFragment.replaceChildFragment(frameLayout, new FollowersFragment(), bundle3);
                    }
                    break;

            }
        }

        private void unFollowResponse(String idUser) {
            Call<UnFollowResponse> response = GetRestAdapter.getRestAdapter(true).unfollowUser(idUser);
            response.enqueue(new Callback<UnFollowResponse>() {
                @Override
                public void onResponse(Call<UnFollowResponse> call,
                                       retrofit2.Response<UnFollowResponse> response) {
                    if (response != null && response.body() != null) {
                        if (response.body().getSuccess().contains("true")) {
                            friendId = mContext.getResources().getString(R.string.friended_none);
                            setFriended(friendId, editProfilText);
                            if (newProfileFragment == null) {
                                newProfileActivity.getProfileApi();
                            } else {
                                newProfileFragment.getProfileApi();
                            }
                        }
                    } else {
                        baseActivity.showToast(R.string.server_error_msg);
                    }
                }

                @Override
                public void onFailure(Call<UnFollowResponse> call, Throwable t) {
                    baseActivity.showToast(t.getMessage());
                }
            });
        }

        private void followResponse(String idUser) {
            Call<UnFollowResponse> response = GetRestAdapter.getRestAdapter(true).followUser(idUser);
            response.enqueue(new Callback<UnFollowResponse>() {
                @Override
                public void onResponse(Call<UnFollowResponse> call,
                                       retrofit2.Response<UnFollowResponse> response) {
                    if (response != null && response.body() != null) {
                        if (response.body().getSuccess().contains("true")) {
                            if (TextUtils.isEmpty(mIsPrivate) || mIsPrivate.equals("false")) {
                                friendId = mContext.getResources().getString(R.string.friended_friended);
                            } else {
                                friendId = mContext.getResources().getString(R.string.friended_friended);
                            }
                            setFriended(friendId, editProfilText);
                            if (newProfileFragment == null) {
                                newProfileActivity.getProfileApi();
                            } else {
                                newProfileFragment.getProfileApi();
                            }

                        } else {
                            baseActivity.showToast(response.body().getSuccess());
                        }
                    } else {
                        baseActivity.showToast(R.string.server_error_msg);
                    }
                }

                @Override
                public void onFailure(Call<UnFollowResponse> call, Throwable t) {

                    baseActivity.showToast(t.getMessage());

                }
            });
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.image_view_profile:
                    int pos = getLayoutPosition();
                    NewsFeedOutput.Posts post = postsList.get(pos);
                    if (post.getPostTypeImage() == true) {
                        if (post.getHasImage()) {
                            Intent intent = new Intent(mContext, ZoomImageActivity.class);
                            if (userName.equals(null) || userName.equals("")) {
                                intent.putExtra("username", mUsername);
                            } else {
                                intent.putExtra("username", userName);
                            }
                            mContext.startActivity(intent);
                        }
                    }
                    break;

            }
            return false;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            MediaPlayer.OnPreparedListener {
        //public View View;
        private TextView mediaCaption;
        private TextView likeCount, repostCount, commentCount;
        private TextView usernameTextView, timeRemainingTextView, timeElapasedTextView, repostedTextView, captionTextView;
        private TextView mUserNameTextView;
        private ImageView clockImageView, likeImage, repostIcon, commentImage, playBtn;
        private ImageView postImage, userImage;
        private ProgressBar progressBar;
        private VideoView postVideoView;
        private RelativeLayout /*shareDataLayout,*/ mainRelate, postLayout;
        //private TextView usersCount, postText;

        public ItemViewHolder(View convertView) {
            super(convertView);
            //usersCount = (TextView) convertView.findViewById(R.id.usersCount);
            //postText = (TextView) convertView.findViewById(R.id.post_text);
            //shareDataLayout = (RelativeLayout) convertView.findViewById(R.id.userList_layout);
            mUserNameTextView = (TextView) convertView.findViewById(R.id.username);
            playBtn = (ImageView) convertView.findViewById(R.id.playBtn1);
            playBtn.setTag(getLayoutPosition());
            playBtn.setVisibility(View.GONE);
            progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            postLayout = (RelativeLayout) convertView.findViewById(R.id.post_layout);
            postImage = (ImageView) convertView.findViewById(R.id.postimage);
            postVideoView = (VideoView) convertView.findViewById(R.id.postvideo);
            userImage = (ImageView) convertView.findViewById(R.id.userimage);
            usernameTextView = (TextView) convertView.findViewById(R.id.username);
            captionTextView = (TextView) convertView.findViewById(R.id.caption);
            mediaCaption = (TextView) convertView.findViewById(R.id.media_caption);
            timeRemainingTextView = (TextView) convertView.findViewById(R.id.timeremaining);
            timeElapasedTextView = (TextView) convertView.findViewById(R.id.timeelapsed);
            clockImageView = (ImageView) convertView.findViewById(R.id.clock);
            likeImage = (ImageView) convertView.findViewById(R.id.like);
            likeCount = (TextView) convertView.findViewById(R.id.like_count);
            repostCount = (TextView) convertView.findViewById(R.id.repost_count);
            commentCount = (TextView) convertView.findViewById(R.id.comment_count);
            repostIcon = (ImageView) convertView.findViewById(R.id.repost);
            commentImage = (ImageView) convertView.findViewById(R.id.comment);
            mainRelate = (RelativeLayout) convertView.findViewById(R.id.relate_main);
            mainRelate.setOnClickListener(this);
            playBtn.setOnClickListener(this);
            likeImage.setOnClickListener(this);
            captionTextView.setOnClickListener(this);
            usernameTextView.setOnClickListener(this);
            timeRemainingTextView.setOnClickListener(this);
            clockImageView.setOnClickListener(this);
            repostIcon.setOnClickListener(this);
            postImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(baseActivity, ZoomImageActivity.class);
                    int position = getLayoutPosition() - 1;
                    intent.putExtra("image", HttpUtils.getPostImageURL(postsList.get(position).getIdPost()));
                    baseActivity.startActivity(intent);
                    return false;
                }
            });

            postImage.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int position = getLayoutPosition() - 1;
                    moveToPostDetail(position);

                }

                @Override
                public void onDoubleClick(View v) {
                    int position = getLayoutPosition() - 1;
                    if (!localUserId.equals(postsList.get(position).getUserPoster().getIdUser())) {
                        if (!Utilities.checkInternet(baseActivity)) {
                            baseActivity.showToast(R.string.no_internet_msg);
                        } else {
                            //await TenServiceHelper.LikePost(post, likeImage);
                            if (!postsList.get(position).getLiked()) {
                                likePost(position, postsList.get(position).getIdPost());
                            }
                        }
                    }
                }
            });
            mainRelate.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int position = getLayoutPosition() - 1;
                    if (position < 0) {
                        position = 0;
                    }
                    moveToPostDetail(position);
                }

                @Override
                public void onDoubleClick(View v) {
                    //baseActivity.showToast("double");
                    int position = getLayoutPosition() - 1;
                    if (position < 0) {
                        position = 0;
                    }
                    if (!localUserId.equals(postsList.get(position).getUserPoster().getIdUser())) {
                        if (!Utilities.checkInternet(baseActivity)) {
                            baseActivity.showToast(R.string.no_internet_msg);
                        } else {
                            //await TenServiceHelper.LikePost(post, likeImage);
                            if (!postsList.get(position).getLiked()) {
                                likePost(position, postsList.get(position).getIdPost());
                            }
                        }
                    }
                }
            });
        }

        private void moveToPostDetail(int position) {
            if (baseFragment == null) {
                Intent intent;
                intent = new Intent(baseActivity, PostDetailActivity.class);
                intent.putParcelableArrayListExtra(PreferenceKeys.PREF_HOME_PAGE_DATA, postsList);
                intent.putExtra("position", position);
                baseActivity.startActivity(intent);
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, postsList);
                bundle.putInt("position", position);
                baseFragment.replaceChildFragment(frameLayout, new PostDetailFragment(),
                        bundle);
            }
        }


        @Override
        public void onClick(View v) {
            if (Utilities.checkInternet(baseActivity)) {
                final int position = getLayoutPosition() - 1;
                final NewsFeedOutput.Posts post = postsList.get(position);
                switch (v.getId()) {
                    case R.id.playBtn1:
                        playVideo(postsList.get(position), postVideoView);
                        break;
                    case R.id.username:
                        if (post.getUserPoster().getIdUser().equals(localUserId)) {
                            return;
                        }
                        if (baseFragment == null) {
                            Intent intent = new Intent(baseActivity, NewProfileActivity.class);
                            intent.putExtra("username", post.getUserPoster().getUsername());
                            baseActivity.startActivity(intent);
                        } else {
                            gotoProfile(post);
                        }
                        break;
                    case R.id.timeremaining:
                        if (baseFragment == null) {
                            Intent intent = new Intent(baseActivity, TimersActivity.class);
                            intent.putExtra("postId", postsList.get(position).getIdPost());
                            baseActivity.startActivity(intent);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("postId", postsList.get(position).getIdPost());
                            baseFragment.replaceChildFragment(frameLayout, new TimersFragment(), bundle);
                        }
                        break;

                    case R.id.clock:
                        if (baseFragment == null) {
                            Intent intent = new Intent(baseActivity, TimersActivity.class);
                            intent.putExtra("postId", postsList.get(position).getIdPost());
                            baseActivity.startActivity(intent);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("postId", postsList.get(position).getIdPost());
                            baseFragment.replaceChildFragment(frameLayout, new TimersFragment(), bundle);
                        }
                        break;
                    case R.id.repostlabel:
                        if (post.getUserReposter().getIdUser().equals(localUserId)) {
                            return;
                        }
                        if (baseFragment == null) {
                            Intent intent = new Intent(baseActivity, NewProfileActivity.class);
                            intent.putExtra("username", post.getUserPoster().getUsername());
                            baseActivity.startActivity(intent);
                        } else {
                            gotoProfile(post);
                        }

                        break;
                    case R.id.repost:
                        if (post.getUserPoster().getIdUser().equals(localUserId)) {
                            Utilities.showlongToast(baseActivity, "You can't re-post your own post.");
                            return;
                        }
                        if (post.getIsReposted()) {
                            Utilities.showlongToast(baseActivity, "You already re-post this post.");
                            return;
                        }
                        new AlertDialog.Builder(baseActivity).setTitle("Repost?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    if (post.getUserPoster().getIdUser().equals(localUserId)) {
                                        Utilities.showlongToast(baseActivity, "You can't re-post your own post.");
                                        return;
                                    }
                                    if (post.getIsReposted()) {
                                        Utilities.showlongToast(baseActivity, "You already re-post this post.");
                                        return;
                                    }
                                    repostIcon.setImageResource(R.drawable.refresh);
                                    rePost(post, position);
                                } catch (Exception eee) {
                                    eee.printStackTrace();
                                } finally {
                                    if (!post.getIsReposted()) {
                                        repostIcon.setImageResource(R.drawable.repost);
                                    }
                                }
                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                        break;
                    case R.id.like:
                        if (post.getUserPoster().getIdUser().equals(localUserId)) {
                            return;
                        }
                        if (post.getLiked()) {
                            baseActivity.showToast("You already liked this post.");
                            return;
                        }

                        if (!Utilities.checkInternet(baseActivity)) {
                            baseActivity.showToast(R.string.no_internet_msg);
                        } else {
                            //await TenServiceHelper.LikePost(post, likeImage);
                            if (postsList.get(position).getLiked() == true) {

                            } else {
                                if (!Utilities.checkInternet(baseActivity)) {
                                    baseActivity.showToast(R.string.no_internet_msg);
                                } else {
                                    //await TenServiceHelper.LikePost(post, likeImage);
                                    likePost(position, postsList.get(position).getIdPost());
                                }
                            }
                        }
                        break;
                    case R.id.relate_main:
                        moveToPostDetail(position);

                        break;
                }
            } else {
                Utilities.showMessage(baseActivity, baseActivity.getString(R.string.no_internet_msg));
            }
        }

        private void rePost(NewsFeedOutput.Posts post, final int position) {
            Call<RepostPostResponse> response = GetRestAdapter.getRestAdapter(true).repost(post.getIdPost());
            response.enqueue(new Callback<RepostPostResponse>() {
                @Override
                public void onResponse(Call<RepostPostResponse> call,
                                       retrofit2.Response<RepostPostResponse> response) {
                    if (response != null && response.body() != null) {
                        if (response.body().getSuccess().contains("true")) {
                            postsList.get(position).setIsReposted(true);
                        }
                    }
                    if (baseFragment == null) {
                        baseActivity.reLoadList();
                    } else {
                        baseFragment.reLoadList();
                    }
                }

                @Override
                public void onFailure(Call<RepostPostResponse> call, Throwable t) {
                    //showToast(t.getMessage());
                }
            });
        }

        private void gotoProfile(NewsFeedOutput.Posts post) {
            Bundle bundle = new Bundle();
            bundle.putString("username", post.getUserPoster().getUsername());
            baseFragment.replaceChildFragment(frameLayout, new NewProfileFragment(),
                    bundle);
        }

        private Bundle postSelectedBundle(final int position) {
            Bundle bundle = new Bundle();
            bundle.putString("username", postsList.get(position).getUserPoster().getUsername());
            bundle.putString("postCaption", postsList.get(position).getText());
            bundle.putString("localUserId", postsList.get(position).getUserPoster().getIdUser());
            bundle.putString("postId", postsList.get(position).getIdPost());
            bundle.putString("likedValue", postsList.get(position).getUserPoster().getIdUser());
            bundle.putString("remainingTime", postsList.get(position).getDatestamp());
            bundle.putString("expirationTime", postsList.get(position).getExpiration());
            bundle.putParcelable("post_data", postsList.get(position));
            if (frameLayout == R.id.popularLayout) {
                bundle.putString("Back", "Popular");
            }
            return bundle;
        }

        private void playVideo(NewsFeedOutput.Posts post, VideoView videoView) {
            try {
                //"https://s3.amazonaws.com/ten-s3-local/images/271/temp_1498227024860.mp4";//
                //https://s3.amazonaws.com/ten-s3-local/images/271/10627-1498454387192.mp4
                String url = post.getVideoUrl();
                // String url = "https://s3.amazonaws.com/ten-s3-local/images/271/10627-1498454387192.mp4";
                Log.d("", "" + url);
                if (currentVideoView != null)//&& currentVideoView.IsPlaying
                {
                    currentVideoView.stopPlayback();
                    currentVideoView.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                    currentVideoView = null;
                }
                //videoRelative.setVisibility(View.VISIBLE);//;Visibility = ViewStates.Visible;
                postVideoView.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.GONE);
                currentVideoView = videoView;
                //currentVideoView.setOnPreparedListener(this);
                currentVideoView.setVideoURI(Uri.parse(url));
                //currentVideoView.setVideoPath(url);
                currentVideoView.requestFocus();
                currentVideoView.start();
                currentVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        currentVideoView.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                        currentVideoView = null;
                        notifyDataSetChanged();
                    }
                });
            } catch (Exception exp) {
                Log.d("", "" + exp);
            }
        }

        @Override
        public void onPrepared(MediaPlayer mp) {

        }

        private void likePost(final int position, String idPost) {
            Call<UnFollowResponse> response = GetRestAdapter.getRestAdapter(true).likePost(idPost);
            response.enqueue(new Callback<UnFollowResponse>() {
                @Override
                public void onResponse(Call<UnFollowResponse> call,
                                       retrofit2.Response<UnFollowResponse> response) {
                    if (response != null && response.body() != null) {
                        if (response.body().getSuccess().contains("true")) {
                            int oldCount = postsList.get(position).getLikeCount();
                            oldCount++;
                            postsList.get(position).setLiked(true);
                            postsList.get(position).setLikeCount(oldCount);
                            //notifyItemChanged(position);
                            MediaPlayer mp = MediaPlayer.create(baseActivity, R.raw.ticksound);
                            mp.start();
                            View view = mLinearManager.findViewByPosition(position);
                            ItemViewHolder holder = (ItemViewHolder) view.getTag();
                            holder.likeCount.setText(oldCount + "");
                            holder.likeImage.setImageDrawable(ContextCompat.getDrawable(baseActivity, R.drawable.likeselected));

                        } else {
                            baseActivity.showToast(response.message());
                        }
                    }
                    if (baseFragment == null) {
                        LikeCommentsCountsPresenter likeCommentsCountsPresenter = new LikeCommentsCountsPresenter(baseActivity, UserProfileAdapter.this);
                        likeCommentsCountsPresenter.countsApi(position, postsList.get(position).getIdPost());
                    } else {
                        LikeCommentsCountsPresenter likeCommentsCountsPresenter = new LikeCommentsCountsPresenter(baseFragment, UserProfileAdapter.this);
                        likeCommentsCountsPresenter.countsApi(position, postsList.get(position).getIdPost());
                    }
                }

                @Override
                public void onFailure(Call<UnFollowResponse> call, Throwable t) {
                    baseActivity.showToast(t.getMessage());
                }
            });
        }

    }

}

