package com.nvcomputers.ten.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

//import com.bumptech.glide.load.engine.cache.MemoryCache;

import com.nvcomputers.ten.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

/**
 * Created by rkumar4 on 6/12/2017.
 */

public class CustomImageLoader {

    private Context mContext;

    MemoryCache memoryCache = new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    private Activity mActivity;

    //Context context;
    public CustomImageLoader(Context context) {
        //Make the background thead low priority. This way it will not affect the UI performance
        photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);
        fileCache = new FileCache(context);
    }

    int stub_id = R.drawable.myprofilelarge;

    public void displayImage(String categoryId, String imageName, String url, Activity activity, ImageView imageView) {
        try {

            stub_id = R.drawable.myprofilelarge;

            imageViews.put(imageView, url);
            Bitmap bitmap = memoryCache.get(url);

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                queuePhoto(categoryId, imageName, url, activity, imageView);
                imageView.setImageResource(stub_id);
                Log.d("@@@@@@ stub_id @@@@@@@@", "@@@@@@ stub_id @@@@");

            }
                /*else if(InviteAppFriendActivity.image_state==true)
                {
    	        	Log.d("@@@@@@@@@@", "@@@@@@@@@@"+InviteAppFriendActivity.image_state);
    	        	imageView.setBackgroundResource(R.drawable.no_image_default);
    	        }*/
        } catch (Exception e) {
            Log.d("@@@ Exception @@@@@", "#####" + e.getMessage());
        }


    }

    private void queuePhoto(String categoryId, String imageName, String url, Activity activity, ImageView imageView) {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
        mActivity = activity;
        photosQueue.Clean(imageView);
        PhotoToLoad p = new PhotoToLoad(categoryId, imageName, url, imageView);
        synchronized (photosQueue.photosToLoad) {
            photosQueue.photosToLoad.push(p);
            photosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        if (photoLoaderThread.getState() == Thread.State.NEW)
            photoLoaderThread.start();
    }

    public void stopThread() {
        photoLoaderThread.interrupt();
    }

    PhotosLoader photoLoaderThread = new PhotosLoader();

    class PhotosLoader extends Thread {
        public void run() {
            try {
                while (true) {
                    if (photosQueue.photosToLoad.size() == 0)
                        synchronized (photosQueue.photosToLoad) {
                            photosQueue.photosToLoad.wait();
                        }
                    if (photosQueue.photosToLoad.size() != 0) {
                        PhotoToLoad photoToLoad;
                        synchronized (photosQueue.photosToLoad) {
                            photoToLoad = photosQueue.photosToLoad.pop();
                        }
                        //Bitmap bmp=getBitmap(photoToLoad.url);
                        Bitmap bmp1 = Utils.getImageBitmap(photoToLoad.categoryid, photoToLoad.imageName, photoToLoad.url);
                        Bitmap bmp = Utils.getRoundedCornerBitmap(bmp1, 20);
                        memoryCache.put(photoToLoad.url, bmp);
                        String tag = imageViews.get(photoToLoad.imageView);
                        if (tag != null && tag.equals(photoToLoad.url)) {
                            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);
                            //Activity a = (Activity) photoToLoad.imageView.getContext();
                            // Activity a = (Activity) context;
                            mActivity.runOnUiThread(bd);
                        }
                    }
                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
            }
        }
    }

    //Task for the queue
    private class PhotoToLoad {
        public String categoryid, imageName, url;
        public ImageView imageView;

        public PhotoToLoad(String categoryId, String imageName, String u, ImageView i) {
            categoryid = categoryId;
            this.imageName = imageName;
            url = u;
            imageView = i;
        }
    }

    private Bitmap getBitmap(String url) {
        File f = fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        //from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f) {
        try {
        /*	 // clear garbage collector
            System.gc();
            Runtime.getRuntime().gc();*/

            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    PhotosQueue photosQueue = new PhotosQueue();


    //stores list of photos to download
    class PhotosQueue {
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

        //removes all instances  b of this ImageView
        public void Clean(ImageView image) {
            for (int j = 0; j < photosToLoad.size(); ) {
                if (photosToLoad.get(j).imageView == image)
                    photosToLoad.remove(j);
                else
                    ++j;
            }
        }
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;

        public BitmapDisplayer(Bitmap b, ImageView i) {
            bitmap = b;
            imageView = i;
        }

        public void run() {
            if (bitmap != null)
                imageView.setImageBitmap(bitmap);
            else
                imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
}