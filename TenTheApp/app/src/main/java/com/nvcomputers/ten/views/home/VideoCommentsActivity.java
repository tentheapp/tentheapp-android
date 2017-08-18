package com.nvcomputers.ten.views.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.net.Uri;
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

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.nvcomputers.ten.R;
import com.nvcomputers.ten.utils.FileUtils;
import com.nvcomputers.ten.utils.ProgressUtility;
import com.nvcomputers.ten.utils.SharedPrefsHelper;
import com.nvcomputers.ten.utils.Utilities;
import com.nvcomputers.ten.views.TTApplication;
import com.nvcomputers.ten.views.core.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK;
import static android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT;

public class VideoCommentsActivity extends BaseActivity implements View.OnClickListener, TextureView.SurfaceTextureListener,
        View.OnTouchListener, Camera.PictureCallback {

    private static final String TAG = "VideoComment";
    private int QUALITY = 100;
    SurfaceTexture surfaceTexture;
    ImageView closeBtn, flipImageView, doneImageView, takePictureImageView;
    TextureView textureView;
    Camera camera;
    //public static Uri editedImageUri = null;
    //public static Uri originalUri;
    //public static int currentPosition;
    File imageFile;
    boolean cameraStarted = false/*, cameraIsFront = false*/;
    //String PictureFilePath = null;
    private int frontCamera = -1;
    private int rearCamera = -1;
    private int currentCamera = 0;

    private boolean isImageCaptured, isUploadStarted;
    private ProgressBar photo_saving_progress;
    private boolean upCalled;
    private Timer captureTimer;
    private int count_time;
    private MediaRecorder media_recorder;
    private boolean isVideoRecording;
    private Timer timer;
    private TextView timerTextView;
    private VideoView videoPlayer;
    //private boolean uploadAttemped;
    //private SharedPrefsHelper sharePref;
    private String btnMode = "camera";
    //private boolean isFilterStarted;
    private String picturePath;
    private boolean isFlashOn;
    private ImageView flashImageView;
    private ImageView mCapturedImgView;
    private ImageView muteImageView;
    private boolean mute;
    private FFmpeg ffmpeg;

    @Override
    public void dispose() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_camera_for_video_comments;
    }

    @Override
    protected void initViews() {
        //DIRECTLY_UPLOADED_SUCEESS = false;
        //sharePref = new SharedPrefsHelper(this);
        closeBtn = (ImageView) findViewById(R.id.close);
        closeBtn.setOnClickListener(this);

        videoPlayer = (VideoView) findViewById(R.id.videoPlayer);
        photo_saving_progress = (ProgressBar) findViewById(R.id.photo_saving_progress);
        mCapturedImgView = (ImageView) findViewById(R.id.image_view_captured);
        takePictureImageView = (ImageView) findViewById(R.id.picture);
        muteImageView = (ImageView)findViewById(R.id.mute_icon_comment);
        muteImageView.setOnClickListener(this);
        takePictureImageView.setOnClickListener(this);
        flipImageView = (ImageView) findViewById(R.id.flip);
        flipImageView.setImageResource(R.drawable.flipcamera);
        doneImageView = (ImageView) findViewById(R.id.done);
        flashImageView = (ImageView) findViewById(R.id.flash);

        timerTextView = (TextView) findViewById(R.id.timer);
        textureView = (TextureView) findViewById(R.id.textureView);
        textureView.setSurfaceTextureListener(this);

        takePictureImageView.setOnTouchListener(this);
        doneImageView.setOnClickListener(this);
        flipImageView.setOnClickListener(this);
        flashImageView.setOnClickListener(this);

        ffmpeg = FFmpeg.getInstance(this);
        loadFFMpegBinary();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTApplication.recordedVideoPath = "";
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

        try {
            camera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }

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
            FileUtils.setCameraDisplayOrientation(this, currentCamera, camera);
            startPreview();
        } else if (frontCamera != -1) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            camera = Camera.open(frontCamera);
            currentCamera = frontCamera;
            FlashModeOff();
            isFlashOn = false;
            FileUtils.setCameraDisplayOrientation(this, currentCamera, camera);
            startPreview();
        }
    }

    private void startPreview() {
        try {
            btnMode = "camera";
            if (camera == null) {
                camera = Camera.open(currentCamera);
                FileUtils.setCameraDisplayOrientation(this, currentCamera, camera);
            }
            TTApplication.recordedVideoPath = "";
            // camera.unlock();
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
            if (videoPlayer.isPlaying()) {
                videoPlayer.stopPlayback();
            }
            takePictureImageView.setImageResource(R.drawable.ic_camerabutton);
            flipImageView.setImageResource(R.drawable.flipcamera);
            flipImageView.setVisibility(View.VISIBLE);
            doneImageView.setVisibility(View.GONE);
            flashImageView.setVisibility(View.VISIBLE);
            muteImageView.setVisibility(View.GONE);
            cameraStarted = true;
            isImageCaptured = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close:
                if (isImageCaptured) {
                    startPreview();
                } else {
                    finish();
                }
                break;
            case R.id.mute_icon_comment:
                if (mute) {
                    mute = false;
                    muteImageView.setImageResource(R.drawable.ic_unmute);
                } else {
                    mute = true;
                    muteImageView.setImageResource(R.drawable.ic_mute);
                }
                break;
            case R.id.done:
                //Toast.makeText(this, "upload comment", Toast.LENGTH_SHORT).show();
                if (TTApplication.recordedVideoPath == null || TTApplication.recordedVideoPath.equals("")) {
                    Intent intent = new Intent();
                    intent.putExtra("recordedPath", "");
                    intent.putExtra("imagePath", imageFile.getPath());
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    ProgressUtility.showProgress(this, getString(R.string.please_wait_meassge));
                    trasnposeVideo();
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
                flipCamera();
                break;

            default:
                break;
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
    public boolean onTouch(View v, MotionEvent event) {
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

                /*if (TTApplication.recordedVideoPath != "" && TTApplication.recordedVideoPath != null) {
                    playVideo();
                } else {
                    upCalled = false;
                    setCaptureTiming();
                }*/
                if (btnMode.equals("player")) {
                    if (TTApplication.recordedVideoPath != "" && TTApplication.recordedVideoPath != null) {
                        playVideo();
                    }
                } else if (btnMode.equals("camera")) {
                    upCalled = false;
                    setCaptureTiming();
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                Toast.makeText(this, "action cancel", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void stopTimer() {
        try {
            timer.cancel();
            timer = null;
            if (count_time < 3) {
                timerTextView.setText("00:00");
                stopVideoRecording(false);
            }
            if (count_time >= 3) {
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
                            VideoCommentsActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (upCalled) {
                                        captureImage();
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

    private void captureImage() {
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
        //isFilterStarted = false;
        isUploadStarted = false;
        // startCameraPreview();
        Random rndm = new Random();
        int rndomNumber = rndm.nextInt();
        imageFile = new File(getExternalFilesDir(null), rndomNumber + ".jpeg");
        picturePath = imageFile.getAbsolutePath();
        // Glide.with(this).load(file).into(mCapturedImgView);
        ///textureView.setba
        saveImageLocally(data);
        btnMode = "image";
        FlashModeOff();
        isFlashOn = false;
        flashImageView.setVisibility(View.GONE);
        doneImageView.setVisibility(View.VISIBLE);
    }


    private void saveImageLocally(final byte[] data) {
        //mCapturedImgView.setVisibility(View.VISIBLE);
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

                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    bmp = FileUtils.modifyOrientation(VideoCommentsActivity.this, myBitmap, picturePath, currentCamera);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, QUALITY, stream);
                    byte[] rotateddata = stream.toByteArray();
                    FileUtils.saveToSdcard(rotateddata, imageFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Bitmap finalBmp = bmp;
                VideoCommentsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCapturedImgView.setVisibility(View.VISIBLE);
                        mCapturedImgView.setImageBitmap(finalBmp);
                        photo_saving_progress.setVisibility(View.GONE);
                        flipImageView.setVisibility(View.GONE);
                        muteImageView.setVisibility(View.GONE);
                        //flipImageView.setImageResource(R.drawable.homewhite);
                    }
                });
            }
        });
        thread.start();
    }

    private void recordVideo() throws IOException {
        TTApplication.recordedVideoPath = FileUtils.createDirInSdCard("temp_" + System.currentTimeMillis());
        media_recorder = new MediaRecorder();
        camera.unlock();
        media_recorder.setCamera(camera);
        // set for record voice as well as music player voice
        media_recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
        //set for record voice
        //media_recorder.SetAudioSource(AudioSource.Camcorder);
        media_recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        CamcorderProfile cpLow = CamcorderProfile.get(CamcorderProfile.QUALITY_480P);
        media_recorder.setProfile(cpLow);
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
        setTiming();
        isVideoRecording = true;
        takePictureImageView.setImageResource(R.drawable.play_video_icon);
        closeBtn.setVisibility(View.INVISIBLE);
        flipImageView.setVisibility(View.INVISIBLE);
        doneImageView.setVisibility(View.GONE);
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
                        VideoCommentsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (count_time == 10) {
                                    timerTextView.setText("00:" + count_time);
                                    timer.cancel();
                                    timer = null;
                                    stopVideoRecording(true);
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
            if (flag == false) {

                Utilities.showlongToast(VideoCommentsActivity.this, "Video length should be between 2-10 seconds");
                timerTextView.setVisibility(View.GONE);
                takePictureImageView.setImageResource(R.drawable.ic_camerabutton);
                closeBtn.setVisibility(View.VISIBLE);
                flipImageView.setVisibility(View.VISIBLE);
                media_recorder.stop();
                media_recorder.release();
                muteImageView.setVisibility(View.GONE);
                mute = false;
                muteImageView.setImageResource(R.drawable.ic_unmute);
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
                    doneImageView.setVisibility(View.VISIBLE);
                    muteImageView.setVisibility(View.VISIBLE);
                    flashImageView.setVisibility(View.GONE);
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

        flipImageView.setVisibility(View.INVISIBLE);
        timerTextView.setVisibility(View.GONE);
        muteImageView.setVisibility(View.VISIBLE);
        takePictureImageView.setImageResource(R.drawable.play_video_btn);
        doneImageView.setVisibility(View.VISIBLE);
        flashImageView.setVisibility(View.GONE);
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


    private void saveToSdcard(byte[] data, File file) {
        try {
            FileOutputStream outStream = new FileOutputStream(file);
            outStream.write(data);
            outStream.close();
            //outStream = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void trasnposeVideo() {
        String outputPath = FileUtils.createDirInSdCard("rotated_" + System.currentTimeMillis());
        String cmd = "";
        if (mute){
            cmd = "-y -i " + TTApplication.recordedVideoPath + " -metadata:s:v:0 rotate=90 -preset ultrafast -an " + outputPath;//
        }else {
            cmd = "-y -i " + TTApplication.recordedVideoPath + " -metadata:s:v:0 rotate=90 -preset ultrafast " + outputPath;//
        }
        String[] command = cmd.split(" ");
        if (command.length != 0) {
            execFFmpegBinary(command, outputPath);
        } else {
            //Toast.makeText(MainActivity.this, getString(R.string.empty_command_toast), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setProgressBar() {

    }

    @Override
    public void dismiss() {

    }


    private void execFFmpegBinary(final String[] command, final String outputPath) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    TTApplication.rotatedVideoPath = TTApplication.recordedVideoPath;
                    uploadVideo();
                }

                @Override
                public void onSuccess(String s) {
                    TTApplication.rotatedVideoPath = outputPath;
                    uploadVideo();
                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg " + command);
                }

                @Override
                public void onStart() {
                    //outputLayout.removeAllViews();
                    Log.d(TAG, "Started command : ffmpeg " + command);
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg " + command);
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
            uploadVideo();
        }
    }

    private void uploadVideo() {
        ProgressUtility.dismissProgress();
        Intent intent = new Intent();
        intent.putExtra("recordedPath", TTApplication.rotatedVideoPath);
        setResult(RESULT_OK, intent);
        finish();
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
