package ru.samsung.case2022.db;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.samsung.case2022.adapters.CustomAdapter;
import ru.samsung.case2022.adapters.Item;

/**
 * The DBJson class
 * @author Philipp Schepnov
 * This class is used to manage list of buys
 */

public class DBJson {

    /**
     * This class is used to get data from Shared Preferences
     */
    private  AppDao appDao;

    public static boolean start = true;
    public static boolean startServ = false;
    Context context;

    public DBJson(Context context) {
        this.context = context;
        appDao = new AppDao(context);
    }

    /**
     * This method is used to add item in list of buys
     * @param item is the item to add
     */
    public void add(String name, int count) {
        for (String x: BuysManager.possibleItems) {
            if (name.equalsIgnoreCase(x.split(" ")[0])) {
                name = x;
            }
        }
        for (int i = 0; i < BuysManager.buys.size(); i++) {
            Item x = BuysManager.buys.get(i);
            if (x.name.equals(name))  {
                x.count += count;
                save();
                return;
            }
        }
        BuysManager.buys.add(new Item(name, count));
        save();
    }

    public void addToBag(Item item) {
        for (int i = 0; i < BuysManager.bag.size(); i++) {
            Item x = BuysManager.bag.get(i);
            if (x.name.equals(item.name))  {
                x.count += item.count;
                return;
            }
        }
        BuysManager.bag.add(item);
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
    public void removeByName(String name) {
        // This cycle is needed to delete all elements that equal to item
        for (int i = 0; i < BuysManager.buys.size(); i++) {
            Item x = BuysManager.buys.get(i);
            if (x.name.equals(name)) {
                Money price = CustomAdapter.getMoneyByName(x).multiply(x.count);
                BuysManager.buys.remove(x);
                addToBag(x);
                BuysManager.sum = BuysManager.sum.plus(price);
                save();
                break;
            }
        }
        //sync();
    }




    /**
     * This method is used to remove item from list of buys by its index
     * @param position is the index of item to remove
     * @return true if item deleted or false if index >= length of list
     */
    public void removeByIndex(int position) {
        BuysManager.buys.remove(position);
        save();
        //sync();
    }

    public void removeByIndexBag(int position) {
        BuysManager.bag.remove(position);
        save();
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



//    private void sync() {
//        new Thread() {
//            public void run() {
//                try {
//                    (new ServerDB(context)).sync(BuysManager.buys).execute();
//                } catch (IOException ignored) {}
//            }
//        }.start();
//    }
}