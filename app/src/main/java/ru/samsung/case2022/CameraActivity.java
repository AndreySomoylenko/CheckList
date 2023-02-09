package ru.samsung.case2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class CameraActivity extends AppCompatActivity {

    ImageView image;
    Button recognize;
    Button cancel;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        recognize = findViewById(R.id.recognize);
        cancel = findViewById(R.id.cancel);
        image = findViewById(R.id.preview);
        image.setImageBitmap(MainActivity.bitmap);
        bitmap = MainActivity.bitmap;
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        recognize.setOnClickListener(v -> {
            recognize();
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();*/
        });
    }

    private void recognize() {
        try {
            TFLiteInterpreter tf = new TFLiteInterpreter(getApplicationContext());
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
            image.setImageBitmap(bitmap);
            float[] output = tf.runInference(bitmap);
            String ans = tf.getResult(output);
            recognize.setText(ans);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}