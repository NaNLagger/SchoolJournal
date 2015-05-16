package com.nanlagger.schooljournal.app.fragments;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.activities.MainActivity;
import com.nanlagger.schooljournal.app.utils.DBHelper;
import com.nanlagger.schooljournal.app.utils.TaskCallback;

/**
 * Created by NaNLagger on 12.05.15.
 *
 * @author Stepan Lyashenko
 */
public class ProfileFragment extends Fragment implements TaskCallback{
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView name;
    private TextView className;

    public static ProfileFragment newInstance(int sectionNumber) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        name = (TextView) rootView.findViewById(R.id.nameTextView);
        className = (TextView) rootView.findViewById(R.id.classTextView);
        update();
        return rootView;
    }

    public void update() {
        SQLiteDatabase db = DBHelper.getHelper(null).getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM profile", null);
        if (cursor.moveToFirst()) {
            name.setText(cursor.getString(cursor.getColumnIndex("name")));
            className.setText(cursor.getString(cursor.getColumnIndex("class_name")));
        }
        db.close();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void callback(int loader) {
        update();
    }
}
