package com.nanlagger.schooljournal.app.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NaNLagger on 12.05.15.
 *
 * @author Stepan Lyashenko
 */
public class AsyncJournalsLoader extends AsyncLoader {

    public AsyncJournalsLoader(TaskCallback taskCallback) {
        super(taskCallback);
        info = "journals";
        typeLoader = 1;
    }

    @Override
    protected boolean jsonParse(String response) {
        try {
            JSONArray array = new JSONArray(response);
            SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
            db.delete("journals", null, null);
            for (int i=0; i<array.length(); i++) {
                JSONObject main = array.getJSONObject(i);
                Integer journal_id = main.getInt("id");
                String subject_name = main.getJSONObject("subject").getString("name");
                ContentValues values = new ContentValues();
                values.put("journal_id", journal_id);
                values.put("subject_name", subject_name);
                long z = db.insert("journals", null, values);
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
