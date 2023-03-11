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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
                ServerDB.checkRegister(login).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        boolean dataCorrect = response.body().toString() == "1";
                        if (dataCorrect) {
                            SharedPreferences prefs = getSharedPreferences("app_pref", MODE_PRIVATE);
                            prefs.edit().putString("login", login).apply();
                            Intent intent = new Intent(RegisterActivity.this, RootActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Неправильные данные", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        ServerDB.showConnectionError(RegisterActivity.this);
                    }
                });
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