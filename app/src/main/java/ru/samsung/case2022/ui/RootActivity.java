package ru.samsung.case2022.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.samsung.case2022.adapters.CustomAdapter;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.R;

public class RootActivity extends AppCompatActivity implements CustomAdapter.OnNoteListener {
    Button scan;
    Button add;
    RecyclerView recycler;

    private String pictureImagePath = "";

    public static Bitmap  bitmap;


    public static DBJson db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recycler = findViewById(R.id.recycler);
        scan = findViewById(R.id.scan);
        add = findViewById(R.id.add);
        DBJson.init(this);

        scan.setOnClickListener(v -> {
            try{
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(pictureImagePath);
                Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(cameraIntent, 1);
            }catch (ActivityNotFoundException e){
                Toast.makeText(this, "Камера не обнаружена", Toast.LENGTH_SHORT).show();
            }
        });
        add.setOnClickListener(v -> {
            Intent add_intent = new Intent(this, AddActivity.class);
            startActivity(add_intent);
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
            }
            else {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                Intent intent = new Intent(this, CameraActivity.class);
                startActivity(intent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CustomAdapter adapter = new CustomAdapter(DBJson.buys, this, this);
        recycler.setAdapter(adapter);
    }

    @Override
    public void OnNoteClick(int position) {
        Intent intent = new Intent(this, EditAcicity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        DBJson.save();
    }
}
