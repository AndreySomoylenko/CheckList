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

    public static final Map<String, Money> prices = Map.of(
        "biscuits", new Money(29, 90),
        "broccoli", new Money(249, 90),
        "cheese", new Money(359, 90),
        "coffee", new Money(489, 90),
        "curd", new Money(144, 49),
        "dough", new Money(144, 0),
        "milk", new Money(61, 99),
        "pancakes", new Money(140, 80),
        "sourcream", new Money(74, 90),
        "tea", new Money(506, 80)
        );

    public static Money sum;
}