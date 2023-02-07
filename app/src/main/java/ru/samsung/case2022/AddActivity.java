package ru.samsung.case2022;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class AddActivity extends AppCompatActivity {

    String s;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editText = (EditText) findViewById(R.id.editText);
    }

    public void updateText(View view) {
        s = String.valueOf(editText.getText());

        System.out.println(s);
        if (Objects.equals(s, "")) {
            Toast.makeText(this, "Пустой ввод!", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}