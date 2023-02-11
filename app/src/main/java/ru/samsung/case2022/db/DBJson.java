package ru.samsung.case2022.db;

import android.content.Context;

import java.util.ArrayList;

public class DBJson {

    public static ArrayList<String> buys;

    private static AppDao appDao;

    public static void init(Context context) {
        appDao = new AppDao(context);
        if (appDao.getList() != null) {
            buys = appDao.getList();
        } else {
            buys = new ArrayList<>();
        }
    }

    public static void add(String item) {
        buys.add(item);
        save();
    }

    public static boolean removeByName(String item) {
        while (buys.contains(item)) {
            buys.remove(item);
        }
        save();
        return true;
    }

    public static boolean removeByIndex(int index) {
        if (index == -1) return false;
        else {
            buys.remove(index);
            save();
            return true;
        }
    }

    public static void save() {
        appDao.putList(buys);
    }
}
