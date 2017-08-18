package com.nvcomputers.ten.model.input;

/**
 * Created by KaurGurleen on 5/3/2017.
 */

public class PostCommentInput {
    private String text;
    private String videoFile;
    private String videoPoster;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(String videoFile) {
        this.videoFile = videoFile;
    }

    public String getVideoPoster() {
        return videoPoster;
    }

    public void setVideoPoster(String videoPoster) {
        this.videoPoster = videoPoster;
    }
}

