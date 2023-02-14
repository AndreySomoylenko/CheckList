package ru.samsung.case2022.db;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.samsung.case2022.retrofit.RetrofitClient;
import ru.samsung.case2022.retrofit.models.User;
import ru.samsung.case2022.vcs.VersionAgent;

public class ServerDB implements VersionAgent {


    public static ArrayList<String> buys;

    private DBJson dbJson = new DBJson();

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
        Call<List<String>> call = RetrofitClient.getInstance().getApi().getList(getLogin());
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

    @Override
    public void init(Context context) {
        this.context = context;
    }

    @Override
    public void add(String item) {
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().addElement(getLogin(), item);
        try {
            Response<ResponseBody> resp = call.execute();
            dbJson.add(item);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean removeByName(String item) {
        dbJson.removeByName(item);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().deleteElementByName(getLogin(), item);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean removeByIndex(int index) {
        dbJson.removeByIndex(index);
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().deleteElementById(getLogin(), index);
        try {
            Response<ResponseBody> resp = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void save() {
        dbJson.save();
    }

    public String getLogin() {
        SharedPreferences spref = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
        return spref.getString("login", "");
    }
}
