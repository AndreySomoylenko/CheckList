package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.R;

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


    /**
     * Position of element, which we clicked on
     */
    int position;

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
        editText.setText(BuysManager.buys.get(position));
    }

    /**
     * Method, which called when you clicked button "Переименовать"
     * @param view
     */

    public void updateText(View view) {
        s = String.valueOf(editText.getText());
        if (Objects.equals(s, "")) {
            Toast.makeText(this, "Пустой ввод!", Toast.LENGTH_SHORT).show();
        } else {
            BuysManager.buys.set(position, s);
            db.save();
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
        Intent intent = new Intent(this, RootActivity.class);
        startActivity(intent);
        finish();
    }
}