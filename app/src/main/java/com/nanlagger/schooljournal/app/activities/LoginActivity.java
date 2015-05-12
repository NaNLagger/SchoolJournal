package com.nanlagger.schooljournal.app.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.utils.AsyncLogin;
import com.nanlagger.schooljournal.app.utils.DBHelper;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
                loginTask.execute(login.getText().toString(), password.getText().toString());
            }
        });
    }

    private void showMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
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
}
