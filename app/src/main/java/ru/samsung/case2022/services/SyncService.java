package ru.samsung.case2022.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.db.BuysManager;
import ru.samsung.case2022.db.ServerDB;
import ru.samsung.case2022.ui.AddActivity;
import ru.samsung.case2022.ui.BagActivity;
import ru.samsung.case2022.ui.CameraActivity;
import ru.samsung.case2022.ui.EditActivity;
import ru.samsung.case2022.ui.LoginActivity;
import ru.samsung.case2022.ui.RegisterActivity;
import ru.samsung.case2022.ui.RootActivity;

public class SyncService extends Service {
    public SyncService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d("hello", "hello");
            (new ServerDB(getApplicationContext())).sync(BuysManager.buys).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    ServerDB.hasConnection = true;
                    try {
                        RootActivity.bar.setSubtitle("");
                        AddActivity.bar.setSubtitle("");
                        BagActivity.bar.setSubtitle("");
                        CameraActivity.bar.setSubtitle("");
                        EditActivity.bar.setSubtitle("");
                        LoginActivity.bar.setSubtitle("");
                        RegisterActivity.bar.setSubtitle("");
                    } catch (Exception ignored) {}
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ServerDB.hasConnection = false;
                    try {
                        RootActivity.bar.setSubtitle("Нeт подключения к интернету");
                        AddActivity.bar.setSubtitle("Нeт подключения к интернету");
                        BagActivity.bar.setSubtitle("Нeт подключения к интернету");
                        CameraActivity.bar.setSubtitle("Нeт подключения к интернету");
                        EditActivity.bar.setSubtitle("Нeт подключения к интернету");
                        LoginActivity.bar.setSubtitle("Нeт подключения к интернету");
                        RegisterActivity.bar.setSubtitle("Нeт подключения к интернету");
                    } catch (Exception ignored) {}
                }
            });
            mHandler.postDelayed(this, 2000);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.postDelayed(mRunnable, 2000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}