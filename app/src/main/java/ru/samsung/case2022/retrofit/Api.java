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
    @POST("add_element")
    Call<ResponseBody> addElement(@Field("login") String login, @Field("element") String element);

    @FormUrlEncoded
    @POST("delete_element_by_name")
    Call<ResponseBody> deleteElementByName(@Field("login") String login, @Field("element") String element);

    @FormUrlEncoded
    @POST("delete_element_by_id")
    Call<ResponseBody> deleteElementById(@Field("login") String login, @Field("id") Integer id);

    @GET("check_reg")  // returns '1' if registered and '0' if not registered
    Call<ResponseBody> checkIfUserRegistered(@Field("login") String login);


}


