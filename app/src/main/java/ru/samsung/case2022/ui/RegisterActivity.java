package ru.samsung.case2022.ui;

import static ru.samsung.case2022.ui.RootActivity.appDao;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.R;
import ru.samsung.case2022.db.ServerDB;
import ru.samsung.case2022.retrofit.models.ServerString;
import ru.samsung.case2022.services.SyncService;

public class RegisterActivity extends AppCompatActivity {


    /**
     * This method calls on activity start
     * @param savedInstanceState
     */

    public static ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        Button loginBtn = findViewById(R.id.registerBtn);
        TextView hasAccount = findViewById(R.id.hasAccount);
        FloatingActionButton back = findViewById(R.id.back_register);
        loginBtn.setOnClickListener(v -> {
            String name = ((EditText)findViewById(R.id.nameInput)).getText().toString().strip();
            String login = ((EditText)findViewById(R.id.loginInput)).getText().toString().strip();
            String pass = ((EditText)findViewById(R.id.passInput)).getText().toString().strip();
            if (login.equals("") || pass.equals("") || name.equals("")) {
                Toast.makeText(this,"Введите данные", Toast.LENGTH_SHORT).show();
            }
            else if (name.length() >= 4 && name.length() < 16) {
                Toast.makeText(this, "Длина имени от 4 до 16 символов", Toast.LENGTH_SHORT).show();
            }
            else if (login.length() >= 4 && login.length() < 25) {
                Toast.makeText(this, "Длина логина от 4 до 25 символов", Toast.LENGTH_SHORT).show();
            }
            else if (pass.length() >= 5 && pass.length() < 30) {
                Toast.makeText(this, "Длина пароля от 5 до 30 символов", Toast.LENGTH_SHORT).show();
            }
            else {
                (new ServerDB(RegisterActivity.this)).checkRegister(name, login, pass).enqueue(new Callback<ServerString>() {
                    @Override
                    public void onResponse(Call<ServerString> call, Response<ServerString> response) {
                        Log.d("onResponse", response.body().str);
                        boolean dataCorrect = Objects.equals(response.body().str, "1");
                        if (dataCorrect) {
                            SharedPreferences prefs = getSharedPreferences("app_pref", MODE_PRIVATE);
                            appDao.setLogin(login);
                            appDao.setName(name);
                            startService(new Intent(RegisterActivity.this, SyncService.class));
                            Intent intent = new Intent(RegisterActivity.this, RootActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "Аккаунт существует", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerString> call, Throwable t) {

                        ServerDB.showConnectionError(RegisterActivity.this);
                        System.out.println(t.toString());
                    }
                });
            }
            bar = getSupportActionBar();
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
        if (appDao.getLogin() != "") {
            getSupportActionBar().setTitle(appDao.getName());
        }
        if (!ServerDB.hasConnection) {
            bar.setSubtitle("Нет подключения к интернету");
        }
    }
}