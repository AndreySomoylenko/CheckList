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
import ru.samsung.case2022.retrofit.models.Bool;
import ru.samsung.case2022.retrofit.models.User;


/**
 * The Api interface
 * @author Philipp Schepnov
 * This interface is used to describe requests methods
 */

public interface Api {
    // Variable that contains server base url
    String BASE_URL = "https://48c5-79-139-202-28.eu.ngrok.io/";

    /**
     * This POST-request method is used to register new account on server
     * @param user class(login, name, password)
     * @return simple retrofit response
     */

    @POST("reg_user")
    Call<ResponseBody> regUser(@Body User user);

    /**
     * This GET-request method is used to get list of buys from server by user login
     * @param login is the user login
     * @return response with the list of buys
     */
    @GET("get_list")
    Call<List<String>> getList(@Query("login") String login);


    /**
     * This POST-request method is used to update list of buys on server
     * @param login is the user login
     * @param buys is the list of buys
     * @return simple retrofit response
     */
    @FormUrlEncoded
    @POST("sync")
    Call<ResponseBody> sync(@Field("login") String login, @Field("buys") List<String> buys);

    /**
     * This POST-request method is used to check if account on server exists
     * @param login is the user login
     * @param password is the user password
     * @return simple retrofit response (“1” if exists, else “0”)
     */
    @FormUrlEncoded
    @POST("check_login")
    Call<Bool> check_login(@Field("login") String login, @Field("password") String password);

    /**
     * This POST-request method is used to check if login on server exists
     * @param login is the user login
     * @return simple retrofit response (“0” if exists, else “1”)
     */


    @FormUrlEncoded
    @POST("check_register")
    Call<Bool> check_register(@Field("login") String login);

}