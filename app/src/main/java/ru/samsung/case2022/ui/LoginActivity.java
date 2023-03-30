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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.R;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.ServerDB;
import ru.samsung.case2022.retrofit.models.ServerString;

public class LoginActivity extends AppCompatActivity {

    /**
     * This method calls on activity start
     * @param savedInstanceState
     */

    public static ActionBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn = findViewById(R.id.loginBtn);
        FloatingActionButton back = findViewById(R.id.back_login);
        loginBtn.setOnClickListener(v -> {
            String login = ((EditText)findViewById(R.id.loginInputLog)).getText().toString().strip();
            String pass = ((EditText)findViewById(R.id.passInputLog)).getText().toString().strip();
            if (login.equals("") || pass.equals("")) {
                Toast.makeText(this, getString(R.string.enter_data), Toast.LENGTH_SHORT).show();
            } else {
                ServerDB serverDB = new ServerDB(LoginActivity.this);
                serverDB.checkLogin(login, pass).enqueue(new Callback<ServerString>() {
                    @Override
                    public void onResponse(Call<ServerString> call, Response<ServerString> response) {
                        boolean dataCorrect = Objects.equals(response.body().str, "1");
                        if (dataCorrect) {
                            appDao.setLogin(login);
                            serverDB.getName().enqueue(new Callback<ServerString>() {
                                @Override
                                public void onResponse(Call<ServerString> call, Response<ServerString> response) {
                                    appDao.setName(response.body().str);
                                    serverDB.getList().enqueue(new Callback<List<String>>() {
                                        @Override
                                        public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                                            Log.d("LOGIN LIST", response.body().toString());
                                            BuysManager.buys = response.body();
                                            (new DBJson(getApplicationContext())).save();
                                            DBJson.start = true;
                                            Intent intent = new Intent(LoginActivity.this, RootActivity.class);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onFailure(Call<List<String>> call, Throwable t) {
                                            ServerDB.showConnectionError(LoginActivity.this, getString(R.string.error_get_list));
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<ServerString> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.wrong_data), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerString> call, Throwable t) {
                        ServerDB.showConnectionError(LoginActivity.this);
                    }
                });
            }
        });
        if (appDao.getLogin() != "") {
            getSupportActionBar().setTitle(appDao.getName());
        }
        back.setOnClickListener(v -> {
            Intent intent = new Intent(this, RootActivity.class);
            startActivity(intent);
        });
        bar = getSupportActionBar();
        if (!ServerDB.hasConnection) {
            bar.setSubtitle(getString(R.string.no_connection));
        }
    }
}