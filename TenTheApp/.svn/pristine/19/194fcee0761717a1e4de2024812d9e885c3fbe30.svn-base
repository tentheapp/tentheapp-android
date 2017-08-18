package com.nvcomputers.ten.presenter;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.output.PostCommentOutput;
import com.nvcomputers.ten.utils.FileUtils;
import com.nvcomputers.ten.utils.ProgressUtility;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by KaurGurleen on 5/3/2017.
 */

public class PostCommentPresenter {
    CommentCallback commentCallback;

    public PostCommentPresenter(CommentCallback commentCallback) {
        this.commentCallback = commentCallback;
    }

    public interface CommentCallback {
        void postCommmentError(Call<PostCommentOutput> call, Throwable t);

        void postCommmentSuccess(Response<PostCommentOutput> response);
    }

    public void responseCheck(String postId, String commentStr, String videoPath) {

        Call<PostCommentOutput> response = null;
        if (videoPath != null && videoPath.length() > 0) {
            RequestBody description = RequestBody.create(MultipartBody.FORM, commentStr);
            if (commentStr.equals("Image_Comment")) {
                File imageFile = new File(videoPath);
                RequestBody imageRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
                MultipartBody.Part imageFilePart = MultipartBody.Part.createFormData("videoFile", imageFile.getName(), imageRequestBody);
                response = GetRestAdapter.getRestAdapter(true).commentImagePost(postId, description, imageFilePart);
            } else {
                Bitmap thumbnailImg = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
                byte[] videoThumbImageByteArray = FileUtils.convertThumbBitmapToByteArray(thumbnailImg);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), videoThumbImageByteArray);
                MultipartBody.Part imageFilePart = MultipartBody.Part.createFormData("videoPoster", "videoThumb.jpeg", requestBody);
                File videoFile = new File(videoPath);
                RequestBody videoRequestBody = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
                MultipartBody.Part videoFilePart = MultipartBody.Part.createFormData("videoFile", videoFile.getName(), videoRequestBody);
                response = GetRestAdapter.getRestAdapter(true).commentVideoPost(postId, description, imageFilePart, videoFilePart);
            }
        } else {
            RequestBody description = RequestBody.create(MultipartBody.FORM, commentStr);
            response = GetRestAdapter.getRestAdapter(true).commentPost(postId, description);
        }

        response.enqueue(new Callback<PostCommentOutput>() {
            @Override
            public void onResponse(Call<PostCommentOutput> call, Response<PostCommentOutput> response) {
                commentCallback.postCommmentSuccess(response);
                ProgressUtility.dismissProgress();
            }

            @Override
            public void onFailure(Call<PostCommentOutput> call, Throwable t) {
                ProgressUtility.dismissProgress();
                commentCallback.postCommmentError(call, t);
            }
        });
    }
}
