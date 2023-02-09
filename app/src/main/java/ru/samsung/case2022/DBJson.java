package ru.samsung.case2022;

import android.content.Context;

import java.util.ArrayList;

public class DBJson {

    public ArrayList<String> buys = new ArrayList<>();

    private AppDao appDao;

    public DBJson(Context context) {
        appDao = new AppDao(context);
        if (appDao.getList() != null) {
            buys = appDao.getList();
        } else {
            buys = new ArrayList<>();
        }
    }

    public void add(String item) {
        buys.add(item);
    }

    public boolean removeByName(String item) {
        int index = buys.indexOf(item);
        if (index == -1) return false;
        else {
            buys.remove(index);
            return true;
        }
    }

    public boolean removeByIndex(int index) {
        if (index == -1) return false;
        else {
            buys.remove(index);
            return true;
        }
    }

    public void save() {
        appDao.putList(buys);
    }
}
