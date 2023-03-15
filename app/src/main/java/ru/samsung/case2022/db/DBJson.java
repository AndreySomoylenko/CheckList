package ru.samsung.case2022.db;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * The DBJson class
 * @author Philipp Schepnov
 * This class is used to manage list of buys
 */

public class DBJson {

    /**
     * This class is used to get data from Shared Preferences
     */
    private static AppDao appDao;

    public static boolean start = true;

    Context context;

    public DBJson(Context context) {
        this.context = context;
    }

    /**
     * This method is used to add item in list of buys
     * @param item is the item to add
     */
    public void add(String item) {
        BuysManager.buys.add(item);
        save();
    }

    public void addToBag(String item) {
        BuysManager.bag.add(item);
        save();
    }

    public void clearBag() {
        BuysManager.bag.clear();
        save();
    }

    /**
     * This method is used to remove item from list of buys by its name
     * @param item is the item to remove
     * @return true if item deleted or false if item not found
     */
    public boolean removeByName(String item) {
        if (!BuysManager.buys.contains(item)) return false;
        // This cycle is needed to delete all elements that equal to item
        while (BuysManager.buys.contains(item)) {
            BuysManager.buys.remove(item);
            addToBag(item);
            if (BuysManager.prices.get(item) != null) {
                Money price = BuysManager.prices.get(item);
                BuysManager.sum = BuysManager.sum.plus(price);
            }
        }
        save();
        //sync();
        return true;
    }


    /**
     * This method is used to remove item from list of buys by its index
     * @param index is the index of item to remove
     * @return true if item deleted or false if index >= length of list
     */
    public boolean removeByIndex(int index) {
        if (index == -1) return false;
        else {
            BuysManager.buys.remove(index);
            save();
            //sync();
            return true;
        }
    }

    public boolean removeByIndexBag(int index) {
        if (index == -1) return false;
        else {
            BuysManager.bag.remove(index);
            save();
            return true;
        }
    }

    /**
     * This method is used to initialise this class and put list from Shared Preferences in list of buys from BuysManager
     */
    public void init() {
        appDao = new AppDao(context);
        if (appDao.getList() != null) {
            BuysManager.buys = appDao.getList();
        }
        if (appDao.getBagList() != null) {
            BuysManager.bag = appDao.getBagList();
        }
        BuysManager.sum = appDao.getSum();
    }

    /**
     * This method is used to save element of buys list to Shared Preferences via AppDao class
     */
    public void save() {
        appDao.putList(BuysManager.buys);
        appDao.putBagList(BuysManager.bag);
        appDao.putSum(BuysManager.sum.getRubles(), BuysManager.sum.getCents());
    }

    private void sync() {
        new Thread() {
            public void run() {
                try {
                    (new ServerDB(context)).sync(BuysManager.buys).execute();
                } catch (IOException ignored) {}
            }
        }.start();
    }
}