package ru.samsung.case2022.retrofit;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.samsung.case2022.retrofit.models.ServerString;


/**
 * The Api interface
 * @author Philipp Schepnov
 * This interface is used to describe requests methods
 */

public interface Api {
    // Variable that contains server base url
    String BASE_URL = "https://easyshop.pihta24.ru/";

    /**
     * This GET-request method is used to get list of buys from server by user login
     * @param login is the user login
     * @return response with the list of buys
     */
    @GET("get_list.php")
    Call<List<String>> getList(@Query("login") String login);


    /**
     * This POST-request method is used to update list of buys on server
     * @param login is the user login
     * @param buys is the JSON-string (list of buys)
     * @return simple retrofit response
     */
    @FormUrlEncoded
    @POST("sync.php")
    Call<ResponseBody> sync(@Field("login") String login, @Field("buys") String buys);

    /**
     * This POST-request method is used to check if account on server exists
     * @param login is the user login
     * @param password is the user password
     * @return Bool (“1” if exists, else “0”)
     */
    @FormUrlEncoded
    @POST("check_login.php")
    Call<ServerString> check_login(@Field("login") String login, @Field("password") String password);

    /**
     * This POST-request method is used to check if login on server exists
     * @param login is the user login
     * @return Bool (“0” if exists, else “1”)
     */
    @FormUrlEncoded
    @POST("check_register.php")
    Call<ServerString> check_register(@Field("name") String name, @Field("login") String login, @Field("password") String pass);

    @GET("get_name.php")
    Call<ServerString> get_name(@Query("login") String login);

}