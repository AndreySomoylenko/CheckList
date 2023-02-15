package ru.samsung.case2022.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class AppDao {
    SharedPreferences spref;

    public AppDao(Context context) {
        spref = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
    }
    public void putList(List<String> strings) {
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        spref
                .edit()
                .putString("items", json)
                .apply();
    }
    public List<String> getList() {
        Gson gson = new Gson();
        String json = spref.getString("items", null);
        Type listType = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    public String getLogin() {
        return spref.getString("login", "");
    }
}
