package com.nanlagger.schooljournal.app.utils;

import android.os.AsyncTask;
import com.nanlagger.schooljournal.app.activities.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by NaNLagger on 12.05.15.
 *
 * @author Stepan Lyashenko
 */
public class AsyncLoader extends AsyncTask<String, Integer, Boolean> {
    private final String URL_SERVER = "http://elenastuly.ru.xn--80aauktf0a4f.xn--80aswg/yii/SchoolJournal/index.php?r=export/index";
    private TaskCallback taskCallback;
    protected String errorString = "";
    protected String info = "";
    protected int typeLoader = 0;

    public AsyncLoader(TaskCallback taskCallback) {
        this.taskCallback = taskCallback;
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
            jsonResponse = HttpClientWrapper.simpleGet(URL_SERVER + "&username=" + login + "&password=" + password + "&info=" + info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParse(jsonResponse);
    }

    protected boolean jsonParse(String response) {
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        taskCallback.callback(typeLoader);
    }
}
