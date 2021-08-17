package com.hypech.case24_screenshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private View main;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }

        main = findViewById(R.id.main);
        imageView = (ImageView) findViewById(R.id.imageView);
        Button btn = (Button) findViewById(R.id.btn);
    }

    public void click_btn(View v) {
        Bitmap bitmap = Screenshot.takescreenshotOfRootView(imageView);
        imageView.setImageBitmap(bitmap);
        main.setBackgroundColor(Color.parseColor("#999999"));
        store(bitmap, "dddd.png");
        Log.e("ddd", "ok");
    }

    public void click_btn2(View v) {
        int i = 1;
    }

    public void click_btn3(View v) {
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache();
        saveImageToGallery(bitmap, this);

        imageView.setImageBitmap(bitmap);

//        view.setDrawingCacheEnabled(false);
  //      view.destroyDrawingCache();
    }

    private static void saveImageToGallery(Bitmap bmp, Activity context) {
        File appDir = new File(getDCIM());
        if (!appDir.exists()) appDir.mkdir();

//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd", now);

        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        String fileName = "sv" + date + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+getDCIM())));
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

    public static void store(Bitmap bm, String fileName){
        final String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File dir = new File(dirPath);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dirPath, fileName);
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


