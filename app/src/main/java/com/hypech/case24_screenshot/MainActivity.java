package com.hypech.case24_screenshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        }

        imageView = findViewById(R.id.image_view);
    }

    public void click_ss_show(View v) {     //1 show
        Bitmap bitmap = ScreenShot.ssRootView(imageView);
        imageView.setImageBitmap(bitmap);
    }

    public void click_ss_save(View v) {     //2 save to DCIM
        Bitmap bitmap = ScreenShot.ssWindow(this);
        imageView.setImageBitmap(bitmap);
        ScreenShot.saveImageToGallery(bitmap, this);
    }

    public void click_ss_share(View v) {   //3 share it
        Bitmap bitmap = ScreenShot.ssWindow(this);
        Uri uri = ScreenShot.saveImageToGallery(bitmap, this);
        ScreenShot.ssShare(uri, this);
    }
}