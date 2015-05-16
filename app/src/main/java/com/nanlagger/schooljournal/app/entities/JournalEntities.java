package com.nanlagger.schooljournal.app.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nanlagger.schooljournal.app.utils.DBHelper;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 12.05.15.
 *
 * @author Stepan Lyashenko
 */
public class JournalEntities {

    private String tableName = "journals";
    public Boolean flagEdit = false;
    private ArrayList<Journal> dump;
    static private JournalEntities instance = null;

    public static JournalEntities getInstance() {
        if (instance == null) {
            instance = new JournalEntities();
        }
        return instance;
    }

    public Journal getJournal(int journal_id) {
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM journals WHERE journal_id = " + journal_id, null);
        Journal result = null;
        if (cursor.moveToFirst()) {
            result = new Journal(cursor.getInt(cursor.getColumnIndex("journal_id")), cursor.getString(cursor.getColumnIndex("subject_name")));
        }
        db.close();
        return result;
    }

    public ArrayList<Journal> getJournals() {
        if (flagEdit)
            return dump;
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM journals", null);
        ArrayList<Journal> result = new ArrayList<Journal>();
        while (cursor.moveToNext()) {
            result.add(new Journal(cursor.getInt(cursor.getColumnIndex("journal_id")), cursor.getString(cursor.getColumnIndex("subject_name"))));
        }
        db.close();
        return result;
    }

    public void save() {
        dump = getJournals();
        flagEdit = true;
    }
}
