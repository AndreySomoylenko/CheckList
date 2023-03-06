package ru.samsung.case2022.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.ServerDB;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        Button loginBtn = findViewById(R.id.registerBtn);
        TextView hasAccount = findViewById(R.id.hasAccount);
        FloatingActionButton back = findViewById(R.id.back_register);
        loginBtn.setOnClickListener(v -> {
            String name = ((EditText)findViewById(R.id.nameInput)).getText().toString();
            String login = ((EditText)findViewById(R.id.loginInput)).getText().toString();
            String pass = ((EditText)findViewById(R.id.passInput)).getText().toString();
            if (login.equals("") || pass.equals("") || name.equals("")) {
                Toast.makeText(this,"Введите данные", Toast.LENGTH_SHORT).show();
            } else {
                boolean dataCorrect = !ServerDB.checkRegister(login);
                if (dataCorrect) {
                    SharedPreferences prefs = getSharedPreferences("app_pref", MODE_PRIVATE);
                    prefs.edit().putString("login", login).apply();
                    Intent intent = new Intent(this, RootActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Неправильные данные", Toast.LENGTH_LONG).show();
                }
            }
        });

        hasAccount.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
            finish();
        });
    }
}