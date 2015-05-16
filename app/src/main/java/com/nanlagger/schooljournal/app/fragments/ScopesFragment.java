package com.nanlagger.schooljournal.app.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.activities.MainActivity;
import com.nanlagger.schooljournal.app.adapters.JournalsListAdapter;
import com.nanlagger.schooljournal.app.entities.*;
import com.nanlagger.schooljournal.app.utils.TaskCallback;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 12.05.15.
 *
 * @author Stepan Lyashenko
 */
public class ScopesFragment extends Fragment implements TaskCallback {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ExpandableListView journalsListView;
    private ArrayList<Journal> entity;
    private JournalsListAdapter adapter;

    public static ScopesFragment newInstance(int sectionNumber) {
        ScopesFragment fragment = new ScopesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScopesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scopes, container, false);
        journalsListView = (ExpandableListView) rootView.findViewById(R.id.journalsListView);
        adapter = new JournalsListAdapter(getActivity(), entity = JournalEntities.getInstance().getJournals());
        journalsListView.setAdapter(adapter);
        return rootView;
    }

    public void update() {
        entity.removeAll(entity);
        entity.addAll(JournalEntities.getInstance().getJournals());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void callback(int loader) {
        switch (loader) {
            case 1:
                JournalEntities.getInstance().flagEdit = false;
                break;
            case 2:
                LessonEntities.getInstance().flagEdit = false;
                break;
            case 3:
                ScopeEntities.getInstance().flagEdit = false;
                break;
        }
        if (!JournalEntities.getInstance().flagEdit && !LessonEntities.getInstance().flagEdit && !ScopeEntities.getInstance().flagEdit)
            update();
    }
}
