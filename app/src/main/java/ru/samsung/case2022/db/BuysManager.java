package ru.samsung.case2022.db;

import java.util.ArrayList;
import java.util.List;

/**
 * The BuysManager class
 * @author Philipp Schepnov
 * This class is used to store list of buys for the whole project
 */

public class BuysManager {
    /**
     * List of products for the whole project
     * Creates ones at app startup
     */
    public static List<String> buys = new ArrayList<>();
}