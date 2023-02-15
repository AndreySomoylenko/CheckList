package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.MainActivity.db;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.R;

public class EditAcicity extends AppCompatActivity {

    String s;

    EditText editText;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        position = (int) getIntent().getSerializableExtra("position");
        editText = findViewById(R.id.editProductName);
        editText.setText(BuysManager.buys.get(position));
    }


    public void updateText(View view) {
        s = String.valueOf(editText.getText());
        if (Objects.equals(s, "")) {
            Toast.makeText(this, "Пустой ввод!", Toast.LENGTH_SHORT).show();
        } else {
            BuysManager.buys.set(position, s);
            db.save();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void deleteItem(View view) {
        db.removeByIndex(position);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}