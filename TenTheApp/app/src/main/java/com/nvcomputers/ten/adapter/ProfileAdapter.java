package com.nvcomputers.ten.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.model.output.TopLikersResponse;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.profile.NewProfileActivity;
import com.nvcomputers.ten.views.profile.NewProfileFragment;
import com.nvcomputers.ten.views.profile.ZoomImageActivity;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * This class is used to show ten users  adapter
 *
 * @author jindaldipanshu
 * @version 1.0
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {

    private SharedPrefsHelper sharedPref;
    private BaseFragment NewProfileFragment;
    private ArrayList<TopLikersResponse.Users> mList;
    private BaseActivity mActivity;
    int screen;

    public ProfileAdapter(BaseActivity profileActivity, ArrayList<?> mList, int screen) {
        this.mActivity = profileActivity;
        this.mList = (ArrayList<TopLikersResponse.Users>) mList;
        this.screen = screen;
        sharedPref = new SharedPrefsHelper(profileActivity);
    }

    public ProfileAdapter(BaseFragment baseFragment, ArrayList<?> mList, int screen) {

        this.mList = (ArrayList<TopLikersResponse.Users>) mList;
        this.screen = screen;
        this.NewProfileFragment = baseFragment;
        this.mActivity = baseFragment.getBaseActivity();
        sharedPref = new SharedPrefsHelper(baseFragment.getBaseActivity());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_profile, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        String userName = mList.get(position).getUsername();
        if (TextUtils.isEmpty(userName)) {
            viewHolder.mUserNameTextView.setText("N/A");
        } else {
            viewHolder.mUserNameTextView.setText(userName);
        }
        String imageTag = sharedPref.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(mActivity)
                .load(HttpUtils.getProfileImageURL(mList.get(position).getUsername()))
                .error(mActivity.getResources().getDrawable(R.drawable.myprofilelarge))
                .bitmapTransform(new RoundedCornersTransformation(mActivity, 15, 0))
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .into(viewHolder.mUserImagView);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mUserNameTextView;
        private ImageView mUserImagView;

        public ViewHolder(View itemView) {
            super(itemView);
            mUserImagView = (ImageView) itemView.findViewById(R.id.image_view_user_profile);
            mUserNameTextView = (TextView) itemView.findViewById(R.id.text_view_username);

            RelativeLayout layout = (RelativeLayout) itemView.findViewById(R.id.rl_profile);

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToProfile(getLayoutPosition());
                }
            });

            mUserImagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveToProfile(getLayoutPosition());
                }
            });
            mUserImagView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(mActivity, ZoomImageActivity.class);
                    intent.putExtra("username", mList.get(getLayoutPosition()).getUsername());
                    mActivity.startActivity(intent);
                    return false;
                }
            });

        }

        private void moveToProfile(int layoutPosition) {
            String localUsername = sharedPref.get(PreferenceKeys.PREF_USER_NAME, "");
            if (!mList.get(layoutPosition).getUsername().equals(localUsername)) {
                if (screen == 1) {
                    Intent profileIntent = new Intent(mActivity, NewProfileActivity.class);
                    profileIntent.putExtra("username", mList.get(layoutPosition).getUsername());
                    mActivity.startActivity(profileIntent);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("username", mList.get(layoutPosition).getUsername());
                    NewProfileFragment.replaceChildFragment(R.id.profile_frame_layout, new NewProfileFragment(), bundle);
                }
            }
        }
    }
}
