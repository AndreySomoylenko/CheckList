package ru.samsung.case2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

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
        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
        image.setImageBitmap(bitmap);
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        recognize.setOnClickListener(v -> {
            //Neural network logic
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}