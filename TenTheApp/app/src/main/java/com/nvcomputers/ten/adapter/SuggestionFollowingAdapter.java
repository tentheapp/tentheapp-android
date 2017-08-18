package com.nvcomputers.ten.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.model.output.SearchFollowingUserResponse;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.home.PostDetailActivity;
import com.nvcomputers.ten.views.home.PostDetailPagerItem;
import com.nvcomputers.ten.views.post.AddPostFragment;

import java.util.List;
import java.util.StringTokenizer;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Thaparsneh on 5/23/2017.
 */

public class SuggestionFollowingAdapter extends RecyclerView.Adapter<SuggestionFollowingAdapter.SuggestionFollowingViewHolder> {
    private final Activity mActivity;
    private final List<SearchFollowingUserResponse.Users> followingList;
    private PostDetailActivity postDetailActivity;
    private BaseFragment baseFragment;
    private final SharedPrefsHelper sharedPref;
    private int listCount;

    public SuggestionFollowingAdapter(BaseFragment baseFragment,
                                      List<SearchFollowingUserResponse.Users> arrayList) {
        this.mActivity = baseFragment.getBaseActivity();
        this.baseFragment = baseFragment;
        this.followingList = arrayList;
        if (followingList != null) {
            listCount = followingList.size();
        }
        sharedPref = new SharedPrefsHelper(baseFragment.getBaseActivity());
    }

   /* public SuggestionFollowingAdapter(PostDetailActivity postDetailActivity,
                                      List<SearchFollowingUserResponse.Users> arrayList) {
        this.mActivity = postDetailActivity;
        this.postDetailActivity = postDetailActivity;
        //this.baseFragment = baseFragment;
        this.followingList = arrayList;
        if (followingList != null) {
            listCount = followingList.size();
        }
        sharedPref = new SharedPrefsHelper(postDetailActivity);
    }*/

    @Override
    public SuggestionFollowingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.row_suggestion_following, parent, false);
        return new SuggestionFollowingViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(SuggestionFollowingViewHolder holder, int position) {
        SearchFollowingUserResponse.Users data = followingList.get(position);
        String user_name = data.getUsername();
        holder.userName.setText(user_name);

        String imageTag = sharedPref.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(mActivity)
                .load(HttpUtils.getProfileImageURL(data.getUsername()))
                .override(90, 90)
                .bitmapTransform(new RoundedCornersTransformation(mActivity, 15, 15))
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return followingList == null ? 0 : followingList.size();
    }

    public class SuggestionFollowingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView userName;
        private final ImageView userImage;

        public SuggestionFollowingViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.followingName);
            userImage = (ImageView) itemView.findViewById(R.id.followingImg);
            itemView.findViewById(R.id.inner_layout).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getLayoutPosition();
            String comment = "";
            if (view.getId() == R.id.inner_layout) {
                String selectedUserName = followingList.get(clickedPosition).getUsername();
                if (baseFragment != null && baseFragment instanceof PostDetailPagerItem) {
                    PostDetailPagerItem pagerItem = (PostDetailPagerItem) baseFragment;
                    comment = pagerItem.edit_text_comment.getText().toString().trim();
                    if (comment != null && comment.length() > 0) {
                        StringTokenizer st = new StringTokenizer(comment, "@");
                        String lastToken = null;
                        while (st.hasMoreTokens()) {
                            lastToken = st.nextToken();
                        }
                        //comment = comment.replace(lastToken,"");
                        if (!TextUtils.isEmpty(lastToken)) {
                            comment = comment.substring(0, (comment.length() - lastToken.length()));
                            pagerItem.edit_text_comment.setText(comment + selectedUserName + " ");
                            pagerItem.edit_text_comment.setSelection(pagerItem.edit_text_comment.getText().length());
                        }
                        pagerItem.suggestionRV.setVisibility(View.GONE);
                    }
                } else if (baseFragment != null && baseFragment instanceof AddPostFragment) {

                    AddPostFragment addPostFragment = (AddPostFragment) baseFragment;
                    comment = addPostFragment.edit_text_comment.getText().toString().trim();
                    if (comment != null && comment.length() > 0) {
                        StringTokenizer st = new StringTokenizer(comment, "@");
                        String lastToken = null;
                        while (st.hasMoreTokens()) {
                            lastToken = st.nextToken();
                        }
                        //comment = comment.replace(lastToken,"");
                        if (!TextUtils.isEmpty(lastToken)) {
                            comment = comment.substring(0, (comment.length() - lastToken.length()));
                            addPostFragment.edit_text_comment.setText(comment + selectedUserName + " ");
                            addPostFragment.edit_text_comment.setSelection(addPostFragment.edit_text_comment.getText().length());
                        }
                        addPostFragment.suggestionRV.setVisibility(View.GONE);
                    }
                    /*comment = postDetailActivity.edit_text_comment.getText().toString().trim();
                    if (comment != null && comment.length() > 0) {
                        StringTokenizer st = new StringTokenizer(comment, "@");
                        String lastToken = null;
                        while (st.hasMoreTokens()) {
                            lastToken = st.nextToken();
                        }
                        //comment = comment.replace(lastToken,"");
                        comment = comment.substring(0, (comment.length() - lastToken.length()));
                        postDetailActivity.edit_text_comment.setText(comment + selectedUserName + " ");
                        postDetailActivity.edit_text_comment.setSelection(postDetailActivity.edit_text_comment.getText().length());
                        postDetailActivity.mSuggestionDialog.dismiss();
                    }*/
                }
               /* String commented = comment +selectedUserName;
                //String commented = comment.replace("@", " " + name);
                baseFragment.edit_text_comment.setText(commented);

                if (comment.length() > 1) {
                    int index = comment.indexOf("@");
                    StringTokenizer stringTokenizer = new StringTokenizer(comment, "@");
                    if (index == 0) {
                        if (stringTokenizer.hasMoreTokens()) {
                            //String first = stringTokenizer.nextToken();
                            baseFragment.edit_text_comment.setText("@"+selectedUserName+" ");
                            baseFragment.mSuggestionDialog.dismiss();
                            baseFragment.edit_text_comment.setSelection(baseFragment.edit_text_comment.getText().length());
                        }
                    } else {
                        String first = null;
                        if (stringTokenizer.hasMoreTokens()) {
                            first = stringTokenizer.nextToken();
                        }
                        if (stringTokenizer.hasMoreTokens()) {
                            String last = stringTokenizer.nextToken();
                            baseFragment.edit_text_comment.setText(first + "@" + selectedUserName+" ");
                            baseFragment.mSuggestionDialog.dismiss();
                            baseFragment.edit_text_comment.setSelection(baseFragment.edit_text_comment.getText().length());
                        }
                    }
                }*/
            }
        }
    }
}