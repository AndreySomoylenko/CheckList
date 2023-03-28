package ru.samsung.case2022.db;

import android.content.Context;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class ListSync implements SyncApi{
    ServerDB db;
    public ListSync(Context context) {
        db = new ServerDB(context);
    }
    @Override
    public Call<ResponseBody> sync() {
        return db.sync(BuysManager.buys, null);
    }

    @Override
    public Call<String> getList() {
        return null;
    }
}
