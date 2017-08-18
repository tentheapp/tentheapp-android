package com.nvcomputers.ten.views.home;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.nvcomputers.ten.R;
import com.squareup.picasso.Picasso;

public class CommentDetailActivity extends AppCompatActivity {

    private ImageView commentimage;
    private VideoView commetVideo;
    private ProgressBar progressbar;
    private Intent intent;
    private VideoView currentVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.tenBlue));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        initUI();
    }

    private void initUI() {
        ImageView backbtn = (ImageView) findViewById(R.id.backBtn);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commentimage = (ImageView) findViewById(R.id.commentImage);
        commetVideo = (VideoView) findViewById(R.id.commentvideo);
        progressbar = (ProgressBar) findViewById(R.id.progressBar);
        updateData();
    }

    private void updateData() {
        intent = getIntent();
        String videoUrl = intent.getStringExtra("videoUrl");
        String videoPoster = intent.getStringExtra("videoImage");
        Picasso.with(this).load(videoPoster).into(commentimage);
        playVideo(videoUrl, commetVideo);
    }

    private void playVideo(String url, VideoView videoView) {
        videoView.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.VISIBLE);
        try {
            Log.d("", "" + url);
            //&& currentVideoView.IsPlaying
            if (currentVideoView != null) {
                currentVideoView.stopPlayback();
                currentVideoView.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                currentVideoView = null;
            }
            videoView.setVisibility(View.VISIBLE);
            commentimage.setVisibility(View.GONE);
            currentVideoView = videoView;
            currentVideoView.setVideoPath(url);
            currentVideoView.requestFocus();
            currentVideoView.start();
            currentVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    currentVideoView.setVisibility(View.GONE);//Visibility = ViewStates.Gone;
                    currentVideoView = null;
                    finish();
                }
            });
            currentVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                            progressbar.setVisibility(View.GONE);
                        }
                    });
                }
            });
        } catch (Exception exp) {
            Log.d("--->", "--->" + exp);
        }
    }
}
