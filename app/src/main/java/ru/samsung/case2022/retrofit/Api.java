package ru.samsung.case2022.retrofit;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.samsung.case2022.retrofit.models.User;

public interface Api {
    String BASE_URL = "https://developer.android.com/develop/ui/views/components/appbar/setting-up/";

    @FormUrlEncoded
    @POST("reg_user")
    Call<ResponseBody> regUser(@Body User user);

    @GET("get_list")
    Call<List<String>> getList(@Query("login") String login);

    @FormUrlEncoded
    @POST("sync")
    Call<ResponseBody> sync(@Query("login") String login, @Query("buys") List<String> buys);

    @FormUrlEncoded
    @POST("check_login") // returns 1 if OK else 0
    Call<ResponseBody> check_login(@Query("login") String login, @Query("password") String password);

}


