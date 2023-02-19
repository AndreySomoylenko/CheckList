package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

import ru.samsung.case2022.R;
import ru.samsung.case2022.tensorflow.TFLiteInterpreter;

/**
 * The Camera Activity
 * @author ismailvelidzhanov
 * @version 1.0
 * This is the screen with image preview, recognize and cancel buttons
 */

public class CameraActivity extends AppCompatActivity {

    /**
     * ImageView for user to see preview of image
     */
    ImageView image;
    /**
     * Button wich start recognition of image
     */
    Button recognize;
    /**
     * Button wich cancel recognition if user doesn't like photo
     */
    Button cancel;
    /**
     * Variable that stores the image user takes
     */
    Bitmap bitmap;

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        recognize = findViewById(R.id.recognize);
        cancel = findViewById(R.id.cancel);
        image = findViewById(R.id.preview);
        bitmap = RootActivity.bitmap;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        image.setImageBitmap(bitmap);
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });
        recognize.setOnClickListener(v -> {
            String s = recognize();
//            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            db.removeByName(s);
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private String recognize() {
        try {
            TFLiteInterpreter tf = new TFLiteInterpreter(getApplicationContext());
            bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
            float[] output = tf.runInference(bitmap);
            return tf.getResult(output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}