package com.nanlagger.schooljournal.app.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.utils.AsyncLogin;
import com.nanlagger.schooljournal.app.utils.DBHelper;
import com.nanlagger.schooljournal.app.utils.GcmIntentService;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by NaNLagger on 10.05.15.
 *
 * @author Stepan Lyashenko
 */
public class LoginActivity extends Activity {
    Button signIn;
    TextView login;
    TextView password;
    DBHelper dbHelper;
    ProgressDialog dialog;

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    String SENDER_ID = "865796568014";

    static final String TAG = "GCM Demo";

    GoogleCloudMessaging gcm;
    Context context;

    String regid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        NotificationManager notificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(GcmIntentService.NOTIFICATION_ID);
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            }
            Log.i(TAG, regid);
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }

        dbHelper = new DBHelper(this);
        checkAccess();
        login = (TextView) findViewById(R.id.login);
        password = (TextView) findViewById(R.id.password);
        signIn = (Button) findViewById(R.id.sign_in_button);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(LoginActivity.this, "Авторизация", "Пожайлуста подождите...", true);
                signIn.setEnabled(false);
                AsyncLogin loginTask = new AsyncLogin(LoginActivity.this);
                loginTask.execute(login.getText().toString(), password.getText().toString(), regid);
            }
        });
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        try {
            intent.putExtras(getIntent().getExtras());
        } catch (NullPointerException e) {

        }
        startActivity(intent);
        this.finish();
    }

    public void accessTrue() {
        save(login.getText().toString(), password.getText().toString());
        checkAccess();
    }

    public void showError(String message) {
        dialog.dismiss();
        signIn.setEnabled(true);
        TextView error = (TextView) findViewById(R.id.error);
        error.setText(message);
        error.setVisibility(View.VISIBLE);
    }

    public void checkAccess() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM profile", null);
        if(cursor.moveToFirst()) {
            SharedPreferences preferences = getSharedPreferences("com.nanlagger.schooljournal", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("login", cursor.getString(cursor.getColumnIndex("login")));
            editor.putString("password", cursor.getString(cursor.getColumnIndex("password")));
            editor.commit();
            showMainActivity();
        }
        db.close();
    }

    public void save(String log, String pass) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("login", log);
        values.put("password", pass);
        long z = db.insert("profile", null, values);
        db.close();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    sendRegistrationIdToBackend();
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        return getSharedPreferences(DemoActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
    }
}
