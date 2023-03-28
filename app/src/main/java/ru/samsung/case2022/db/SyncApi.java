package ru.samsung.case2022.db;

import okhttp3.ResponseBody;
import retrofit2.Call;

public interface SyncApi {
    Call<ResponseBody> sync();
}
