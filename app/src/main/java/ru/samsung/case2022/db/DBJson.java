package ru.samsung.case2022.db;

import android.content.Context;

import java.util.ArrayList;

import ru.samsung.case2022.vcs.VersionAgent;

public class DBJson implements VersionAgent {

    public static ArrayList<String> buys;

    private static AppDao appDao;

    @Override
    public void add(String item) {
        buys.add(item);
        save();
    }


    @Override
    public boolean removeByName(String item) {
        while (buys.contains(item)) {
            buys.remove(item);
        }
        save();
        return true;
    }
    @Override
    public boolean removeByIndex(int index) {
        if (index == -1) return false;
        else {
            buys.remove(index);
            save();
            return true;
        }
    }

    @Override
    public void init(Context context) {
        appDao = new AppDao(context);
        if (appDao.getList() != null) {
            buys = appDao.getList();
        } else {
            buys = new ArrayList<>();
        }
    }

    @Override
    public void save() {
        appDao.putList(buys);
    }
}
