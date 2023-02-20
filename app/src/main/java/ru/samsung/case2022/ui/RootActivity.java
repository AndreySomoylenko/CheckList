package ru.samsung.case2022.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.samsung.case2022.R;
import ru.samsung.case2022.adapters.CustomAdapter;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;

/**
 * The RootActivity
 * @author Ismail Velidzhanov Philipp Shepnov
 * @version 1.0
 * It is the first screen the user see
 * In this screen user can manage his list of products
 */
public class RootActivity extends AppCompatActivity implements CustomAdapter.OnNoteListener {

    /**
     * Button which starts camera
     */
    Button scan;

    /**
     * Button which open AddActivity to add element to list
     */
    Button add;

    /**
     * Recyclerview which shows list of products
     */
    RecyclerView recycler;

    /**
     * Path for imageFile
     */
    private String pictureImagePath = "";

    /**
     * Bitmap to save user's photo
     */
    public static Bitmap  bitmap;

    /**
     * Class which help us to manage list of products
     */
    public static DBJson db;

    /**
     * Method to start this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
         No night mode
         */
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recycler = findViewById(R.id.recycler);
        scan = findViewById(R.id.scan);
        add = findViewById(R.id.add);
        db = new DBJson();
        db.init(this);
        // Listener for scan button
        scan.setOnClickListener(v -> {
            takePicture();
        });
        // Listener for add button
        add.setOnClickListener(v -> {
            Intent add_intent = new Intent(this, AddActivity.class);
            startActivity(add_intent);
        });
    }

    /**
     * Method to start taking picture if we can use camera
     */
    private void takePicture() {
        try{
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = timeStamp + ".jpg";
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
                File file = new File(pictureImagePath);
                Uri outputFileUri = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", file);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            } catch (Exception ignored) {

            } finally {
                someActivityResultLauncher.launch(cameraIntent);
            }
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "Камера не обнаружена", Toast.LENGTH_SHORT).show();
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                /**
                 * Method to take user's photo
                 * @param result
                 */
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        try {
                            File imgFile = new File(pictureImagePath);
                            // Check if file exists
                            if (imgFile.exists()) {
                                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            } else {
                                assert data != null;
                                Bundle extras = data.getExtras();
                                bitmap = (Bitmap) extras.get("data");
                            }
                        } catch (Exception e) {
                            assert data != null;
                            Bundle extras = data.getExtras();
                            bitmap = (Bitmap) extras.get("data");
                        } finally {
                            Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });

    /**
     * This method is called after onCreate method
     * In this method we set Adapter for RecyclerView
     */
    @Override
    protected void onResume() {
        super.onResume();
        CustomAdapter adapter = new CustomAdapter(BuysManager.buys, this, this);
        recycler.setAdapter(adapter);
    }

    /**
     *Method to catches user's onItemClick
     * @param position index of item in the list
     */
    @Override
    public void OnNoteClick(int position) {
        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    /**
     * This method sets menu to AppBar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This method catches user's click on menu button
     * @param item button on the menu which user clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.log_in:
                Toast.makeText(this, "Yes", Toast.LENGTH_SHORT).show();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * This method is called when user close application
     * In this method we save user's list to GetSharedPreferences
     */
    @Override
    protected void onStop() {
        super.onStop();
        db.save();
    }
}
