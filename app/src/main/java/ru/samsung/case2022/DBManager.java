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
    public void insertNewElement() {
        db.execSQL("INSERT INTO lists (element) VALUES ('')");
    }
    public List<String> getList() {
        List<String> res = new ArrayList<>();
        Cursor cur = db.rawQuery("SELECT * FROM lists", null);
        while (cur.moveToNext()) {;
            res.add(cur.getString(1));
        }
        cur.close();
        return res;
    }

    public void updateList(int id, String newElement) {
        db.execSQL("UPDATE lists SET element='" + newElement + "' WHERE id=" + Integer.toString(id));
    }


    public void closeDB() {
        dbHelper.close();
    }
}
