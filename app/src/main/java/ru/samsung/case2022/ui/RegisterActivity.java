package ru.samsung.case2022.ui;


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

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.R;
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.ServerDB;
import ru.samsung.case2022.retrofit.models.ServerString;

public class RegisterActivity extends AppCompatActivity {


    /**
     * This method calls on activity start
     * @param savedInstanceState
     */

    public static ActionBar bar;

    AppDao appDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        Button loginBtn = findViewById(R.id.registerBtn);
        TextView hasAccount = findViewById(R.id.hasAccount);
        appDao = new AppDao(this);
        FloatingActionButton back = findViewById(R.id.back_register);
        loginBtn.setOnClickListener(v -> {
            String name = ((EditText)findViewById(R.id.nameInput)).getText().toString().strip();
            String login = ((EditText)findViewById(R.id.loginInput)).getText().toString().strip();
            String pass = ((EditText)findViewById(R.id.passInput)).getText().toString().strip();
            if (login.equals("") || pass.equals("") || name.equals("")) {
                Toast.makeText(this, getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
            }
            else if (!(name.length() >= 4 && name.length() < 16)) {
                Toast.makeText(this, getString(R.string.name_len_limit), Toast.LENGTH_SHORT).show();
            }
            else if (!(login.length() >= 4 && login.length() < 25)) {
                Toast.makeText(this, getString(R.string.login_len_limit), Toast.LENGTH_SHORT).show();
            }
            else if (!(pass.length() >= 5 && pass.length() < 30)) {
                Toast.makeText(this, getString(R.string.pass_len_limit), Toast.LENGTH_SHORT).show();
            }
            else {
                loginBtn.setClickable(false);
                (new ServerDB(RegisterActivity.this)).checkRegister(name, login, pass).enqueue(new Callback<ServerString>() {
                    @Override
                    public void onResponse(Call<ServerString> call, Response<ServerString> response) {
                        Log.d("onResponse", response.body().str);
                        boolean dataCorrect = Objects.equals(response.body().str, "1");
                        if (dataCorrect) {
                            appDao.setLogin(login);
                            appDao.setName(name);
                            new ServerDB(RegisterActivity.this).sync(BuysManager.unpack(BuysManager.buys), BuysManager.unpack(BuysManager.bag)).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    DBJson.start = true;
                                    Intent intent = new Intent(RegisterActivity.this, RootActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {

                                }
                            });
                        } else {
                            loginBtn.setClickable(true);
                            Toast.makeText(RegisterActivity.this, getString(R.string.account_exists), Toast.LENGTH_LONG).show();
                            loginBtn.setClickable(true);
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerString> call, Throwable t) {
                        loginBtn.setClickable(true);
                        ServerDB.showConnectionError(RegisterActivity.this);
                        System.out.println(t);
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
        if (!appDao.getLogin().equals("")) {
            getSupportActionBar().setTitle(appDao.getName());
        }
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }
    }
}