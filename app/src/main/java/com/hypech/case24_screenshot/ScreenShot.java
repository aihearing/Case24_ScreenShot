package com.hypech.case24_screenshot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenShot {

    public static Bitmap ssRootView(View view) {
        View v = view.getRootView();
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        Bitmap b = Bitmap.createBitmap(v.getDrawingCache());
        return b;
    }

    public static Bitmap ssWindow(Activity activity) {
        View v = activity.getWindow().getDecorView();
        v.setDrawingCacheEnabled(true);
        Bitmap b = v.getDrawingCache();
        return b;
    }

    public static Uri saveImageToGallery(Bitmap bmp, Activity activity) {
        Uri uri = null;

        File appDir = new File(getDCIM());      //DCIM path
        if (!appDir.exists()) appDir.mkdir();

        String date = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss").format(new Date());
        String fileName = "sv" + date + ".jpg";
        File file = new File(appDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            uri = FileProvider.getUriForFile(activity, "com.hypech2.fileProvider", file);
            Log.e("-------", String.valueOf(uri));
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+getDCIM())));
        return uri;
    }

    // get DCIM path
    private static String getDCIM() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return "";
        }

        String path = Environment.getExternalStorageDirectory().getPath() + "/dcim/Screenshots/";
        if (new File(path).exists()) return path;
        path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Screenshots/";

        File file = new File(path); //, System.currentTimeMillis() + ".jpg");
        if(!file.exists()){
            if (!file.mkdirs()){
                return "";
            }
        }
        return path;
    }

    public static void ssShare(Uri uri, Activity activity) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);// putting uri of image to be shared
        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");// adding text to share
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");// Add subject Here
        intent.setType("image/*");// setting type to image
        activity.startActivity(Intent.createChooser(intent, "Share Via"));// calling startactivity() to share
    }

}