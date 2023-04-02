package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.syncApi;
import static ru.samsung.case2022.ui.RootActivity.appDao;
import static ru.samsung.case2022.ui.RootActivity.db;
import static ru.samsung.case2022.ui.RootActivity.syncApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Objects;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.ServerDB;

/**
 * The AddActivity
 *
 * @author Dmitry Kovalchuk
 * The screen of adding elements
 */

public class AddActivity extends AppCompatActivity {

    /**
     * Widget to input name of product
     */
    EditText editText;

    public static ActionBar bar;

    FloatingActionButton back;

    /**
     * String which we get from editText
     */
    String s;

    /**
     * Method to start this activity
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        if (!appDao.getLogin().equals("")) {
            getSupportActionBar().setTitle(appDao.getName());
        }
        editText = findViewById(R.id.editProductName);
        back = findViewById(R.id.back_add);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });
        bar = getSupportActionBar();
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }
    }

    /**
     * Method, which called when you clicked on button add, it adds item in list
     * @param view
     */

    public void updateText(View view) {
        s = String.valueOf(editText.getText()).strip();
        if (Objects.equals(s, "")) {
            Toast.makeText(this, getString(R.string.empty_input), Toast.LENGTH_SHORT).show();
        } else {
            db.add(s);
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
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        }
    }
}