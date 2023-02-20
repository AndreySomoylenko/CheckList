package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import ru.samsung.case2022.R;

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
            //RootActivity.db.save();
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        }
    }
}