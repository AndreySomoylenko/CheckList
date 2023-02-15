package ru.samsung.case2022.db;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class DBJson {

    private static AppDao appDao;

    public void add(String item) {
        BuysManager.buys.add(item);
        save();
    }


    public boolean removeByName(String item) {
        while (BuysManager.buys.contains(item)) {
            BuysManager.buys.remove(item);
        }
        save();
        return true;
    }

    public boolean removeByIndex(int index) {
        if (index == -1) return false;
        else {
            BuysManager.buys.remove(index);
            save();
            return true;
        }
    }

    public void init(Context context) {
        appDao = new AppDao(context);
        if (appDao.getList() != null) {
            BuysManager.buys = appDao.getList();
        } else {
            BuysManager.buys = new ArrayList<>();
        }
    }

    public void save() {
        appDao.putList(BuysManager.buys);
    }
}
