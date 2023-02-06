package ru.samsung.case2022;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }
    public void openDB() {
        db = dbHelper.getWritableDatabase();
    }
    public void insertNewUser() {
        db.execSQL("INSERT INTO users (lists) VALUES ('')");
    }
    public List<String> getList(int id) {
        List<String> res = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM users WHERE id=" + Integer.toString(id), null);
        if (cur.moveToNext()) {;
            res.add(cur.getString(1));
        }
        cur.close();
        return res;
    }

    public void updateList(int id, String newList) {
        db.execSQL("UPDATE users SET lists='" + newList + "' WHERE id=" + Integer.toString(id));
    }


    public void closeDB() {
        dbHelper.close();
    }
}
