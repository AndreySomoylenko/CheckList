package ru.samsung.case2022.db;

import android.content.Context;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.samsung.case2022.ui.RootActivity;

/**
 * The BuysManager class
 * @author Philipp Schepnov ismail velidzhanov
 * This class is used to store list of buys for the whole project
 */

public class BuysManager {
    /**
     * List of products for the whole project
     * Creates ones at app startup
     */
    public static List<String> buys = new ArrayList<>();
    public static List<String> bag = new ArrayList<>();




    public static List<String> possibleItems = new ArrayList<>();
    public static Map<String, Money> prices = new HashMap<>();

    public static Money sum;

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("items.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static void getMapFromJSON(Context context) {
        try {
            JSONArray globalArr = new JSONArray(loadJSONFromAsset(context));

            for (int i = 0; i < globalArr.length(); i++) {
                JSONArray arr = globalArr.getJSONArray(i);
                String name = arr.getString(0);
                int rub = Integer.parseInt(arr.getString(1));
                int kop = Integer.parseInt(arr.getString(2));
                prices.put(name, new Money(rub, kop));
                possibleItems.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}