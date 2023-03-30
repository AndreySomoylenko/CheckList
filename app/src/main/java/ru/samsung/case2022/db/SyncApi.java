package ru.samsung.case2022.db;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface SyncApi {
    Call<ResponseBody> sync();

    Call<List<String>> getList();
}
