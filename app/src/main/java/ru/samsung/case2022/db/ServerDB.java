package ru.samsung.case2022.db;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import ru.samsung.case2022.retrofit.RetrofitClient;
import ru.samsung.case2022.retrofit.models.User;


/**
 * The ServerDB class
 * @author Philipp Schepnov
 * This class is used to send requests and get responses from server in the context of buys list
 */

public class ServerDB {

    /**
     * Class to sync online and offline save
     */
    private DBJson dbJson = new DBJson();

    /**
     * This class is used to get data from Shared Preferences
     */
    private AppDao appDao;

    /**
     * Application context
     */
    Context context;


    /**
     * This method is used to send request to register new account
     * @param user is the user data(login, name, password)
     */
    public void regUser(User user){
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().regUser(user);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to send request to get list of buys from server by user login
     * @return list of buys
     */

    public List<String> getList() {
        Call<List<String>> call = RetrofitClient.getInstance().getApi().getList(appDao.getLogin());
        try {
            Response<List<String>> resp = call.execute();
            return resp.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to send request to update list of buys in server database
     * @param buys is the list of buys
     */

    public void sync(List<String> buys) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().sync(appDao.getLogin(), buys);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to send request to check if account in database exists
     * @param login is the user login
     * @param password is the user password
     * @return true if account exists or false if doesn’t exist
     */
    public boolean checkLogin(String login, String password) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().check_login(login, password);
        try {
            Response<ResponseBody> resp = call.execute();
            return resp.body().string().equals("1");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}