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
public class LessonEntities {

    private String tableName = "lessons";
    public Boolean flagEdit = false;
    private ArrayList<Lesson> dump;
    static private LessonEntities instance = null;

    public static LessonEntities getInstance() {
        if (instance == null) {
            instance = new LessonEntities();
        }
        return instance;
    }

    public ArrayList<Lesson> getLessons() {
        if (flagEdit)
            return dump;
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName, null);
        ArrayList<Lesson> result = new ArrayList<Lesson>();
        while (cursor.moveToNext()) {
            result.add(new Lesson(cursor.getInt(cursor.getColumnIndex("lesson_id")), cursor.getInt(cursor.getColumnIndex("journal_id"))));
        }
        db.close();
        return result;
    }

    public ArrayList<Lesson> getLessons(int journal_id) {
        ArrayList<Lesson> result = new ArrayList<Lesson>();
        if (flagEdit) {
            for (Lesson lesson : dump) {
                if (lesson.journal_id == journal_id)
                    result.add(lesson);
            }
            return result;
        }
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE journal_id = " + journal_id, null);
        while (cursor.moveToNext()) {
            result.add(new Lesson(cursor.getInt(cursor.getColumnIndex("lesson_id")), cursor.getInt(cursor.getColumnIndex("journal_id"))));
        }
        db.close();
        return result;
    }

    public void save() {
        dump = getLessons();
        flagEdit = true;
    }
}
