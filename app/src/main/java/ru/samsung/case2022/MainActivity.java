package ru.samsung.case2022;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button scan;
    Button add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan = findViewById(R.id.scan);
        add = findViewById(R.id.add);
        scan.setOnClickListener(v -> {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try{
                startActivityForResult.launch(takePhotoIntent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(this, "Камера не обнаружена", Toast.LENGTH_SHORT).show();
            }
        });
        add.setOnClickListener(v -> {
            Intent add_intent = new Intent(this, AddActivity.class);
            startActivity(add_intent);
        });
    }
    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                    Intent data = result.getData();
                    Bundle extras = data.getExtras();
                    Bitmap bitmap = (Bitmap) extras.get("data");
                    Intent intent = new Intent(this, CameraActivity.class);
                    intent.putExtra("BitmapImage", bitmap);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Ошибка подключения к камере", Toast.LENGTH_SHORT).show();
                }
            }
    );
}
