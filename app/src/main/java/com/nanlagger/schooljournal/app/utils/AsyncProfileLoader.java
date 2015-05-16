package com.nanlagger.schooljournal.app.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.nanlagger.schooljournal.app.activities.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NaNLagger on 12.05.15.
 *
 * @author Stepan Lyashenko
 */
public class AsyncProfileLoader extends AsyncLoader {

    public AsyncProfileLoader(TaskCallback taskCallback) {
        super(taskCallback);
        info = "profile";
    }

    @Override
    protected boolean jsonParse(String response) {
        try {
            JSONObject main = new JSONObject(response);
            String name = main.getString("name");
            String class_name = main.getJSONObject("class").getString("name");
            SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            values.put("class_name", class_name);
            long z = db.update("profile", values, null, null);
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
