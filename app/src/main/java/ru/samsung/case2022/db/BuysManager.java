package ru.samsung.case2022.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public final Map<String, Float> prices= new HashMap<>();
    {
        prices.put("biscuits", 29.901f);
        prices.put("broccoli", 249.901f);
        prices.put("cheese", 359.901f);
        prices.put("coffee", 489.901f);
        prices.put("curd", 144.491f);
        prices.put("dough", 144.001f);
        prices.put("milk", 61.991f);
        prices.put("pancakes", 140.801f);
        prices.put("sourcream", 74.901f);
        prices.put("tea", 506.801f);
    }

    public static float sum;
}