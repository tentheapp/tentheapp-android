package com.nvcomputers.ten.views.post;

import android.app.Dialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.adapter.SuggestionFollowingAdapter;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.imagechooser.api.ChooserType;
import com.nvcomputers.ten.imagechooser.api.ChosenImage;
import com.nvcomputers.ten.imagechooser.api.ChosenImages;
import com.nvcomputers.ten.imagechooser.api.ChosenVideo;
import com.nvcomputers.ten.imagechooser.api.ChosenVideos;
import com.nvcomputers.ten.imagechooser.api.ImageChooserListener;
import com.nvcomputers.ten.imagechooser.api.ImageChooserManager;
import com.nvcomputers.ten.interfaces.AppCommonCallback;
import com.nvcomputers.ten.model.output.EditProfileResponse;
import com.nvcomputers.ten.model.output.SearchFollowingUserResponse;
import com.nvcomputers.ten.presenter.SearchFollowingPresenter;
import com.nvcomputers.ten.utils.FileUtils;
import com.nvcomputers.ten.utils.HttpUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.ProgressUtility;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.TTApplication;
import com.nvcomputers.ten.views.core.BaseFragment;
import com.nvcomputers.ten.views.core.CarouselPagerFragment;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.StringTokenizer;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class AddPostFragment extends BaseFragment implements View.OnClickListener, ImageChooserListener, AppCommonCallback, SearchFollowingPresenter.SearchFollowingCallback {

    private static final int REQUEST_Storage = 1;
    private static final int REQUEST_CAMERA = 0;
    private static final int TRIM_VIDEO_CODE = 345;
    private static final int IMAGE_FILTER = 346;

    private ImageView userImage, cameraIcon, galleryIcon, crossImage;
    private TextView userName, postTime;
    private TextView postBtn;
    private View mLayout;
    private RelativeLayout iconsLayout, imageLayout;
    private ImageChooserManager imageChooserManager;
    private String filePath, originalFilePath, thumbnailSmallFilePath;
    private boolean isActivityResultOver;
    private ImageView postImage, selectedThumbImg;
    //private byte[] byteArray;
    private byte[] videoThumbImageByteArray;
    private File destinationFile;
    private String descriptionString = "";
    private File imageFile;
    private boolean isDataLoaded;
    private SharedPrefsHelper mSharedPrefsHelper;
    private TextView captionCount;
    private ProgressBar image_loader;
    private String userId;
    private SuggestionFollowingAdapter followingListAdapter;
    public RecyclerView suggestionRV;
    private Uri originalVideoUri;
    private Uri croppedVideoUri;
    private boolean videoFromCamera;
    public EditText edit_text_comment;
    private FFmpeg ffmpeg;
    public static boolean newPostAdded = false;
    //private FFmpeg ffmpeg;

    public AddPostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_add_post;
    }

    @Override
    protected void initViews(View view) {
        newPostAdded = false;
        mSharedPrefsHelper = new SharedPrefsHelper(getBaseActivity());
        String username = mSharedPrefsHelper.get(PreferenceKeys.PREF_USER_NAME);
        userId = mSharedPrefsHelper.get(PreferenceKeys.PREF_USER_ID);
        userName = (TextView) view.findViewById(R.id.user_name);
        if (!TextUtils.isEmpty(username)) {
            userName.setText(username);
        }
        transferUtility = Util.getTransferUtility(mContext);
        ffmpeg = FFmpeg.getInstance(getBaseActivity());
        loadFFMpegBinary();
        suggestionRV = (RecyclerView) findViewById(R.id.suggestionRV);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        suggestionRV.setLayoutManager(mLayoutManager);

        image_loader = (ProgressBar) view.findViewById(R.id.image_loader);
        mLayout = view.findViewById(R.id.post_add_layout);
        userImage = (ImageView) view.findViewById(R.id.user_img);
        cameraIcon = (ImageView) view.findViewById(R.id.camera_icon);
        galleryIcon = (ImageView) view.findViewById(R.id.gallery_icon);
        postTime = (TextView) view.findViewById(R.id.post_time);
        selectedThumbImg = (ImageView) view.findViewById(R.id.thumnail_img);
        iconsLayout = (RelativeLayout) view.findViewById(R.id.icons_layout);
        imageLayout = (RelativeLayout) view.findViewById(R.id.showImageRelate);
        edit_text_comment = (EditText) view.findViewById(R.id.caption);
        edit_text_comment.setOnClickListener(this);
        postBtn = (TextView) view.findViewById(R.id.postBtn);
        postImage = (ImageView) findViewById(R.id.thumnail_img);
        crossImage = (ImageView) findViewById(R.id.cross_img);
        cameraIcon.setOnClickListener(this);
        galleryIcon.setOnClickListener(this);
        postBtn.setOnClickListener(this);
        crossImage.setOnClickListener(this);
        mLayout.setOnClickListener(this);
        captionCount = (TextView) findViewById(R.id.characterCount);
        String imageTag = mSharedPrefsHelper.get(PreferenceKeys.PREF_IMAGE_TAG, "");
        Glide.with(this)
                .load(HttpUtils.getProfileImageURL(username))
                //.bitmapTransform(new RoundedCornersTransformation(getBaseActivity(), 15, 0))
                .placeholder(R.drawable.myprofilelarge)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .signature(new StringSignature(imageTag))
                .centerCrop()
                .into(userImage);

        edit_text_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence != null && charSequence.length() > 0) {
                    postBtn.setTextColor(Color.WHITE);
                    postBtn.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.post_selected));
                } else {
                    if (!isDataLoaded) {
                        postBtn.setTextColor(getBaseActivity().getResources().getColor(R.color.tenBlue));
                        postBtn.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.post_hollow));
                    } else {
                        postBtn.setTextColor(Color.WHITE);
                        postBtn.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.post_selected));
                    }
                }
                int textLength = edit_text_comment.getText().toString().trim().length();
                int viewText = 250 - textLength;
                captionCount.setText(viewText + "".trim());
                String comment = edit_text_comment.getText().toString();
                if (!TextUtils.isEmpty(comment)) {//&& textLength < 251
                    if (comment.contains("@")) {
                        if (comment.length() > 1) {
                            int index = comment.indexOf("@");
                            StringTokenizer stringTokenizer = new StringTokenizer(comment, "@");
                            String token = null;
                            while (stringTokenizer.hasMoreTokens()) {
                                token = stringTokenizer.nextToken();
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
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void hitFollowingSuggestionsApi(String suggestion, int index) {
        Log.e("suggesstion--", suggestion);
        if (Utilities.checkInternet(getBaseActivity())) {
            SearchFollowingPresenter searchFollowingPresenter = new SearchFollowingPresenter(this, this);
            searchFollowingPresenter.responseCheck(userId, suggestion);
        } else {
            showToast(R.string.no_internet_msg);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (GpuImageFiltersActivity.savedfilePath != null) {
            iconsLayout.setVisibility(View.INVISIBLE);
            imageLayout.setVisibility(View.VISIBLE);
            final String picturePath = GpuImageFiltersActivity.savedfilePath;
            GpuImageFiltersActivity.savedfilePath = null;
            Uri imageUri = Uri.fromFile(new File(picturePath));
            CropImage.activity(imageUri)
                    .start(mContext, this);
           /* imageFile = new File(picturePath);
            image_loader.setVisibility(View.VISIBLE);
            //
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    mBitmap = BitmapFactory.decodeFile(picturePath);
                    getBaseActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            selectedThumbImg.setImageBitmap(mBitmap);
                            image_loader.setVisibility(View.GONE);
                            postBtn.setTextColor(Color.WHITE);
                            postBtn.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.post_selected));
                        }
                    });
                }
            });
            thread.start();*/
        } else if (CameraUploadActivity.croppedVideoUri != null) {
            croppedVideoUri = CameraUploadActivity.croppedVideoUri;
            originalVideoUri = CameraUploadActivity.originalUri;
            int currentPos = CameraUploadActivity.currentPosition;
            CameraUploadActivity.croppedVideoUri = CameraUploadActivity.originalUri = null;
            CameraUploadActivity.currentPosition = 0;
            setVideoImage(currentPos);
            videoFromCamera = true;
        }
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                context,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onClick(View view) {
        if (Utilities.checkInternet(mContext)) {
            switch (view.getId()) {
                case R.id.post_add_layout:
                    Utilities.hideKeypad(view);
                    break;
                case R.id.camera_icon:
                    // cameraPermission();
                    //permission

               /* Intent intent = new Intent(mContext, VideoCommentsActivity.class);
                startActivity(intent);*/
                    Intent intent = new Intent(mContext, CameraUploadActivity.class);
                    intent.putExtra("caption", edit_text_comment.getText().toString().trim());
                    startActivity(intent);

                    break;
                case R.id.gallery_icon:
                    //chooseImage();
                    openGallery();
                    break;
                case R.id.postBtn:
                    Utilities.hideKeypad(view);
                    if (!Utilities.checkInternet(mContext)) {
                        showToast(R.string.no_internet_msg);
                    } else if (edit_text_comment.getText().toString().trim().length() > 250) {
                        //showToast("Text is too large");
                    } else if (originalVideoUri != null && croppedVideoUri != null) {
                        uploadImageVideo(true);
                    } else if (imageFile != null || !edit_text_comment.getText().toString().equals("")) {
                        uploadImageVideo(false);
                    } else {
                        showToast("Please select image or enter caption to post data");
                    }
                    break;
                case R.id.cross_img:
                    //To-do
                    String caption = edit_text_comment.getText().toString().trim();
                    if (TextUtils.isEmpty(caption)) {
                        postBtn.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.post_hollow));
                        postBtn.setTextColor(getBaseActivity().getResources().getColor(R.color.tenBlue));

                    }
                    isDataLoaded = false;
                    imageFile = null;
                    originalVideoUri = null;
                    croppedVideoUri = null;
                    //CameraUploadActivity.editedImageUri = null;
                    iconsLayout.setVisibility(View.VISIBLE);
                    imageLayout.setVisibility(View.GONE);
                    Glide.with(AddPostFragment.this)
                            .load("")
                            .into(selectedThumbImg);
                    break;

                case R.id.caption:
                    edit_text_comment.requestFocus();
                    Utilities.showKeyboard(getBaseActivity());
                    break;

                default:

                    break;
            }
        } else {
            Utilities.showMessage(mContext, getString(R.string.no_internet_msg));
        }


    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/* video/*");
        startActivityForResult(intent, ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO);
    }

    private void uploadImageVideo(boolean isVideo) {

        ProgressUtility.showProgress(getBaseActivity(), getString(R.string.please_wait_meassge));

        descriptionString = edit_text_comment.getText().toString().trim();

        Call<EditProfileResponse> response = null;
        if (isVideo) {
            File videoFile = new File(croppedVideoUri.toString());
            String videoFilePath = videoFile.getPath();
            if (videoFilePath.contains("DCIM") || videoFilePath.contains("temp_")) {
                trasnposeVideo(videoFilePath);
            } else {
                uploadVideo(videoFilePath);
            }
        } else if (imageFile == null) {
            RequestBody description = RequestBody.create(MultipartBody.FORM, descriptionString);
            response = GetRestAdapter.getRestAdapter(true).uploadTextPost(description);
            processUploading(response);
        } else {
            String uriType = "image/png";
            Bitmap.CompressFormat formatType = Bitmap.CompressFormat.PNG;
            if (imageFile.getPath().contains("jpeg")) {
                uriType = "image/jpeg";
                formatType = Bitmap.CompressFormat.JPEG;
            } else if (imageFile.getPath().contains("jpg")) {
                uriType = "image/jpg";
                formatType = Bitmap.CompressFormat.JPEG;
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            mBitmap.compress(formatType, 90, stream);
            byte[] byteArray = stream.toByteArray();
            RequestBody description = RequestBody.create(MultipartBody.FORM, descriptionString);
            RequestBody requestBody = RequestBody.create(MediaType.parse(uriType), byteArray);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("imageFile", imageFile.getName(), requestBody);
            response = GetRestAdapter.getRestAdapter(true).uploadImagePost(description, filePart);
            processUploading(response);
        }

    }

    public void updateUI() {
        try {
            edit_text_comment.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*
        * A TransferListener class that can listen to a upload task and be notified
        * when the status changes.
        */
    private class UploadListener implements TransferListener {

        private String fileType = "video";

        UploadListener(String fileType) {
            this.fileType = fileType;
        }

        // Simply updates the UI list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));

        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);

            if (newState.equals(TransferState.COMPLETED) && fileType.equals("video")) {
                //TODO -- ProgressUtility.dismissProgress();
            }
        }
    }

    private TransferUtility transferUtility;
    String mVideoName;
    String mImageName;

    private void uploadToS3() {
        File videoFile = new File(croppedVideoUri.toString());
        long value = System.currentTimeMillis();
        mVideoName = "vid_" + value + ".mp4";//videoFile.getName();
        mImageName = "img_" + value + ".jpeg";// videoName.replace(".mp4", ".jpeg");
        //String filePath = null;
        Bitmap thumbnailImg = ThumbnailUtils.createVideoThumbnail(TTApplication.recordedVideoPath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        byte[] videoThumbImageByteArray = FileUtils.convertThumbBitmapToByteArray(thumbnailImg);
        File rootPath = Environment.getExternalStorageDirectory();
        imageFile = new File(rootPath + "/Android/data/com.Android.Ten/Videos/", mImageName);
        //save file to sd card to upload on server.
        FileUtils.saveToSdcard(videoThumbImageByteArray, imageFile);
        //upload video on s3 server
        TransferObserver videoObserver = transferUtility.upload(Constants.BUCKET_NAME + "/images/" + userId, mVideoName, videoFile);
        videoObserver.setTransferListener(new UploadListener("video"));//
        //upload video thumbnail on s3 sever
        TransferObserver imageObserver = transferUtility.upload(Constants.BUCKET_NAME + "/images/" + userId, mImageName, imageFile);
        imageObserver.setTransferListener(new UploadListener("image"));//
    }

    private void uploadVideo(String videoPath) {
        RequestBody description = RequestBody.create(MultipartBody.FORM, descriptionString);
        Bitmap thumbnailImg = mBitmap;//ThumbnailUtils.createVideoThumbnail((TTApplication.recordedVideoPath), MediaStore.Images.Thumbnails.MINI_KIND);
        byte[] videoThumbImageByteArray = FileUtils.convertThumbBitmapToByteArray(thumbnailImg);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), videoThumbImageByteArray);
        MultipartBody.Part imageFilePart = MultipartBody.Part.createFormData("videoPoster", "videoThumb.jpeg", requestBody);
        File videoFile = new File(videoPath);

        RequestBody videoRequestBody = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
        MultipartBody.Part videoFilePart = MultipartBody.Part.createFormData("imageFile", videoFile.getName(), videoRequestBody);
        Call<EditProfileResponse> response = GetRestAdapter.getRestAdapter(true).uploadVideoPost(description, imageFilePart, videoFilePart);
        processUploading(response);
    }

    private void processUploading(Call<EditProfileResponse> response) {
        response.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                ProgressUtility.dismissProgress();
                if (response.code() == 200) {
                    //showToast("Post Uploaded Successfully");
                    edit_text_comment.setText("");
                    croppedVideoUri = null;
                    originalVideoUri = null;
                    selectedThumbImg.setImageBitmap(null);
                    if (imageFile != null && imageFile.exists()) {
                        imageFile.delete();
                    }
                    iconsLayout.setVisibility(View.VISIBLE);
                    imageLayout.setVisibility(View.GONE);
                    imageFile = null;
                    //mSharedPrefsHelper.save(PreferenceKeys.PREF_HOME_PAGE_DATA, "");
                    newPostAdded = true;
                    moveToHomeFragment();
                } else {
                    showToast("" + response.message());
                }
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                ProgressUtility.dismissProgress();
                showToast(t.getMessage());
            }
        });
    }

    private void moveToHomeFragment() {

        Fragment parentFragmemt = getParentFragment();
        if (parentFragmemt instanceof CarouselPagerFragment) {
            CarouselPagerFragment carouselPagerFragment = (CarouselPagerFragment) parentFragmemt;
            carouselPagerFragment.updatePage(0);
            carouselPagerFragment.updateButtonUI();

        }
    }


    private void chooseImage() {
        //chooserType = ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO;
        imageChooserManager = new ImageChooserManager(this,
                ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO, false) {
            @Override
            public void onProcessedVideo(ChosenVideo video) {


            }

            @Override
            public void onProcessedVideos(ChosenVideos videos) {

            }
        };

        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.clearOldFiles();
        try {
            filePath = imageChooserManager.choose();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onImageChosen(final ChosenImage image) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "Chosen Image: O - " + image.getFilePathOriginal());
                Log.i(TAG, "Chosen Image: T - " + image.getFileThumbnail());
                Log.i(TAG, "Chosen Image: Ts - " + image.getFileThumbnailSmall());
                isActivityResultOver = true;
                originalFilePath = image.getFilePathOriginal();
                //thumbnailFilePath = image.getFileThumbnail();
                thumbnailSmallFilePath = image.getFilePathOriginal();
                //pbar.setVisibility(View.GONE);
                if (image != null) {
                    Log.i(TAG, "Chosen Image: Is not null");
                    loadImage(postImage, image.getFilePathOriginal());
                } else {
                    Log.i(TAG, "Chosen Image: Is null");
                }
            }
        });
    }

    @Override
    public void onError(final String reason) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Log.i(TAG, "OnError: " + reason);
                //pbar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), reason,
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onImagesChosen(final ChosenImages images) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "On Images Chosen: " + images.size());
                onImageChosen(images.getImage(0));
            }
        });
    }

    private void loadImage(ImageView iv, final String path) {
        //get image bitmap to get image code
        if (path != null && path.length() > 0) {
            Glide.with(mContext)
                    .load(path)
                    .into(postImage);
        }
    }

    private void reinitializeImageChooser() {
        imageChooserManager = new ImageChooserManager(this, ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO, true) {
            @Override
            public void onProcessedVideo(ChosenVideo video) {

            }

            @Override
            public void onProcessedVideos(ChosenVideos videos) {

            }
        };
        Bundle bundle = new Bundle();
        bundle.putBoolean(Intent.EXTRA_ALLOW_MULTIPLE, true);
        imageChooserManager.setExtras(bundle);
        imageChooserManager.setImageChooserListener(this);
        imageChooserManager.reinitialize(filePath);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "Saving Stuff");
        Log.i(TAG, "File Path: " + filePath);
        //Log.i(TAG, "Chooser Type: " + chooserType);
        outState.putBoolean("activity_result_over", isActivityResultOver);
        //outState.putInt("chooser_type", chooserType);
        outState.putString("media_path", filePath);
        outState.putString("orig", originalFilePath);
        outState.putString("thumbs", thumbnailSmallFilePath);
        super.onSaveInstanceState(outState);
    }


    //@Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            /*if (savedInstanceState.containsKey("chooser_type")) {
                chooserType = savedInstanceState.getInt("chooser_type");
            }*/
            if (savedInstanceState.containsKey("media_path")) {
                filePath = savedInstanceState.getString("media_path");
            }
            if (savedInstanceState.containsKey("activity_result_over")) {
                isActivityResultOver = savedInstanceState.getBoolean("activity_result_over");
                originalFilePath = savedInstanceState.getString("orig");
                thumbnailSmallFilePath = savedInstanceState.getString("thumbs");
            }
        }
        Log.i(TAG, "Restoring Stuff");
        Log.i(TAG, "File Path: " + filePath);
        //Log.i(TAG, "Chooser Type: " + chooserType);
        Log.i(TAG, "Activity Result Over: " + isActivityResultOver);
        if (isActivityResultOver) {
            populateData();
        }
        //super.onRestoreInstanceState(savedInstanceState);
    }

    private void populateData() {
        Log.i(TAG, "Populating Data");
        //loadImage(imageViewThumbnail, thumbnailFilePath);
        loadImage(postImage, thumbnailSmallFilePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            Log.i(TAG, "File Path : " + filePath);

            //Log.i(TAG, "Chooser Type: " + chooserType);
            if (requestCode == ChooserType.REQUEST_PICK_PICTURE_OR_VIDEO ||
                    requestCode == ChooserType.REQUEST_PICK_PICTURE) {

                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();
                    handleUri(uri);
                } else {
                    Uri uri = Uri.fromFile(new File(filePath));
                    if (uri != null) {
                        handleUri(uri);
                    } else {
                        if (imageChooserManager == null) {
                            reinitializeImageChooser();
                        }
                        imageChooserManager.submit(requestCode, data);
                    }
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    handleCroppedURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(getBaseActivity(), "" + error, Toast.LENGTH_LONG).show();
                }
            } else if (requestCode == TRIM_VIDEO_CODE) {
                croppedVideoUri = data.getData();
                int currentPosition = data.getIntExtra("position", 0);
                setVideoImage(currentPosition);
            }

        }
    }

    private void handleCroppedURI(Uri resultUri) {
        /*Intent intent1 = new Intent(getBaseActivity(), GpuImageFiltersActivity.class);
        String realPath = FileUtils.getPath(getBaseActivity(), resultUri);
        intent1.putExtra("SelectedImagePath", realPath);
        intent1.putExtra("selphie", "");
        intent1.putExtra("mode", "gallery");
        startActivity(intent1);*/
        final String picturePath = FileUtils.getPath(getBaseActivity(), resultUri);
        imageFile = new File(picturePath);
        image_loader.setVisibility(View.VISIBLE);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mBitmap = BitmapFactory.decodeFile(picturePath);
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        selectedThumbImg.setImageBitmap(mBitmap);
                        image_loader.setVisibility(View.GONE);
                        postBtn.setTextColor(Color.WHITE);
                        postBtn.setBackground(getBaseActivity().getResources().getDrawable(R.drawable.post_selected));
                    }
                });
            }
        });
        thread.start();
    }

    private void setVideoImage(int currentPosition) {
        iconsLayout.setVisibility(View.GONE);
        imageLayout.setVisibility(View.VISIBLE);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(mContext, originalVideoUri);
        if (currentPosition < 100) {
            currentPosition = currentPosition * 1000000;
        } else if (currentPosition < 100000) {
            currentPosition = currentPosition * 1000;
        }
        Bitmap bitmap = mediaMetadataRetriever.getFrameAtTime(currentPosition, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        //Bitmap bitmap1 = mediaMetadataRetriever.getFrameAtTime(2000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        Log.d("", currentPosition + "===>" + bitmap);
        isDataLoaded = true;
        postBtn.setBackground(mContext.getResources().getDrawable(R.drawable.post_selected));
        postBtn.setTextColor(Color.WHITE);
        mBitmap = bitmap;
        //selectedThumbImg.setBackgroundDrawable(null);
        selectedThumbImg.setImageBitmap(bitmap);
        image_loader.setVisibility(View.GONE);
    }


    private Bitmap mBitmap;
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(final Bitmap bitmap, GlideAnimation glideAnimation) {
            // do something with the bitmap
            // for demonstration purposes, let's just set it to an ImageView
            mBitmap = bitmap;
            selectedThumbImg.setImageBitmap(bitmap);
            image_loader.setVisibility(View.GONE);
           /* Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream out = null;
                    try {
                        String path = Environment.getExternalStorageDirectory().toString();
                        imageFile = new File(path, "TenPost" + System.currentTimeMillis() + ".jpeg");
                        out = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out); // bmp is your Bitmap instance
                        mBitmap = bitmap;
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            thread.start();*/
        }
    };

    private void handleUri(Uri mSelectedImageUri) {
        if (mSelectedImageUri == null) {
            Log.i("MainActivity", "imageUri is null");
        }
        String realPath = FileUtils.getPath(mContext, mSelectedImageUri);
        String extension = MimeTypeMap.getFileExtensionFromUrl(realPath);
        String fileFormatType = "";
        if (extension != null && !extension.equals("")) {
            fileFormatType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        if (fileFormatType == null || fileFormatType.equals("")) {
            showToast("File not found.");
            return;
        }
        if (fileFormatType.contains("Image") || fileFormatType.contains("image")) {
           /* CropImage.activity(mSelectedImageUri)
                    .start(getContext(), this);*/
            Intent intent1 = new Intent(getBaseActivity(), GpuImageFiltersActivity.class);
            //String realPath = FileUtils.getPath(getBaseActivity(), resultUri);
            intent1.putExtra("SelectedImagePath", realPath);
            intent1.putExtra("selphie", "");
            intent1.putExtra("mode", "gallery");
            startActivity(intent1);
            //startActivityForResult(intent1, IMAGE_FILTER);

        } else if (fileFormatType.contains("Video") || fileFormatType.contains("video")) {
            originalVideoUri = mSelectedImageUri;
            Intent intent = new Intent(getBaseActivity(), VideoTrimmerActivity.class);
            intent.putExtra("REAL_VIDEO_PATH", realPath);
            startActivityForResult(intent, TRIM_VIDEO_CODE);
        }
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }

    private void showSuggestionDialog() {
        //mSuggestionDialog = new Dialog(getActivity());
        //mSuggestionDialog.setContentView(R.layout.suggestion_name_layout);

    }

    @Override
    public void searchFollowingnError(Call<SearchFollowingUserResponse> call, Throwable t) {
        Utilities.showMessage(getActivity(), getString(R.string.server_error_msg));
        suggestionRV.setVisibility(View.GONE);
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

    private void trasnposeVideo(String inputVideoPath) {
        String outputPath = FileUtils.createDirInSdCard("rotated_" + System.currentTimeMillis());
        String cmd = "-y -i " + inputVideoPath + " -metadata:s:v:0 rotate=90 -preset ultrafast " + outputPath;
        // String cmd = "-y -i " + TTApplication.recordedVideoPath + " -vf transpose=1 -preset ultrafast " + outputPath;//
        String[] command = cmd.split(" ");
        if (command.length != 0) {
            execFFmpegBinary(command, inputVideoPath, outputPath);
        } else {
            Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
            uploadVideo(inputVideoPath);// uploadToS3();//
        }
    }

    private void execFFmpegBinary(final String[] command, final String inputVideoPath, final String outputPath) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    //addTextViewToLayout("FAILED with output : " + s);
                    uploadVideo(inputVideoPath);// uploadToS3();//
                }

                @Override
                public void onSuccess(String s) {
                    //addTextViewToLayout("SUCCESS with output : " + s);
                    uploadVideo(outputPath);// uploadToS3();//
                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                    //addTextViewToLayout("progress : " + s);
                    //progressDialog.setMessage("Processing\n" + s);
                }

                @Override
                public void onStart() {
                    //outputLayout.removeAllViews();

                    Log.d(TAG, "Started command : ffmpeg " + command);
                    //progressDialog.setMessage("Processing...");
                    //progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);
                    //progressDialog.dismiss();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
            uploadVideo(inputVideoPath);// uploadToS3();//
        }
    }

    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    //showUnsupportedExceptionDialog();
                    Log.d("loadFFMpegBinary", "-----UNABLE TO LOAD BINARY FILES-------");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            //showUnsupportedExceptionDialog();
            e.printStackTrace();
        }
    }
}
