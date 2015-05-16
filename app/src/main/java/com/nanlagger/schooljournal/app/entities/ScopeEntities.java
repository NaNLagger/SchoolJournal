package com.nanlagger.schooljournal.app.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nanlagger.schooljournal.app.utils.DBHelper;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 13.05.15.
 *
 * @author Stepan Lyashenko
 */
public class ScopeEntities {

    private String tableName = "scopes";
    public Boolean flagEdit = false;
    private ArrayList<Scope> dump;
    static private ScopeEntities instance = null;

    public static ScopeEntities getInstance() {
        if (instance == null) {
            instance = new ScopeEntities();
        }
        return instance;
    }

    public ArrayList<Scope> getScopes() {
        if (flagEdit)
            return dump;
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        ArrayList<Scope> result = new ArrayList<Scope>();
        while (cursor.moveToNext()) {
            result.add(new Scope(cursor.getInt(cursor.getColumnIndex("scope")), cursor.getInt(cursor.getColumnIndex("lesson_id"))));
        }
        db.close();
        return result;
    }

    public ArrayList<Scope> getScopes(int lesson_id) {
        ArrayList<Scope> result = new ArrayList<Scope>();
        if (flagEdit) {
            for (Scope scope : dump) {
                if (scope.lesson_id == lesson_id)
                    result.add(scope);
            }
            return result;
        }
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE lesson_id = " + lesson_id, null);
        while (cursor.moveToNext()) {
            result.add(new Scope(cursor.getInt(cursor.getColumnIndex("scope")), cursor.getInt(cursor.getColumnIndex("lesson_id"))));
        }
        db.close();
        return result;
    }

    public void save() {
        dump = getScopes();
        flagEdit = true;
    }
}
