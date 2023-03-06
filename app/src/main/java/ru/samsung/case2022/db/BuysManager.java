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

    public static final Map<String, Float> prices = Map.of(
        "biscuits", 29.901f,
        "broccoli", 249.901f,
        "cheese", 359.901f,
        "coffee", 489.901f,
        "curd", 144.491f,
        "dough", 144.001f,
        "milk", 61.991f,
        "pancakes", 140.801f,
        "sourcream", 74.901f,
        "tea", 506.801f
        );

    public static float sum;
}