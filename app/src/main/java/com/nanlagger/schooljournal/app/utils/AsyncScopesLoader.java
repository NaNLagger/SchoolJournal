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
public class AsyncScopesLoader extends AsyncLoader {

    public AsyncScopesLoader(TaskCallback taskCallback) {
        super(taskCallback);
        info = "scopes";
        typeLoader = 3;
    }

    @Override
    protected boolean jsonParse(String response) {
        try {
            JSONArray array = new JSONArray(response);
            SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
            db.delete("scopes", null, null);
            for (int i=0; i<array.length(); i++) {
                JSONObject main = array.getJSONObject(i);
                Integer lesson_id = main.getInt("lesson_id");
                Integer scopes = main.getInt("scope");
                ContentValues values = new ContentValues();
                values.put("lesson_id", lesson_id);
                values.put("scope", scopes);
                long z = db.insert("scopes", null, values);
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
