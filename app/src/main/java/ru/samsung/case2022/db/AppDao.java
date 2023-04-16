package ru.samsung.case2022.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;

import ru.samsung.case2022.adapters.Item;

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
    Context context;

    /**
     * Constructor of class
     * Is used to initialize spref
     * @param context is the application context
     */
    public AppDao(Context context) {
        spref = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    /**
     * This method is used to write list of buys to Shared Preferences
     * @param strings is th list of products to be written in Shared Preferences
     */
    public void putList(List<Item> strings) {
        // Creates class to convert list to json format string
        Gson gson = new Gson();
        String json = gson.toJson(strings);
        // Updates Shared Preferences and saves
        spref
                .edit()
                .putString("items", json)
                .apply();
    }

    public void putBagList(List<Item> strings) {
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
    public List<Item> getList() {
        // Creates class to convert json format string to list of strings
        Gson gson = new Gson();
        String json = spref.getString("items", null);
        // Defines type of structure to which to convert from string
        Type listType = new TypeToken<List<Item>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    

    public List<Item> getBagList() {
        // Creates class to convert json format string to list of strings
        Gson gson = new Gson();
        String json = spref.getString("bag", null);
        // Defines type of structure to which to convert from string
        Type listType = new TypeToken<List<Item>>(){}.getType();
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


    public String getLang() {
        return spref.getString("lang", "default");
    }



    public void setLocale(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public void setLocale(){
        Locale locale = new Locale(getLang());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}