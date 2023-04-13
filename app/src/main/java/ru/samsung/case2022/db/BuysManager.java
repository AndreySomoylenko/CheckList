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




    public static Map<String, Money> prices;
    public static Map<String, Money> prices1 = Map.of(
            "Печенье", new Money(29, 90),
            "Брокколи", new Money(249, 90),
            "Сыр", new Money(359, 90),
            "Кофе", new Money(489, 90),
            "Творог", new Money(144, 49),
            "Тесто", new Money(144, 0),
            "Молоко", new Money(61, 99),
            "Блинчики", new Money(140, 80),
            "Сметана", new Money(74, 90),
            "Чай", new Money(506, 80)
    );

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

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}