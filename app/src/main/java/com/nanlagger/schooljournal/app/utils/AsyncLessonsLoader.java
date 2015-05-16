package com.nanlagger.schooljournal.app.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NaNLagger on 13.05.15.
 *
 * @author Stepan Lyashenko
 */
public class AsyncLessonsLoader extends AsyncLoader {

    public AsyncLessonsLoader(TaskCallback taskCallback) {
        super(taskCallback);
        info = "lessons";
        typeLoader = 2;
    }

    @Override
    protected boolean jsonParse(String response) {
        try {
            JSONArray array = new JSONArray(response);
            SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
            db.delete("lessons", null, null);
            for (int i=0; i<array.length(); i++) {
                JSONObject main = array.getJSONObject(i);
                Integer lesson_id = main.getInt("id");
                Integer journal_id = main.getInt("journal_id");
                ContentValues values = new ContentValues();
                values.put("lesson_id", lesson_id);
                values.put("journal_id", journal_id);
                long z = db.insert("lessons", null, values);
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
