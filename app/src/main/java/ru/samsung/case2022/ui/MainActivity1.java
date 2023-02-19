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

/**
 * The Main Activity1
 * @author IsmailVelidzhanov PhilippShepnov
 * @version 1.0
 * This will be the first screen the user see
 * In this activity we synchronized list of products with server
 */

public class MainActivity1 extends AppCompatActivity {

    /**
     * This class is used to manage user account
     */
    private ServerDB db;

    /**
     * This class is used to get data from GetSharedPreferences
     */
    private AppDao appDao;

    /**
     * Method to start this activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        appDao = new AppDao(this);
        Intent intent = new Intent(this, RootActivity.class);

        Call<List<String>> call = RetrofitClient.getInstance().getApi().getList(appDao.getLogin());
        call.enqueue(new Callback<List<String>>() {

            /**
             * This method is called when we connect to server successfully
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                BuysManager.buys =  response.body();
                startActivity(intent);
            }

            /**
             * This method is called when we can't establich connection to server. We use local database
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                DBJson db = new DBJson();
                db.init(getApplicationContext());
                startActivity(intent);
            }
        });
    }
}