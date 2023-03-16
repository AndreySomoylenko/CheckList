package ru.samsung.case2022.db;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * The AppDao class
 * @author Philipp Schepnov
 * This class is used to work with Shared Preferences
 */

public class AppDao {

    /**
     * This class is used to manage Shared Preferences
     */
    SharedPreferences spref;

    /**
     * Constructor of class
     * Is used to initialize spref
     * @param context is the application context
     */
    public AppDao(Context context) {
        spref = context.getSharedPreferences("app_pref", Context.MODE_PRIVATE);
    }

    /**
     * This method is used to write list of buys to Shared Preferences
     * @param strings is th list of products to be written in Shared Preferences
     */
    public void putList(List<String> strings) {
        // Creates class to convert list to json format string
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        // Updates Shared Preferences and saves
        spref
                .edit()
                .putString("items", json)
                .apply();
    }

    public void putBagList(List<String> strings) {
        // Creates class to convert list to json format string
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        // Updates Shared Preferences and saves
        spref
                .edit()
                .putString("bag", json)
                .apply();
    }

    /**
     * This method is used to get list of buys from Shared Preferences
     * @return list of buys
     */
    public List<String> getList() {
        // Creates class to convert json format string to list of strings
        Gson gson = new Gson();
        String json = spref.getString("items", null);
        // Defines type of structure to which to convert from string
        Type listType = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    

    public List<String> getBagList() {
        // Creates class to convert json format string to list of strings
        Gson gson = new Gson();
        String json = spref.getString("bag", null);
        // Defines type of structure to which to convert from string
        Type listType = new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    /**
     * This method is used to get user login from Shared Preferences
     * @return login if exists else empty string
     */

    /**
     * This method is used to get login from SharedPreferences
     * @return String (user login)
     */
    public String getLogin() {
        return spref.getString("login", "");
    }


    public String getName() { return spref.getString("name", ""); }
    public void setLogin(String login) {
        spref.edit().putString("login", login).apply();
    }

    public void setName(String name) {
        spref.edit().putString("name", name).apply();
    }

    public void putSum(int rubles, int cents) {
        spref
                .edit()
                .putInt("rubles", rubles)
                .putInt("cents", cents)
                .apply();
    }

    public Money getSum() {
        return new Money(spref.getInt("rubles", 0), spref.getInt("cents", 0));
    }
}