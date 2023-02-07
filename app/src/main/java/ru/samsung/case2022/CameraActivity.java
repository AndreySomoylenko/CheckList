package ru.samsung.case2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
            DoRecognize doo = new DoRecognize();
            doo.execute();
            /*Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();*/
        });
    }

    private void recognize() {
        try {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd("model.tflite");
            long a = assetFileDescriptor.getStartOffset();
            long b = assetFileDescriptor.getLength();
            FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
            FileInputStream stream = new FileInputStream(fileDescriptor);
            TFLiteInterpreter tf = new TFLiteInterpreter(stream, a, b);
            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            float[] output = tf.runInference(bitmap);
            int ans = tf.getResult(output);
            recognize.setText(Integer.toString(ans));
            tf.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    class DoRecognize extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            recognize();
            return null;
        }
    }
}