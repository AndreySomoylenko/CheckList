package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.appDao;
import static ru.samsung.case2022.ui.RootActivity.db;
import static ru.samsung.case2022.ui.RootActivity.syncApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.R;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.ServerDB;

/**
 * The EditActivity
 * @author Dmitry Kovalchuk
 * The screen of renaming and deleting elements
 */

public class EditActivity extends AppCompatActivity {

    /**
     * Widget to edit name of product
     */
    EditText editText;

    /**
     * String which we get from editText
     */
    String s;

    public static ActionBar bar;


    /**
     * Position of element, which we clicked on
     */
    String name;

    int count;

    FloatingActionButton back;

    FloatingActionButton plusBtn, minusBtn;
    TextView counterView;

    public static final int MAX_COUNTER_ELEMENTS = 20;

    /**
     * Method to start this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        name = getIntent().getSerializableExtra("name").toString();
        editText = findViewById(R.id.editProductName);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        counterView = findViewById(R.id.counter);
        counterView.setText(String.valueOf(Collections.frequency(BuysManager.buys, name)));
        editText.setText(name);
        back = findViewById(R.id.back_edit);
        count = Integer.parseInt(counterView.getText().toString());
        if (count == 1) {
            minusBtn.setEnabled(false);
        }
        if (count == MAX_COUNTER_ELEMENTS) {
            plusBtn.setEnabled(false);
        }
        back.setOnClickListener(v -> {
            new Thread() {
                @Override
                public void run() {
                    try {
                        syncApi.sync().execute();
                    } catch (IOException ignored) {}
                }
            }.start();
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });

        plusBtn.setOnClickListener(v -> {
            count++;
            if (count == MAX_COUNTER_ELEMENTS) {
                plusBtn.setEnabled(false);
            }
            if (!minusBtn.isEnabled()) minusBtn.setEnabled(true);
            counterView.setText(String.valueOf(count));
            BuysManager.buys.add(name);
            db.save();
            if (appDao.getLogin() != "") {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            syncApi.sync().execute();
                        } catch (IOException ignored) {}
                    }
                }.start();
            }

        });

        minusBtn.setOnClickListener(v -> {
            count--;
            if (count == 1) {
                minusBtn.setEnabled(false);
            }
            if (!plusBtn.isEnabled()) plusBtn.setEnabled(true);
            counterView.setText(String.valueOf(count));
            BuysManager.buys.remove(name);
            db.save();
            if (appDao.getLogin() != "") {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            syncApi.sync().execute();
                        } catch (IOException ignored) {}
                    }
                }.start();
            }
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
     * Method, which called when you clicked button "Переименовать"
     * @param view
     */

    public void updateText(View view) {
        s = String.valueOf(editText.getText()).strip();
        if (Objects.equals(s, "")) {
            Toast.makeText(this, getString(R.string.empty_input), Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < BuysManager.buys.size(); i++) {
                if (Objects.equals(name, BuysManager.buys.get(i))) BuysManager.buys.set(i, s);
            }
            db.save();
            if (appDao.getLogin() != "") {
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

    /**
     * Method, which called when you clicked button "Удалить"
     * @param view
     */

    public void deleteItem(View view) {
        db.removeByNameBag(name);
        if (appDao.getLogin() != "") {
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