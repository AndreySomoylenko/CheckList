package ru.samsung.case2022;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class AppDao {
    SharedPreferences spref;

    public AppDao(Context context) {
        spref = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
    }
    public void putList(ArrayList<String> strings) {
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        spref
                .edit()
                .putString("items", json)
                .apply();
    }
    public  ArrayList<String> getList() {
        Gson gson = new Gson();
        String json = spref.getString("items", null);
        Type listType = new TypeToken<ArrayList<String>>(){}.getType();
        return gson.fromJson(json, listType);
    }
}
