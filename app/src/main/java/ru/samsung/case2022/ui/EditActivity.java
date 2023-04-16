package ru.samsung.case2022.ui;

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
import ru.samsung.case2022.adapters.Item;
import ru.samsung.case2022.db.AppDao;
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


    DBJson db;

    AppDao appDao;

    /**
     * String which we get from editText
     */
    String s;

    public static ActionBar bar;


    /**
     * Position of element, which we clicked on
     */
    int position;

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
        position = (int) getIntent().getSerializableExtra("position");
        editText = findViewById(R.id.editProductName);
        db = new DBJson(this);
        plusBtn = findViewById(R.id.plusBtn);
        minusBtn = findViewById(R.id.minusBtn);
        appDao = new AppDao(this);
        counterView = findViewById(R.id.counter);
        count = BuysManager.buys.get(position).count;
        counterView.setText(String.valueOf(count));
        Item x = BuysManager.buys.get(position);
        editText.setText(BuysManager.possibleItems.contains(x.name) ? x.name.split(" ")[0] : x.name);
        back = findViewById(R.id.back_edit);
        if (count == 1) {
            minusBtn.setEnabled(false);
        }
        if (count == MAX_COUNTER_ELEMENTS) {
            plusBtn.setEnabled(false);
        }
        back.setOnClickListener(v -> {
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
        });

        minusBtn.setOnClickListener(v -> {
            count--;
            if (count == 1) {
                minusBtn.setEnabled(false);
            }
            if (!plusBtn.isEnabled()) plusBtn.setEnabled(true);
            counterView.setText(String.valueOf(count));
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
            for (String x: BuysManager.possibleItems) {
                if (s.equalsIgnoreCase(x.split(" ")[0])) {
                    s = x;
                }
            }
            Item x = BuysManager.buys.get(position);
            x.name = s;
            x.count = count;
            for (int i = 0; i < BuysManager.buys.size(); i++) {
                if (BuysManager.buys.get(i).name.equals(s)) {
                    BuysManager.buys.remove(x);
                    db.add(s, count);
                }
            }
            db.save();
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

    /**
     * Method, which called when you clicked button "Удалить"
     * @param view
     */

    public void deleteItem(View view) {
        db.removeByIndex(position);
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