package ru.samsung.case2022.db;

import android.content.Context;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class BagSync implements SyncApi{

    ServerDB db;

    public BagSync(Context context) {
        db = new ServerDB(context);
    }
    @Override
    public Call<ResponseBody> sync() {
        return db.sync(null, BuysManager.bag);
    }
}
