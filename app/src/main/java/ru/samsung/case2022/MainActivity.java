package ru.samsung.case2022;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button scan;
    Button add;
    RecyclerView recycler;

    public static Bitmap  bitmap;
    public static DBManager dbManager;

    public static ArrayList<String> buys = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dbManager = new DBManager(this);
        recycler = findViewById(R.id.recycler);
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
            finish();
        });
    }
    ActivityResultLauncher<Intent> startActivityForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                    Intent data = result.getData();
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    Intent intent = new Intent(this, CameraActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(this, "Ошибка подключения к камере", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onResume() {
        super.onResume();
        dbManager.openDB();
        buys = dbManager.getList();
        CustomAdapter adapter = new CustomAdapter(buys, this);
        recycler.setAdapter(adapter);
    }
}
