package com.nvcomputers.ten.views.profile;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.squareup.picasso.Picasso;

public class ZoomImageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mUserImageView;
    private String image;
    private ProgressBar progressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_zoom_image;
    }

    @Override
    protected void initViews() {
        String username = getIntent().getStringExtra("username");
        image = getIntent().getStringExtra("image");
        mUserImageView = (ImageView) findViewById(R.id.image_view_zoom);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        SharedPrefsHelper sharedPref = new SharedPrefsHelper(this);
        if (TextUtils.isEmpty(username)) {
            if (image.contains("ten-s3-local")) {
                Picasso.with(this).load(image).into(mUserImageView);
            } else {
                Glide.with(this).load(image).into(mUserImageView);
            }
        } else {
            String imageTag = sharedPref.get(PreferenceKeys.PREF_IMAGE_TAG, "");
            Glide.with(this)
                    .load(HttpUtils.getProfileImageURL(username))
                    .placeholder(R.drawable.myprofilelarge)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .signature(new StringSignature(imageTag))
                    .into(mUserImageView);
        }
        findViewById(R.id.text_view_back).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void dispose() {
        finish();
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }
}