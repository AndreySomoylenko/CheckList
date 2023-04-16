package ru.samsung.case2022.db;

import android.content.Context;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class FullSync implements SyncApi{

    ServerDB db;

    public FullSync(Context context) {
        db = new ServerDB(context);
    }
    @Override
    public Call<ResponseBody> sync() {
        return db.sync(BuysManager.unpack(BuysManager.buys), BuysManager.unpack(BuysManager.bag));
    }

    @Override
    public Call<List<String> []> getList() {
        return db.getList(ServerDB.ListType.ALL);
    }
}
