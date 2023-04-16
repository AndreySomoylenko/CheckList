package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.syncApi;
import static ru.samsung.case2022.ui.RootActivity.syncApi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.Objects;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
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

    AppDao appDao;

    public static ActionBar bar;

    FloatingActionButton back;
    FloatingActionButton plusBtn, minusBtn;
    TextView counterView;
    int count;

    /**
     * String which we get from editText
     */
    String s;

    DBJson db;

    /**
     * Method to start this activity
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        appDao = new AppDao(this);
        if (!appDao.getLogin().equals("")) {
            getSupportActionBar().setTitle(appDao.getName());
        }
        db = new DBJson(this);
        editText = findViewById(R.id.editProductName);
        back = findViewById(R.id.back_add);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        counterView = findViewById(R.id.counter);
        count = Integer.parseInt(counterView.getText().toString());
        if (count == 1) {
            minusBtn.setEnabled(false);
        }
        if (count == EditActivity.MAX_COUNTER_ELEMENTS) {
            plusBtn.setEnabled(false);
        }
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });
        plusBtn.setOnClickListener(v -> {
            count++;
            if (count == EditActivity.MAX_COUNTER_ELEMENTS) {
                plusBtn.setEnabled(false);
            }
            if (!minusBtn.isEnabled()) minusBtn.setEnabled(true);
            counterView.setText(String.valueOf(count));
        });

        minusBtn.setOnClickListener(v -> {
            count--;
            if (count == 1) {
                minusBtn.setEnabled(false);
            }
            if (!plusBtn.isEnabled()) plusBtn.setEnabled(true);
            counterView.setText(String.valueOf(count));
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
            db.add(s, count);
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