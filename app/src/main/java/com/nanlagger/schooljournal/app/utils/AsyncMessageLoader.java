package com.nanlagger.schooljournal.app.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NaNLagger on 15.05.15.
 *
 * @author Stepan Lyashenko
 */
public class AsyncMessageLoader extends AsyncLoader{
    public AsyncMessageLoader(TaskCallback taskCallback) {
        super(taskCallback);
        info = "messages";
    }

    @Override
    protected boolean jsonParse(String response) {
        try {
            JSONArray array = new JSONArray(response);
            SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
            //db.delete("messages", null, null);
            for (int i=0; i<array.length(); i++) {
                JSONObject main = array.getJSONObject(i);
                Integer message_id = main.getInt("id");
                String message = main.getString("massage");
                Cursor cursor = db.rawQuery("SELECT * FROM messages WHERE message_id = " + message_id, null);
                if (!cursor.moveToFirst()) {
                    ContentValues values = new ContentValues();
                    values.put("message_id", message_id);
                    values.put("message", message);
                    long z = db.insert("messages", null, values);
                }
                cursor.close();
            }
            db.close();
            return true;
        } catch (JSONException e) {
            errorString = "Incorrect login or password";
            return false;
        } catch (NullPointerException e) {
            errorString = "Network error";
            return false;
        }
    }
}
