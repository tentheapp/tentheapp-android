package com.nvcomputers.ten.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.loopj.android.http.Base64;
import com.nvcomputers.ten.views.TTApplication;

import okhttp3.Credentials;


public class Utils {
    private static File extStorageDirectory;

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
    //================================================

    public static Bitmap getImageBitmap(String categoryId, String imageName, String imageUrl) {
        //File file;
        extStorageDirectory = Environment.getExternalStorageDirectory();
        Bitmap myBitmap = null;

        FileInputStream fileInputStream = null;
        BufferedInputStream bufferInputStream;
        String path = extStorageDirectory + "/.TenTheApp/" + categoryId + "/" + imageName;

        try {
            Environment.getExternalStorageState();
            fileInputStream = new FileInputStream(path);
            bufferInputStream = new BufferedInputStream(fileInputStream);
            myBitmap = BitmapFactory.decodeStream(bufferInputStream);


        } catch (FileNotFoundException e) {
            Log.d("FileNotFoundException", e + "===" + e.getMessage());
            try {
                /*if (imageUrl.contains(".jpg") || imageUrl.contains(".png") || imageUrl.contains(".jpeg"))
                {*/
                Log.d("URL :", "url::" + imageUrl);
                myBitmap = convertBitmap(imageUrl);
                if (myBitmap != null) {
                    addImagesToSDcard(myBitmap, categoryId, imageName);
                } else {
                    Log.d("URL Bitmap", "URL not working==" + myBitmap);
                }
    			/*}
    			else 
    			{
    				Log.d("URL","Invalid path=="+imageUrl);
    			}*/
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        return myBitmap;
    }

    //==============================================
    //==============================================
    public static Bitmap convertBitmap(String url) {
        Bitmap myBitmap = null;
        try {
            URL myImageURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) myImageURL.openConnection();
            //String encoded = Base64.encodeToString((TTApplication.userName+":"+ TTApplication.password).getBytes(StandardCharsets.UTF_8));  //Java 8
            String encoded = Credentials.basic(TTApplication.userName, TTApplication.password);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                encoded = android.util.Base64.encodeToString((TTApplication.userName + ":" + TTApplication.password).getBytes(StandardCharsets.UTF_8), android.util.Base64.DEFAULT);
            }
            connection.setRequestProperty("Authorization", "Basic " + encoded);
            connection.setDoInput(true);
            connection.connect();

            if (connection.getResponseCode() == 404) {
                //myBitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.icon);
                // addImagesToSDcard(myBitmap);
            } else {
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

                myBitmap = resizeYourBitmap(myBitmap);
                // addImagesToSDcard(myBitmap);
            }
        } catch (Exception e) {
            Log.d("Exception", e + "==convertBitmap==" + e.getMessage());
        }
        return myBitmap;
    }

    private static Bitmap resizeYourBitmap(Bitmap yourSelectedImageBitmap) {
        Bitmap resizedBitmap = null;
        // TODO Auto-generated method stub
        if (yourSelectedImageBitmap != null) {
            //----------resize for set in imageView------------
            int width = yourSelectedImageBitmap.getWidth();
            int height = yourSelectedImageBitmap.getHeight();
            int newWidth = 100;
            int newHeight = 100;

            // calculate the scale - in this case = 0.4f
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            Matrix matrix = new Matrix();
            // resize the bit map
            matrix.postScale(scaleWidth, scaleHeight);

            resizedBitmap = Bitmap.createBitmap(yourSelectedImageBitmap, 0, 0, width, height, matrix, true);


        }
        return resizedBitmap;
    }

    //========================
    public static void addImagesToSDcard(Bitmap bitmap, String categoryId, String imageName) {
        extStorageDirectory = Environment.getExternalStorageDirectory();
        Log.d("Message::::", "addImagesToSDcard::::::::::start");
        OutputStream outStreamFile = null;
        File file = null;
        try {
            Environment.getExternalStorageState();

            file = new File(extStorageDirectory + "/.TenTheApp/" + categoryId + "/");
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(extStorageDirectory + "/.TenTheApp/" + categoryId + "/", imageName);

        } catch (Exception e) {
            Log.d("Exception", e + "==addImagesToSDcard==" + e.getMessage());
        }

        try {
            Environment.getExternalStorageState();
            outStreamFile = new FileOutputStream(file);
            Environment.getExternalStorageState();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, outStreamFile);
            outStreamFile.flush();
            outStreamFile.close();
        } catch (FileNotFoundException e1) {
            Log.d("Message::::", "FileNotFoundException::::::::::completed " + e1.getMessage());
            e1.printStackTrace();
        } catch (IOException e) {
            Log.d("Message::::", "IOException::::::::::completed " + e.getMessage());
            e.printStackTrace();
        }
        Log.d("Message::::", "addImagesToSDcard::::::::::completed ===imageName:=" + imageName);
    }
    //========================
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}