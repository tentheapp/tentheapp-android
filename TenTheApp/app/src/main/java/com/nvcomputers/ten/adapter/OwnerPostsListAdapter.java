package com.nvcomputers.ten.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextPaint;
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
import com.nvcomputers.ten.interfaces.DoubleClickListener;
import com.nvcomputers.ten.model.output.NewsFeedOutput;
import com.nvcomputers.ten.model.output.PostCountResponse;
import com.nvcomputers.ten.model.output.RepostPostResponse;
import com.nvcomputers.ten.model.output.UnFollowResponse;
import com.nvcomputers.ten.presenter.LikeCommentsCountsPresenter;
import com.nvcomputers.ten.utils.CustomImageLoader;
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
import com.nvcomputers.ten.views.profile.NewProfileActivity;
import com.nvcomputers.ten.views.profile.NewProfileFragment;
import com.nvcomputers.ten.views.profile.ZoomImageActivity;
import com.nvcomputers.ten.views.search.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by Thaparsneh on 7/6/2017.
 */

public class OwnerPostsListAdapter extends RecyclerView.Adapter<OwnerPostsListAdapter.ViewHolder> implements
        LikeCommentsCountsPresenter.LikeCommentsCallback {
    private String userId, userName;
    private CustomImageLoader imageLoader;
    private LinearLayoutManager mLinearManager;
    private String followingUser;
    private int frameLayout;
    private BaseActivity baseActivity;
    private ArrayList<NewsFeedOutput.Posts> mList;
    private BaseFragment baseFragment;
    private SharedPrefsHelper sharedPrefsHelper;
    private VideoView currentVideoView;
    private boolean isOnline;

    public OwnerPostsListAdapter(BaseActivity context, ArrayList<?> mList, LinearLayoutManager manager) {
        this.mList = (ArrayList<NewsFeedOutput.Posts>) mList;
        this.baseActivity = context;
        mLinearManager = manager;
        sharedPrefsHelper = new SharedPrefsHelper(this.baseActivity);
        userId = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_ID, "");
        userName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        followingUser = sharedPrefsHelper.get(PreferenceKeys.PREF_FOLLOWING_LIST, "");
    }

    /**
     * @param baseFragment
     * @param mList
     * @param frameLayout
     */
    public OwnerPostsListAdapter(BaseFragment baseFragment, List<?> mList, int frameLayout, LinearLayoutManager manager) {
        this.frameLayout = frameLayout;
        this.mList = (ArrayList<NewsFeedOutput.Posts>) mList;
        this.baseFragment = baseFragment;
        this.baseActivity = baseFragment.getBaseActivity();
        sharedPrefsHelper = new SharedPrefsHelper(this.baseActivity);
        userId = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_ID, "");
        userName = sharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME, "");
        followingUser = sharedPrefsHelper.get(PreferenceKeys.PREF_FOLLOWING_LIST, "");
        mLinearManager = manager;
        imageLoader = new CustomImageLoader(baseFragment.mContext);
        //mPosition = 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_news_feed_layout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        itemLayoutView.setTag(viewHolder);
        return viewHolder;
    }

    public void isOnline(boolean isOnline) {
        this.isOnline = isOnline;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        //set data
        if (mList == null || mList.size() - 1 < position)
            return;
        NewsFeedOutput.Posts post = null;
        try {
            post = mList.get(position);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        String url = HttpUtils.getProfileImageURL(post.getUserPoster().getUsername());
        String imageTag = sharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(baseActivity)
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(baseActivity, 15, 5))
                .error(R.drawable.myprofilelarge)
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(viewHolder.userImage);

        viewHolder.playBtn.setVisibility(View.INVISIBLE);//Visibility = ViewStates.Invisible;
        viewHolder.usernameTextView.setText(post.getUserPoster().getUsername()); //= post.userPoster.username;

        if (post.getUserReposter() != null) {
            viewHolder.repostedTextView.setVisibility(View.VISIBLE);
            if (post.getUserReposter().getIdUser().equals(userId)) {
                viewHolder.repostedTextView.setText("by you");// = "by you";
            } else {
                viewHolder.repostedTextView.setText("by " + post.getUserReposter().getUsername());//Text = "by " + post.userReposter.username;
            }
        } else {
            viewHolder.repostedTextView.setVisibility(View.INVISIBLE);
            viewHolder.repostedTextView.setText("");
        }

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
        if (followingUser != null && followingUser.length() > 0 && post.getText().contains("@")) {

            String[] parts = followingUser.split(",");

            for (int i = 0; i < parts.length; i++) {
                final String taggedName = parts[i];
                if (post.getText().contains(taggedName)) {
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
                            ds.setUnderlineText(true);

                        }
                    };
                    if (taggedName != null && taggedName.length() > 0) {
                        int startIndex = post.getText().indexOf("@" + taggedName);
                        int endIndex = startIndex + taggedName.length() + 1;
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
                                        ds.setUnderlineText(true);

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
            viewHolder.timeRemainingTextView.setText("11m");
            viewHolder.timeRemainingTextView.setBackground(ContextCompat.getDrawable(baseActivity, R.drawable.rounded_btn_black_filled));
            viewHolder.timeRemainingTextView.setTextColor(ContextCompat.getColor(baseActivity, R.color.white));
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
        if (position == mList.size() - 1 && baseFragment != null) {
            //baseFragment.loadPaginationData(mList.size());
            if (baseFragment instanceof NewProfileFragment) {
                NewProfileFragment NewProfileFragment = (NewProfileFragment) baseFragment;
               /* int lastItem = mLinearManager.findLastVisibleItemPosition();
                if (lastItem == mList.size() - 1) {*/
                NewProfileFragment.loadPaginationData(mList.size());
                //}
            } else if (baseFragment instanceof PostListFragment) {
                PostListFragment postListFragment = (PostListFragment) baseFragment;
                postListFragment.loadPaginationData(mList.size());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public ArrayList<NewsFeedOutput.Posts> getPostList() {
        return mList;
    }

    public void updateTimeData(ViewHolder mViewHolder, int mPosition) {
        NewsFeedOutput.Posts post = mList.get(mPosition);
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

    public void updateList(ArrayList<NewsFeedOutput.Posts> postList) {
        try {
            if (baseFragment instanceof PostListFragment && postList != null) {
                for (int i = 0; i < postList.size(); i++) {
                    if (DateTimeUtil.checkExpiration(postList.get(i).getExpiration())) {
                        if (i < mList.size()) {
                            removeItem(i);
                            notifyItemRemoved(i);
                            notifyItemRangeChanged(i, postList.size());
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (postList.size() != mList.size()) {
            mList = postList;
            notifyDataSetChanged();
        } else {
            try {
                int firstItemPos = mLinearManager.findFirstVisibleItemPosition();
                int lastItemPos = mLinearManager.findLastVisibleItemPosition();
                while (firstItemPos <= lastItemPos) {
                    View view = mLinearManager.findViewByPosition(firstItemPos);
                    ViewHolder holder = (ViewHolder) view.getTag();
                    updateTimeData(holder, firstItemPos);
                    firstItemPos++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void getCounts(int pos, PostCountResponse postCountResponse) {
        mList.get(pos).setCommentCount(postCountResponse.getPost().getCommentCount());
        mList.get(pos).setLikeCount(postCountResponse.getPost().getLikeCount());
        mList.get(pos).setRepostCount(postCountResponse.getPost().getRepostCount());
        baseFragment.notifyList(pos);
        //notifyDataSetChanged();
        notifyItemChanged(pos);
    }

    @Override
    public void countsError(String error) {

    }

    public void removeItem(int pos) {
        mList.remove(pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            MediaPlayer.OnPreparedListener {
        private TextView mediaCaption;
        private TextView likeCount, repostCount, commentCount;
        private TextView usernameTextView, timeRemainingTextView, timeElapasedTextView, repostedTextView, captionTextView;
        private TextView mUserNameTextView;
        private ImageView clockImageView, likeImage, repostIcon, commentImage, playBtn;
        private ImageView postImage, userImage;
        private ProgressBar progressBar;
        private VideoView postVideoView;
        private RelativeLayout shareDataLayout, mainRelate, postLayout;
        private TextView usersCount, postText;

        public ViewHolder(View convertView) {
            super(convertView);
            //usersCount = (TextView) convertView.findViewById(R.id.usersCount);
            // postText = (TextView) convertView.findViewById(R.id.post_text);
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
            repostedTextView = (TextView) convertView.findViewById(R.id.repostlabel);
            clockImageView = (ImageView) convertView.findViewById(R.id.clock);
            likeImage = (ImageView) convertView.findViewById(R.id.like);
//            if (mList.get(getLayoutPosition()).getUserPoster().getIdUser().equals(userId)) {
//                likeImage.setAlpha(0.6f);
//            }
            likeCount = (TextView) convertView.findViewById(R.id.like_count);
            repostCount = (TextView) convertView.findViewById(R.id.repost_count);
            commentCount = (TextView) convertView.findViewById(R.id.comment_count);
            repostIcon = (ImageView) convertView.findViewById(R.id.repost);
//            if (mList.get(getLayoutPosition()).getUserPoster().getIdUser().equals(userId)) {
//                repostIcon.setAlpha(0.6f);
//            }
            commentImage = (ImageView) convertView.findViewById(R.id.comment);
            mainRelate = (RelativeLayout) convertView.findViewById(R.id.relate_main);
            mainRelate.setOnClickListener(this);
            playBtn.setOnClickListener(this);
            likeImage.setOnClickListener(this);
            captionTextView.setOnClickListener(this);
            usernameTextView.setOnClickListener(this);
            timeRemainingTextView.setOnClickListener(this);
            clockImageView.setOnClickListener(this);
            repostedTextView.setOnClickListener(this);
            repostIcon.setOnClickListener(this);
            postImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(baseActivity, ZoomImageActivity.class);
                    intent.putExtra("image", HttpUtils.getPostImageURL(mList.get(getLayoutPosition()).getIdPost()));
                    baseActivity.startActivity(intent);
                    return false;
                }
            });

            postImage.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int position = getLayoutPosition();
                    moveToPostDetail(position);

                }

                @Override
                public void onDoubleClick(View v) {
                    int position = getLayoutPosition();
                    if (!userId.equals(mList.get(position).getUserPoster().getIdUser())) {
                        if (!Utilities.checkInternet(baseActivity)) {
                            baseActivity.showToast(R.string.no_internet_msg);
                        } else {
                            //await TenServiceHelper.LikePost(post, likeImage);
                            if (!mList.get(position).getLiked()) {
                                likePost(position, mList.get(position).getIdPost());
                            }
                        }
                    }
                }
            });
            mainRelate.setOnClickListener(new DoubleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int position = getLayoutPosition();
                    if (position < 0) {
                        position = 0;
                    }
                    moveToPostDetail(position);
                }

                @Override
                public void onDoubleClick(View v) {
                    //baseActivity.showToast("double");
                    int position = getLayoutPosition();
                    if (position < 0) {
                        position = 0;
                    }
                    if (!userId.equals(mList.get(position).getUserPoster().getIdUser())) {
                        if (!Utilities.checkInternet(baseActivity)) {
                            baseActivity.showToast(R.string.no_internet_msg);
                        } else {
                            //await TenServiceHelper.LikePost(post, likeImage);
                            if (!mList.get(position).getLiked()) {
                                likePost(position, mList.get(position).getIdPost());
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
                intent.putParcelableArrayListExtra(PreferenceKeys.PREF_HOME_PAGE_DATA, mList);
                intent.putExtra("position", position);
                baseActivity.startActivity(intent);
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(PreferenceKeys.PREF_HOME_PAGE_DATA, mList);
                bundle.putInt("position", position);
                baseFragment.replaceChildFragment(frameLayout, new PostDetailFragment(),
                        bundle);
            }
        }


        @Override
        public void onClick(View v) {
            if (Utilities.checkInternet(baseActivity)) {
                final int position = getLayoutPosition();
                final NewsFeedOutput.Posts post = mList.get(position);
                switch (v.getId()) {
                    case R.id.playBtn1:
                        playVideo(mList.get(position), postVideoView);
                        break;
                    case R.id.username:
                        if (post.getUserPoster().getIdUser().equals(userId)) {
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
                            intent.putExtra("postId", mList.get(position).getIdPost());
                            baseActivity.startActivity(intent);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("postId", mList.get(position).getIdPost());
                            baseFragment.replaceChildFragment(frameLayout, new TimersFragment(), bundle);
                        }
                        break;

                    case R.id.clock:
                        if (baseFragment == null) {
                            Intent intent = new Intent(baseActivity, TimersActivity.class);
                            intent.putExtra("postId", mList.get(position).getIdPost());
                            baseActivity.startActivity(intent);
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString("postId", mList.get(position).getIdPost());
                            baseFragment.replaceChildFragment(frameLayout, new TimersFragment(), bundle);
                        }
                        break;
                    case R.id.repostlabel:
                        if (post.getUserReposter().getIdUser().equals(userId)) {
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
                        if (post.getUserPoster().getIdUser().equals(userId)) {
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
                                    if (post.getUserPoster().getIdUser().equals(userId)) {
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
                        if (post.getUserPoster().getIdUser().equals(userId)) {
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
                            if (mList.get(position).getLiked() == true) {

                            } else {
                                if (!Utilities.checkInternet(baseActivity)) {
                                    baseActivity.showToast(R.string.no_internet_msg);
                                } else {
                                    //await TenServiceHelper.LikePost(post, likeImage);
                                    likePost(position, mList.get(position).getIdPost());
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
                            mList.get(position).setIsReposted(true);
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
            bundle.putString("username", mList.get(position).getUserPoster().getUsername());
            bundle.putString("postCaption", mList.get(position).getText());
            bundle.putString("userId", mList.get(position).getUserPoster().getIdUser());
            bundle.putString("postId", mList.get(position).getIdPost());
            bundle.putString("likedValue", mList.get(position).getUserPoster().getIdUser());
            bundle.putString("remainingTime", mList.get(position).getDatestamp());
            bundle.putString("expirationTime", mList.get(position).getExpiration());
            bundle.putParcelable("post_data", mList.get(position));
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
                            mList.get(position).setLiked(true);
                            notifyItemChanged(position);
                            MediaPlayer mp = MediaPlayer.create(baseActivity, R.raw.ticksound);
                            mp.start();
                        } else {
                            baseActivity.showToast(response.message());
                        }
                    }
                    if (baseFragment == null) {
                        LikeCommentsCountsPresenter likeCommentsCountsPresenter = new LikeCommentsCountsPresenter(baseActivity, OwnerPostsListAdapter.this);
                        likeCommentsCountsPresenter.countsApi(position, mList.get(position).getIdPost());
                    } else {
                        LikeCommentsCountsPresenter likeCommentsCountsPresenter = new LikeCommentsCountsPresenter(baseFragment, OwnerPostsListAdapter.this);
                        likeCommentsCountsPresenter.countsApi(position, mList.get(position).getIdPost());
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
