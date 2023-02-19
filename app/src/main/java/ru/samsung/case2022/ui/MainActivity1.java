package ru.samsung.case2022.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.R;
import ru.samsung.case2022.db.AppDao;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.DBJson;
import ru.samsung.case2022.db.ServerDB;
import ru.samsung.case2022.retrofit.RetrofitClient;

public class MainActivity1 extends AppCompatActivity {


    private ServerDB db;
    private AppDao appDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        appDao = new AppDao(this);
        Intent intent = new Intent(this, RootActivity.class);

        Call<List<String>> call = RetrofitClient.getInstance().getApi().getList(appDao.getLogin());
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                BuysManager.buys =  response.body();
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                DBJson db = new DBJson();
                db.init(getApplicationContext());
                startActivity(intent);
            }
        });
    }
}