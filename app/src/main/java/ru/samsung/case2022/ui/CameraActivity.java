package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.syncApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.ServerDB;
import ru.samsung.case2022.tensorflow.TFLiteInterpreter;

/**
 * The Camera Activity
 * @author Ismail Velidzhanov
 * @version 1.0
 * This is the screen with image preview, recognize and cancel buttons
 */

public class CameraActivity extends AppCompatActivity {

    /**
     * ImageView for user to see preview of image
     */
    ImageView image;

    AppDao appDao;
    /**
     * Button which start recognition of image
     */
    FloatingActionButton recognize;

    DBJson db;
    /**
     * Button which cancel recognition if user doesn't like photo
     */
    FloatingActionButton cancel;
    /**
     * Variable that stores the image user takes
     */
    Bitmap bitmap;

    public static ActionBar bar;

    /**
     * Start this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        recognize = findViewById(R.id.recognize);
        cancel = findViewById(R.id.cancel);
        image = findViewById(R.id.preview);
        appDao = new AppDao(this);
        db = new DBJson(this);
        bitmap = RootActivity.bitmap;
        //Check if the Bitmap has wrong rotation. If has, change rotation
        if (bitmap.getWidth() > bitmap.getHeight()) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        image.setImageBitmap(bitmap);
        //Set Listener for the cancel button
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });
        //Set Listener for the recognize button
        recognize.setOnClickListener(v -> {
            String s = recognize();
            db.removeByName(s);
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

            if (appDao.getLogin() != "" && syncApi != null) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            syncApi.sync().execute();
                        } catch (IOException ignored) {}
                    }
                }.start();
            }
            db.save();
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        });
        if (!appDao.getLogin().equals("")) {
            getSupportActionBar().setTitle(appDao.getName());
        }
        bar = getSupportActionBar();
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }
    }

    /**
     * We use this method to recognize product from image
     * @return name of the product
     */
    private String recognize() {
        try {
            TFLiteInterpreter tf = new TFLiteInterpreter(getApplicationContext());
            bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
            float[] output = tf.runInference(bitmap);
            String a;
            a = tf.getResult(output);
            return a;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}