package com.nanlagger.schooljournal.app.utils;

import android.os.AsyncTask;
import com.nanlagger.schooljournal.app.activities.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NaNLagger on 10.05.15.
 *
 * @author Stepan Lyashenko
 */
public class AsyncLogin extends AsyncTask<String, Integer, Boolean> {
    private final String URL_SERVER = "http://elenastuly.ru.xn--80aauktf0a4f.xn--80aswg/yii/SchoolJournal/index.php?r=export/register";
    private LoginActivity activity;
    private String errorString = "";

    public AsyncLogin(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String jsonResponse = null;
        String login = "";
        String password = "";
        String reg_id = "";
        try {
            login = params[0];
            password = params[1];
            reg_id = params[2];
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        try {
            jsonResponse = HttpClientWrapper.simpleGet(URL_SERVER + "&username=" + login + "&password=" + password + "&reg_id=" + reg_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParse(jsonResponse);
    }

    private boolean jsonParse(String response) {
        try {
            JSONObject main = new JSONObject(response);
            return main.getString("login").equalsIgnoreCase("success");
        } catch (JSONException e) {
            errorString = "Incorrect login or password";
            return false;
        } catch (NullPointerException e) {
            errorString = "Network error";
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success) {
            activity.accessTrue();
        } else {
            activity.showError(errorString);
        }
    }
}
