package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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

        editText = findViewById(R.id.editProductName);
        back = findViewById(R.id.back_add);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Method, which called when you clicked on button "Добавить"
     * @param view
     */

    public void updateText(View view) {
        s = String.valueOf(editText.getText());
        if (Objects.equals(s, "")) {
            Toast.makeText(this, "Пустой ввод!", Toast.LENGTH_SHORT).show();
        } else {
            db.add(s);

            (new ServerDB(getApplicationContext())).sync((new Gson()).toJson(BuysManager.buys)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    RootActivity.bar.setSubtitle("");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    RootActivity.bar.setSubtitle("Нeт подключения к интернету");
                }
            });
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        }
    }
}