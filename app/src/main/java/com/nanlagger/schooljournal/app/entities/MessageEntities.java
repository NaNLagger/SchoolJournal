package com.nanlagger.schooljournal.app.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nanlagger.schooljournal.app.utils.DBHelper;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 15.05.15.
 *
 * @author Stepan Lyashenko
 */
public class MessageEntities {

    private String tableName = "messages";
    public Boolean flagEdit = false;
    private ArrayList<Message> dump;
    static private MessageEntities instance = null;

    public static MessageEntities getInstance() {
        if (instance == null) {
            instance = new MessageEntities();
        }
        return instance;
    }

    public ArrayList<Message> getMessages() {
        if (flagEdit)
            return dump;
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName + " ORDER BY message_id DESC", null);
        ArrayList<Message> result = new ArrayList<Message>();
        while (cursor.moveToNext()) {
            result.add(new Message(cursor.getInt(cursor.getColumnIndex("message_id")), cursor.getString(cursor.getColumnIndex("message"))));
        }
        db.close();
        return result;
    }

    public void save() {
        dump = getMessages();
        flagEdit = true;
    }
}
