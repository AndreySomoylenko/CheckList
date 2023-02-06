package ru.samsung.case2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        recognize = findViewById(R.id.recognize);
        cancel = findViewById(R.id.cancel);
        image = findViewById(R.id.preview);
        final Bitmap[] bitmap = {(Bitmap) getIntent().getParcelableExtra("BitmapImage")};
        image.setImageBitmap(bitmap[0]);
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        recognize.setOnClickListener(v -> {
            /*try {
                AssetFileDescriptor assetFileDescriptor = getAssets().openFd("model.tflite");
                FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
                FileInputStream stream = new FileInputStream(fileDescriptor);
                TFLiteInterpreter tf = new TFLiteInterpreter(stream);
                bitmap[0] = tf.scaleBitmap(bitmap[0], 400, 400);
                float[] output = tf.runInference(bitmap[0]);
                int ans = tf.getResult(output);
                System.out.println(ans);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}