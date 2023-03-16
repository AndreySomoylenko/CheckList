package ru.samsung.case2022.db;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.samsung.case2022.R;
import ru.samsung.case2022.retrofit.RetrofitClient;
import ru.samsung.case2022.retrofit.models.ServerString;


/**
 * The ServerDB class
 * @author Philipp Schepnov
 * This class is used to send requests and get responses from server in the context of buys list
 */

public class ServerDB {

    /**
     * Class to sync online and offline save
     */

    /**
     * This class is used to get data from Shared Preferences
     */
    private static AppDao appDao;

    /**
     * Application context
     */
    Context context;

    public static boolean hasConnection = true;



    /**
     * Constructor of class
     * Is used to initialize AppDao
     * @param context application context
     */
    public ServerDB(Context context) {
        appDao = new AppDao(context);
    }



    /**
     * This method is used to send request to get list of buys from server by user login
     * @return Call with list of buys
     */

    public Call<List<String>> getList() {
        return RetrofitClient.getInstance().getApi().getList(appDao.getLogin());
    }

    /**
     * This method is used to send request to update list of buys in server database
     * @param buys is the list of buys
     * @return Call with simple retrofit response
     */

    public Call<ResponseBody> sync(List<String> buys) {
        return RetrofitClient.getInstance().getApi().sync(appDao.getLogin(), new Gson().toJson(buys));
    }

    /**
     * This method is used to send request to check if account in database exists
     * @param login is the user login
     * @param password is the user password
     * @return Call with Bool
     */
    public Call<ServerString> checkLogin(String login, String password) {
        return RetrofitClient.getInstance().getApi().check_login(login, password);
    }

    /**
     * This method is used to send request to check if login in database exists
     * @param lg is the user login
     * @return Call with Bool
     */
    public Call<ServerString> checkRegister(String lg, String nm, String pass) {
        return RetrofitClient.getInstance().getApi().check_register(lg, nm, pass);
    }

    public Call<ServerString> getName(String lg) {
        return RetrofitClient.getInstance().getApi().get_name(lg);
    }


    /**
     * This method is used to show Toast with connection error
     * @param context is co
     */
    public static void showConnectionError(Context context) {
        Toast.makeText(context, R.string.error_serv_req, Toast.LENGTH_SHORT).show();
    }


    /**
     * This method is used to show Toast with message from params
     * @param context application context
     * @param message message to show in Toast
     */
    public static void showConnectionError(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}