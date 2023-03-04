package ru.samsung.case2022.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.samsung.case2022.R;
import ru.samsung.case2022.db.ServerDB;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.registerBtn);
        loginBtn.setOnClickListener(v -> {
            String login = ((EditText)findViewById(R.id.loginInput)).getText().toString();
            String pass = ((EditText)findViewById(R.id.passInput)).getText().toString();
            if (login.equals("") || pass.equals("")) {
                Toast.makeText(this,"Введите данные", Toast.LENGTH_SHORT).show();
            } else {
                boolean dataCorrect = !ServerDB.checkLogin(login, pass);
                if (dataCorrect) {
                    SharedPreferences prefs = getSharedPreferences("app_pref", MODE_PRIVATE);
                    prefs.edit().putString("login", login).apply();
                    Intent intent = new Intent(this, RootActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Неправильные данные", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}