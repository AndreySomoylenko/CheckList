package ru.samsung.case2022.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.samsung.case2022.db.DBHelper;

public class DBManager {
    private final Context context;
    private final DBHelper dbHelper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
    }
    public void openDB() {
        db = dbHelper.getWritableDatabase();
    }
    public void insertNewElement(String element) {
        ContentValues cv = new ContentValues();
        cv.put("element", element);
        db.insert("lists", null, cv);
    }
    public ArrayList<String> getList() {
        ArrayList<String> res = new ArrayList<>();
        Cursor cur = db.query("lists", null, null, null, null, null, null);
        while (cur.moveToNext()) {
            res.add(cur.getString(1));
        }
        cur.close();
        return res;
    }

    public void updateList(int id, String newElement) {
        db.execSQL("UPDATE lists SET element='" + newElement + "' WHERE id=" + id);
    }


    public void closeDB() {
        dbHelper.close();
    }
}
