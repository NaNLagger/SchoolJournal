package com.nanlagger.schooljournal.app.utils;

import android.os.AsyncTask;
import com.nanlagger.schooljournal.app.activities.LoginActivity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NaNLagger on 10.05.15.
 *
 * @author Stepan Lyashenko
 */
public class AsyncLogin extends AsyncTask<String, Integer, Boolean> {
    private final String URL_SERVER = "http://elenastuly.ru.xn--80aauktf0a4f.xn--80aswg/yii/SchoolJournal/index.php?r=export/index";
    private LoginActivity activity;

    public AsyncLogin(LoginActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        String jsonResponse = null;
        String login = "";
        String password = "";
        try {
            login = params[0];
            password = params[1];
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        try {
            jsonResponse = HttpClientWrapper.simpleGet(URL_SERVER + "&username=" + login + "&password=" + password);
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
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        if(success) {
            activity.accessTrue();
        } else {
            activity.showError("Incorrect login or password");
        }
    }
}
