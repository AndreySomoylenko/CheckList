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

public class ServerDB {


    public static ArrayList<String> buys;

    private DBJson dbJson = new DBJson();
;
    private AppDao appDao;
    Context context;

    public void regUser(User user){
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().regUser(user);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getList() {
        Call<List<String>> call = RetrofitClient.getInstance().getApi().getList(appDao.getLogin());
        try {
            Response<List<String>> resp = call.execute();
            return resp.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sync(List<String> buys) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().sync(appDao.getLogin(), buys);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

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
