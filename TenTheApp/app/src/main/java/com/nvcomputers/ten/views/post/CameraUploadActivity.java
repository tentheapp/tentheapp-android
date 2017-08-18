package com.nvcomputers.ten.views.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.ExifInterface;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.api.GetRestAdapter;
import com.nvcomputers.ten.model.input.VideoPostInput;
import com.nvcomputers.ten.model.output.EditProfileResponse;
import com.nvcomputers.ten.model.output.UploadPost;
import com.nvcomputers.ten.utils.FileUtils;
import com.nvcomputers.ten.utils.PreferenceKeys;
import com.nvcomputers.ten.utils.ProgressUtility;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.TTApplication;
import com.nvcomputers.ten.views.core.BaseActivity;
import com.nvcomputers.ten.views.home.LandingActivity;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

public class CameraUploadActivity extends BaseActivity implements View.OnClickListener, TextureView.SurfaceTextureListener,
        View.OnTouchListener, Camera.PictureCallback {

    private static final String TAG = "CameraUploadActivity";
    private int QUALITY = 100;
    //private static final int IMAGE_EDITER = 155;
    SurfaceTexture surfaceTexture;
    ImageView mCapturedImgView, closeBtn, dot1, dot2, dot3, dot4, flipImageView, directPostImageView, flashImageView, takePictureImageView;
    TextureView textureView;
    Camera camera;
    //public static Uri editedImageUri = null;
    public static Uri croppedVideoUri, originalUri;
    public static int currentPosition;
    File imageFile;
    boolean cameraStarted = false/*, cameraIsFront = false*/;
    String PictureFilePath = null, btnMode = "camera";
    private int frontCamera = -1;
    private int rearCamera = -1;
    private int currentCamera = 0;
    private boolean isImageCaptured, isFilterStarted, isUploadStarted;
    private ProgressBar photo_saving_progress;
    private boolean upCalled;
    private Timer captureTimer;
    private int count_time;
    private MediaRecorder media_recorder;
    private boolean isVideoRecording;
    private Timer timer;
    private TextView timerTextView;
    private VideoView videoPlayer;
    private boolean uploadAttemped;
    private int TRIM_VIDEO_CODE = 999;
    //private FFmpeg ffmpeg;
    private SharedPrefsHelper sharePref;
    private String mUserId;
    // The TransferUtility is the primary class for managing transfer to S3
    //private TransferUtility transferUtility;
    private ImageView muteImageView;
    private String mCaption;
    private FFmpeg ffmpeg;

    @Override
    public void dispose() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initViews() {

        mCaption = getIntent().getStringExtra("caption");

        sharePref = new SharedPrefsHelper(this);
        mUserId = sharePref.get(PreferenceKeys.PREF_USER_ID, "");
        closeBtn = (ImageView) findViewById(R.id.close);
        closeBtn.setOnClickListener(this);
        dot1 = (ImageView) findViewById(R.id.dot1);
        dot2 = (ImageView) findViewById(R.id.dot2);
        dot3 = (ImageView) findViewById(R.id.dot3);
        dot4 = (ImageView) findViewById(R.id.dot4);
        videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
        photo_saving_progress = (ProgressBar) findViewById(R.id.photo_saving_progress);
        mCapturedImgView = (ImageView) findViewById(R.id.image_view_captured);
        mCapturedImgView.setVisibility(View.GONE);
        takePictureImageView = (ImageView) findViewById(R.id.picture);
        takePictureImageView.setOnClickListener(this);
        muteImageView = (ImageView) findViewById(R.id.mute_icon);
        muteImageView.setOnClickListener(this);

        flipImageView = (ImageView) findViewById(R.id.flip);
        flipImageView.setImageResource(R.drawable.flipcamera);
        flashImageView = (ImageView) findViewById(R.id.flash);

        timerTextView = (TextView) findViewById(R.id.timer);
        textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);
        directPostImageView = (ImageView) findViewById(R.id.comment);
        directPostImageView.setOnClickListener(this);
        takePictureImageView.setOnTouchListener(this);
        flashImageView.setOnClickListener(this);
        flipImageView.setOnClickListener(this);

        //transferUtility = Util.getTransferUtility(this);

        ffmpeg = FFmpeg.getInstance(this);
        loadFFMpegBinary();
    }

    private void goToPostPage() {

        if (!isFilterStarted) {
            isFilterStarted = true;
            if (TTApplication.recordedVideoPath == null || TTApplication.recordedVideoPath.equals("")) {

               /* CropImage.activity(Uri.fromFile(imageFile))
                        .start(CameraUploadActivity.this);*/

                Intent intent1 = new Intent(CameraUploadActivity.this, GpuImageFiltersActivity.class);
                String realPath = imageFile.getAbsolutePath();//FileUtils.getPath(CameraUploadActivity.this, resultUri);
                intent1.putExtra("SelectedImagePath", realPath);
                intent1.putExtra("selphie", "");
                intent1.putExtra("mode", "camera");
                startActivity(intent1);
                finish();
            } else {
                // go to
                Intent intent = new Intent(this, VideoTrimmerActivity.class);
                intent.putExtra("REAL_VIDEO_PATH", TTApplication.recordedVideoPath);
                startActivityForResult(intent, TRIM_VIDEO_CODE);
                //finish();
            }
        }
    }


    private void FlashModeOn() {
        try {
            if (currentCamera == frontCamera) {
                flashImageView.setImageResource(R.drawable.noflash);
                isFlashOn = false;
            } else {
                flashImageView.setImageResource(R.drawable.flash);
                Camera.Parameters parameters = camera.getParameters();

                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void FlashModeOff() {
        try {
            flashImageView.setImageResource(R.drawable.noflash);
            Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        surfaceTexture = surface;

        Camera.CameraInfo camInfo = new Camera.CameraInfo();
        for (int camId = 0; camId < Camera.getNumberOfCameras(); camId++) {
            Camera.getCameraInfo(camId, camInfo);
            if (camInfo.facing == CAMERA_FACING_FRONT) {
                frontCamera = camId;
            }
            if (camInfo.facing == CAMERA_FACING_BACK) {
                rearCamera = camId;
            }
        }
        currentCamera = rearCamera;
        camera = Camera.open(currentCamera);
        //camera.open(0);
        //if (isPreviewRunning) {
        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
        FileUtils.setCameraDisplayOrientation(this, currentCamera, camera);
        textureView.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
        startPreview();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    private void flipCamera() {
        if (currentCamera == frontCamera && rearCamera != -1) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            camera = Camera.open(rearCamera);
            currentCamera = rearCamera;
            FileUtils.setCameraDisplayOrientation(this, currentCamera, camera);//setCameraOrientation(mWidth, mHeight);
            //textureView.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mHeight));
            startPreview();
        } else if (frontCamera != -1) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            camera = Camera.open(frontCamera);
            FlashModeOff();
            isFlashOn = false;
            currentCamera = frontCamera;
            FileUtils.setCameraDisplayOrientation(this, currentCamera, camera);//setCameraOrientation(mWidth, mHeight);
            //textureView.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mHeight));
            startPreview();
        }
    }

    private void startPreview() {
        try {
            btnMode = "camera";
            if (camera == null) {
                camera = Camera.open(currentCamera);
                FileUtils.setCameraDisplayOrientation(this, currentCamera, camera);//setCameraOrientation(mWidth, mHeight);// setCameraOrientation(mWidth, mHeight);
                //textureView.setLayoutParams(new RelativeLayout.LayoutParams(mWidth, mHeight));
                //camera.setDisplayOrientation(90);
            }
            // camera.unlock();
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
            if (videoPlayer.isPlaying()) {
                videoPlayer.stopPlayback();
            }

            takePictureImageView.setImageResource(R.drawable.ic_camerabutton);
            flipImageView.setImageResource(R.drawable.flipcamera);
            flipImageView.setVisibility(View.VISIBLE);
            mCapturedImgView.setVisibility(View.INVISIBLE);
            directPostImageView.setVisibility(View.INVISIBLE);
            flashImageView.setVisibility(View.VISIBLE);
            muteImageView.setVisibility(View.GONE);
            dot1.setVisibility(View.INVISIBLE);
            dot2.setVisibility(View.INVISIBLE);
            dot3.setVisibility(View.INVISIBLE);
            dot4.setVisibility(View.INVISIBLE);
            cameraStarted = true;

            isImageCaptured = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean isFlashOn = false, mute = false;

    @Override
    public void onClick(View v) {
        if (Utilities.checkInternet(this)) {
            switch (v.getId()) {
                case R.id.close:
                    if (isImageCaptured) {
                        //startPreview();
                        if (imageFile != null && imageFile.exists()) {
                            imageFile.delete();
                        }
//                        Intent intent = new Intent(CameraUploadActivity.this,CameraUploadActivity.class);
//                        startActivity(intent);
                        finish();
                    /*if (videoPlayer.IsPlaying)
                    {
                        videoPlayer.StopPlayback();
                    }*/
                        //videoPlayer.Visibility = ViewStates.Gone;
                    } else {
                        finish();
                    }
                    break;
                case R.id.flash:
                    if (cameraStarted) {

                        if (isFlashOn == true) {
                            FlashModeOff();
                            isFlashOn = false;
                        } else {
                            FlashModeOn();
                            isFlashOn = true;
                        }
                    }
                    break;
                case R.id.flip:
                    if (btnMode.equals("player") || btnMode.equals("image")) {
                        uploadPost();
                    } else {
                        flipCamera();
                    }
                    break;
                case R.id.comment:
                    //if (!cameraStarted) {
                    goToPostPage();
                    //}
                    break;
                case R.id.mute_icon:
                    if (mute) {
                        mute = false;
                        muteImageView.setImageResource(R.drawable.ic_unmute);
                    } else {
                        mute = true;
                        muteImageView.setImageResource(R.drawable.ic_mute);
                    }
                    break;
                default:
                    break;
            }
        } else {
            Utilities.showMessage(this, getString(R.string.no_internet_msg));
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (Utilities.checkInternet(this)) {
            Float dx = 0.0f;
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    v.animate().translationX(0).setDuration(100).start();
                    upCalled = true;
                    if (isVideoRecording && pressed_count < 2) {
                        stopTimer();
                    }
                    break;

                case MotionEvent.ACTION_DOWN:

                    if (btnMode.equals("player")) {
                        if (TTApplication.recordedVideoPath != "" && TTApplication.recordedVideoPath != null) {
                            playVideo();
                        }
                    } else if (btnMode.equals("camera")) {
                        upCalled = false;
                        setCaptureTiming();
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (btnMode.equals("player") || btnMode.equals("image")) {
                        Float change = event.getRawX() + dx - takePictureImageView.getLayoutParams().width / 2;
                        if (change > flipImageView.getX()) {
                            //direct upload
                            uploadPost();
                        } else if (change < directPostImageView.getX()) {
                            goToPostPage();
                        } else {
                            v.animate().x(change).setDuration(0).start();
                        }
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                    Toast.makeText(this, "action cancel", Toast.LENGTH_SHORT).show();
                    break;
            }

        } else {
            Utilities.showMessage(this, getString(R.string.no_internet_msg));
        }
        return true;
    }

    private void uploadPost() {
        if (!isUploadStarted) {
            isUploadStarted = true;
            if (TTApplication.recordedVideoPath == null || TTApplication.recordedVideoPath.equals("")) {
                uploadImageVideoDirectly(false);
            } else {
                uploadImageVideoDirectly(true);
            }
        }
    }

    private void stopTimer() {
        try {
            timer.cancel();
            timer = null;
            Log.e("check timer ->>> ", count_time + "");
            if (count_time < 3 || count_time == 0) {
                timerTextView.setText("00:00");
                Log.e("check timer ->>> ", count_time + "false <3");
                stopVideoRecording(false);
            }
            if (count_time >= 3) {
                Log.e("check timer ->>> ", count_time + "true >=3");
                stopVideoRecording(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    int pressed_count = 0;

    private void setCaptureTiming() {
        try {
            if (captureTimer == null) {
                pressed_count = 0;
                captureTimer = new Timer();
                captureTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        pressed_count++;
                        if (pressed_count == 1) {
                            captureTimer.cancel();
                            captureTimer = null;
                            //Remember to do it on UI thread
                            CameraUploadActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (upCalled) {
                                        capureImage();//TakePicture();
                                    } else {
                                        try {
                                            recordVideo();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Log.d("", "" + e.getMessage());
                                        }
                                    }
                                    pressed_count = 0;
                                    count_time = 0;
                                    upCalled = false;
                                }
                            });
                        }
                    }
                }, 500, 1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void recordVideo() throws IOException {
        TTApplication.recordedVideoPath = FileUtils.createDirInSdCard("temp_" + System.currentTimeMillis());
        media_recorder = new MediaRecorder();
        camera.unlock();
        media_recorder.setCamera(camera);
        //if (!mute) {
            // set for record voice as well as music player voice
            media_recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
            media_recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            CamcorderProfile cpLow = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            media_recorder.setProfile(cpLow);
        /*} else {
            // store the quality profile required
            CamcorderProfile profile = CamcorderProfile.get(currentCamera, CamcorderProfile.QUALITY_HIGH);
            // Step 2: Set sources
            media_recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

            // Step 3: Set all values contained in profile except audio settings
            media_recorder.setOutputFormat(profile.fileFormat);
            media_recorder.setVideoEncoder(profile.videoCodec);
            media_recorder.setVideoEncodingBitRate(profile.videoBitRate);
            media_recorder.setVideoFrameRate(profile.videoFrameRate);
            media_recorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);

        }*/
        //set for record voice
        //media_recorder.SetAudioSource(AudioSource.Camcorder);

        if (TTApplication.recordedVideoPath == null || TTApplication.recordedVideoPath.equals("")) {
            return;
        }
        media_recorder.setOutputFile(TTApplication.recordedVideoPath);
        if (currentCamera == frontCamera) {
            media_recorder.setOrientationHint(270);
        } else {
            media_recorder.setOrientationHint(90);
        }

        media_recorder.setMaxDuration(10000);
        //media_recorder.setPreviewDisplay(textureView.getSurfaceTexture());//SetPreviewDisplay(surface.Holder.Surface);
        media_recorder.prepare();
        media_recorder.start();
        takePictureImageView.setImageResource(R.drawable.play_video_icon);
        setTiming();
        isVideoRecording = true;
        closeBtn.setVisibility(View.INVISIBLE);
        flipImageView.setVisibility(View.INVISIBLE);
        flashImageView.setVisibility(View.INVISIBLE);
        muteImageView.setVisibility(View.VISIBLE);
        cameraStarted = false;
    }

    private void setTiming() {
        try {
            if (timer == null) {
                timerTextView.setVisibility(View.VISIBLE);
                timerTextView.setText("00:00");
                count_time = 0;
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        count_time++;
                        CameraUploadActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (count_time == 11) {
                                    timerTextView.setText("00:" + "10");
                                    timer.cancel();
                                    timer = null;
                                    stopVideoRecording(true);
                                } else if (count_time == 10) {
                                    timerTextView.setText("00:" + "10");
                                } else {
                                    timerTextView.setText("00:0" + count_time);
                                }
                            }
                        });
                    }
                }, 500, 1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopVideoRecording(boolean flag) {
        try {
            count_time = 0;
            isVideoRecording = false;
            if (flag == false) {
                Utilities.showlongToast(CameraUploadActivity.this, "Video length should be between 2-10 seconds");
                //Camera.Lock();
                flashImageView.setVisibility(View.VISIBLE);
                closeBtn.setVisibility(View.VISIBLE);
                flipImageView.setVisibility(View.VISIBLE);
                timerTextView.setVisibility(View.GONE);
                muteImageView.setVisibility(View.GONE);
                mute = false;
                muteImageView.setImageResource(R.drawable.ic_unmute);
                takePictureImageView.setImageResource(R.drawable.ic_camerabutton);
                FlashModeOff();
                cameraStarted = true;
                isFlashOn = false;
                media_recorder.stop();
                media_recorder.release();
                File file = new File(TTApplication.recordedVideoPath);
                boolean deleted = file.delete();
                TTApplication.recordedVideoPath = "";

            } else {
                if (media_recorder != null)//&& isVideoRecording == true)
                {
                    media_recorder.stop();
                    media_recorder.release();
                    media_recorder = null;
                    //isVideoRecording = false;
                    btnMode = "player";
                    takePictureImageView.setImageResource(R.drawable.ic_camerabutton);

                    playVideo();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (TTApplication.recordedVideoPath != "" && TTApplication.recordedVideoPath != null) {
                File file = new File(TTApplication.recordedVideoPath);
                boolean deleted = file.delete();
                TTApplication.recordedVideoPath = "";
            }
        }
    }

    public void playVideo() {
        cameraStarted = false;
        dot1.setVisibility(View.VISIBLE);
        dot2.setVisibility(View.VISIBLE);
        dot3.setVisibility(View.VISIBLE);
        dot4.setVisibility(View.VISIBLE);

        FlashModeOff();
        isFlashOn = false;
        flipImageView.setVisibility(View.VISIBLE);
        timerTextView.setVisibility(View.GONE);
        muteImageView.setVisibility(View.VISIBLE);
        flashImageView.setVisibility(View.INVISIBLE);
        //commentImageView.Visibility = ViewStates.Visible;
        directPostImageView.setVisibility(View.VISIBLE);
        takePictureImageView.setImageResource(R.drawable.play_video_btn);
        flipImageView.setImageResource(R.drawable.homewhite);
        videoPlayer.setVisibility(View.VISIBLE);
        closeBtn.setVisibility(View.VISIBLE);

        if (videoPlayer.isPlaying()) {
            videoPlayer.stopPlayback();
            videoPlayer.setVideoPath(TTApplication.recordedVideoPath);
            videoPlayer.start();
        } else {
            videoPlayer.setVideoPath(TTApplication.recordedVideoPath);
            videoPlayer.start();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                handleCroppedURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(CameraUploadActivity.this, "" + error, Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == TRIM_VIDEO_CODE && data != null) {
            croppedVideoUri = data.getData();
            originalUri = Uri.parse(TTApplication.recordedVideoPath);
            currentPosition = data.getIntExtra("position", 0);
            finish();
        } else {
            finish();
        }
    }

    private void handleCroppedURI(Uri resultUri) {
        //go to filters screen
        /*Intent intent1 = new Intent(CameraUploadActivity.this, GpuImageFiltersActivity.class);
        String realPath = FileUtils.getPath(CameraUploadActivity.this, resultUri);
        intent1.putExtra("SelectedImagePath", realPath);
        intent1.putExtra("selphie", "");
        intent1.putExtra("mode", "camera");
        startActivity(intent1);*/

        finish();
    }

    private void capureImage() {
        btnMode = "camera";
        try {
            camera.takePicture(null, this, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onPictureTaken(byte[] data, Camera camera) {

        if (data == null) {
            return;
        }
        isImageCaptured = true;
        isFilterStarted = false;
        isUploadStarted = false;
        Random rndm = new Random();
        int rndomNumber = rndm.nextInt();
        imageFile = new File(getExternalFilesDir(null), rndomNumber + ".jpeg");
        //picturePath = imageFile.getAbsolutePath();
        // Glide.with(this).load(file).into(mCapturedImgView);
        saveImageLocally(data);
        btnMode = "image";
        FlashModeOff();
        isFlashOn = false;
    }

    private void saveImageLocally(final byte[] data) {

        photo_saving_progress.setVisibility(View.VISIBLE);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp = null;
                try {
                    Runtime.getRuntime().gc();
                    System.gc();
                    FileUtils.saveToSdcard(data, imageFile);
                    File newFile = new File(imageFile.getAbsolutePath());
                    long fileSizeInBytes = newFile.length();
                    // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //options.inJustDecodeBounds = true;
                    if (fileSizeInMB >= 2) {
                        options.inSampleSize = 4;
                        QUALITY = 60;
                    } else if (fileSizeInMB >= 1) {
                        options.inSampleSize = 2;
                        QUALITY = 80;
                    } else {
                        options.inSampleSize = 1;
                        QUALITY = 90;
                    }
                    //options.inJustDecodeBounds = false;
                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
                    bmp = FileUtils.modifyOrientation(CameraUploadActivity.this, myBitmap, imageFile.getAbsolutePath(), currentCamera);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream);
                    byte[] rotateddata = stream.toByteArray();
                    FileUtils.saveToSdcard(rotateddata, imageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Bitmap finalBmp = bmp;
                CameraUploadActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCapturedImgView.setVisibility(View.VISIBLE);
                        mCapturedImgView.setImageBitmap(finalBmp);
                        photo_saving_progress.setVisibility(View.GONE);
                        dot1.setVisibility(View.VISIBLE);
                        dot2.setVisibility(View.VISIBLE);
                        dot3.setVisibility(View.VISIBLE);
                        dot4.setVisibility(View.VISIBLE);
                        muteImageView.setVisibility(View.GONE);
                        flashImageView.setVisibility(View.GONE);
                        directPostImageView.setVisibility(View.VISIBLE);
                        flipImageView.setVisibility(View.VISIBLE);
                        flipImageView.setImageResource(R.drawable.homewhite);
                    }
                });
            }
        });
        thread.start();
    }


    private void uploadImageVideoDirectly(boolean isVideo) {
        if (uploadAttemped) {
            return;
        }
        uploadAttemped = true;
        ProgressUtility.showProgress(this, getString(R.string.please_wait_meassge));

        Call<EditProfileResponse> response = null;
        if (isVideo) {
            trasnposeVideo();
            //uploadVideo();
            //uploadToS3();
        } else {

            if (mCaption == null) {
                mCaption = "";
            }
            RequestBody description = RequestBody.create(MultipartBody.FORM, mCaption);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), imageFile);
            MultipartBody.Part imageFilePart = MultipartBody.Part.createFormData("imageFile", imageFile.getName(), requestBody);
            response = GetRestAdapter.getRestAdapter(true).uploadImagePost(description, imageFilePart);
            processUploading(response);
        }
    }


    private void processUploading(Call<EditProfileResponse> response) {
        response.enqueue(new Callback<EditProfileResponse>() {
            @Override
            public void onResponse(Call<EditProfileResponse> call, Response<EditProfileResponse> response) {
                ProgressUtility.dismissProgress();
                TTApplication.recordedVideoPath = null;
                if (response.code() == 200) {
                    //DIRECTLY_UPLOADED_SUCEESS = true;
                    showToast("Post Uploaded Successfully");
                    if (imageFile != null && imageFile.exists()) {
                        imageFile.delete();
                    }
                    imageFile = null;
                } else {
                    showToast("" + response.message());
                }
                uploadAttemped = false;
                //sharePref.save(PreferenceKeys.PREF_HOME_PAGE_DATA, "");
                AddPostFragment.newPostAdded = true;
                Intent intent = new Intent(CameraUploadActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(Call<EditProfileResponse> call, Throwable t) {
                ProgressUtility.dismissProgress();
                showToast(t.getMessage());
                uploadAttemped = false;
                TTApplication.recordedVideoPath = null;
            }
        });
    }


    String mVideoName;
    String mImageName;

    /*private void uploadToS3() {
        File videoFile = new File(TTApplication.recordedVideoPath);
        long value = System.currentTimeMillis();
        mVideoName = videoFile.getName().trim();//"vid_" + value + ".mp4";//
        mImageName = mVideoName.replace(".mp4", ".jpeg").trim();
        ;//"img_" + value + ".jpeg";//
        //String filePath = null;
        Bitmap thumbnailImg = ThumbnailUtils.createVideoThumbnail(TTApplication.recordedVideoPath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        byte[] videoThumbImageByteArray = FileUtils.convertThumbBitmapToByteArray(thumbnailImg);
        File rootPath = Environment.getExternalStorageDirectory();
        imageFile = new File(rootPath + "/Android/data/com.Android.Ten/Videos/", mImageName);
        FileUtils.saveToSdcard(videoThumbImageByteArray, imageFile);
        TransferObserver videoObserver = transferUtility.upload(Constants.BUCKET_NAME + "/images/" + mUserId, mVideoName, videoFile);
        videoObserver.setTransferListener(new UploadListener("video"));//

        TransferObserver imageObserver = transferUtility.upload(Constants.BUCKET_NAME + "/images/" + mUserId, mImageName, imageFile);
        imageObserver.setTransferListener(new UploadListener("image"));//
    }*/

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

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
                // ProgressUtility.dismissProgress();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateVideoPost();
                    }
                });
            }
        }
    }

    private void updateVideoPost() {
        VideoPostInput input = new VideoPostInput();
        input.setText("");
        input.setVideoName(mVideoName);
        input.setVideoPosterName(mImageName);
        Call<UploadPost> caller = GetRestAdapter.getRestAdapter(true).uploadVideoOnly(input);
        caller.enqueue(new Callback<UploadPost>() {
            @Override
            public void onResponse(Call<UploadPost> call, Response<UploadPost> response) {
                Log.d("---", "----" + response.body().toString());
                ProgressUtility.dismissProgress();
                if (response.code() == 200) {
                    showToast("Post Uploaded Successfully");
                    uploadAttemped = false;
                    sharePref.save(PreferenceKeys.PREF_HOME_PAGE_DATA, "");
                    Intent intent = new Intent(CameraUploadActivity.this, LandingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UploadPost> call, Throwable t) {
                ProgressUtility.dismissProgress();
                showToast(t.getMessage());
                uploadAttemped = false;
                TTApplication.recordedVideoPath = null;
            }
        });
    }

    private void uploadVideo() {
        if (mCaption == null) {
            mCaption = "";
        }
        RequestBody description = RequestBody.create(MultipartBody.FORM, mCaption);
        Bitmap thumbnailImg = ThumbnailUtils.createVideoThumbnail((TTApplication.recordedVideoPath), MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
        byte[] videoThumbImageByteArray = FileUtils.convertThumbBitmapToByteArray(thumbnailImg);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), videoThumbImageByteArray);
        MultipartBody.Part imageFilePart = MultipartBody.Part.createFormData("videoPoster", "videoThumb.jpeg", requestBody);
        File videoFile = new File(TTApplication.rotatedVideoPath);

        //int size = (int) videoFile.length();
        //byte[] bytes = new byte[size];
        /*try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(videoFile));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        RequestBody videoRequestBody;
        //if (bytes == null) {
        videoRequestBody = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
       /* } else {
            videoRequestBody = RequestBody.create(MediaType.parse("video/mp4"), bytes);
        }*/
        MultipartBody.Part videoFilePart = MultipartBody.Part.createFormData("imageFile", videoFile.getName(), videoRequestBody);
        Call<EditProfileResponse> response = GetRestAdapter.getRestAdapter(true).uploadVideoPost(description, imageFilePart, videoFilePart);
        processUploading(response);
       /*  MultipartBody.Part videoFilePart = MultipartBody.Part.createFormData("imageFile", videoFile.getName(), videoRequestBody);
        Call<VideoUpload> response = GetRestAdapter.getVidoRestAdapter().uploadVideo(videoFilePart);
        proceeVid(response);*/
    }

    private void trasnposeVideo() {
        String outputPath = FileUtils.createDirInSdCard("rotated_" + System.currentTimeMillis());
        String cmd = "";
        if (mute){
            cmd = "-y -i " + TTApplication.recordedVideoPath + " -metadata:s:v:0 rotate=90 -preset ultrafast -an " + outputPath;
        }else {
            cmd = "-y -i " + TTApplication.recordedVideoPath + " -metadata:s:v:0 rotate=90 -preset ultrafast " + outputPath;
        }

        // String cmd = "-y -i " + TTApplication.recordedVideoPath + " -vf transpose=1 -preset ultrafast " + outputPath;//
        String[] command = cmd.split(" ");
        if (command.length != 0) {
            execFFmpegBinary(command, outputPath);
        } else {
            Toast.makeText(CameraUploadActivity.this, "", Toast.LENGTH_LONG).show();
            uploadVideo();// uploadToS3();//
        }
    }

    private void execFFmpegBinary(final String[] command, final String outputPath) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    //addTextViewToLayout("FAILED with output : " + s);
                    TTApplication.rotatedVideoPath = TTApplication.recordedVideoPath;
                    uploadVideo();// uploadToS3();//
                }

                @Override
                public void onSuccess(String s) {
                    //addTextViewToLayout("SUCCESS with output : " + s);
                    //TTApplication.recordedVideoPath = outputPath;
                    TTApplication.rotatedVideoPath = outputPath;
                    uploadVideo();// uploadToS3();//
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
            TTApplication.rotatedVideoPath = TTApplication.recordedVideoPath;
            uploadVideo();// uploadToS3();//
        }
    }

    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    //showUnsupportedExc    `eptionDialog();
                    Log.d("loadFFMpegBinary", "-----UNABLE TO LOAD BINARY FILES-------");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            //showUnsupportedExceptionDialog();
            e.printStackTrace();
        }
    }
}