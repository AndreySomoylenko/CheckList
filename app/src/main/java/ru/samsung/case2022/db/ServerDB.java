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

    public boolean checkIfUserRegistered(String login) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().checkIfUserRegistered(login);
        try {
            Response<ResponseBody> resp = call.execute();
            return resp.body().string().equals("1");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void init(Context context) {
        this.context = context;
        this.appDao = new AppDao(context);
    }

    public void add(String item) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().addElement(appDao.getLogin(), item);
        try {
            Response<ResponseBody> resp = call.execute();
            dbJson.add(item);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean removeByName(String item) {
        dbJson.removeByName(item);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().deleteElementByName(appDao.getLogin(), item);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public boolean removeByIndex(int index) {
        dbJson.removeByIndex(index);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().deleteElementById(appDao.getLogin(), index);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void save() {
        dbJson.save();
    }

}
