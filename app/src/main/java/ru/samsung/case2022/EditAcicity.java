package ru.samsung.case2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class EditAcicity extends AppCompatActivity {

    String s;

    EditText editText;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        position = (int) getIntent().getSerializableExtra("position");
        editText = (EditText) findViewById(R.id.editText);
    }


    public void updateText(View view) {
        s = String.valueOf(editText.getText());
        if (Objects.equals(s, "")) {
            Toast.makeText(this, "Пустой ввод!", Toast.LENGTH_SHORT).show();
        } else {
            DBJson.buys.set(position, s);
            DBJson.save();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    public void deleteItem(View view) {
        DBJson.removeByIndex(position);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}