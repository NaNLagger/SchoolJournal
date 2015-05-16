package com.nanlagger.schooljournal.app.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.activities.MainActivity;
import com.nanlagger.schooljournal.app.adapters.MessagesListAdapter;
import com.nanlagger.schooljournal.app.entities.Message;
import com.nanlagger.schooljournal.app.entities.MessageEntities;
import com.nanlagger.schooljournal.app.utils.TaskCallback;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 15.05.15.
 *
 * @author Stepan Lyashenko
 */
public class MessagesFragment extends Fragment implements TaskCallback {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private ArrayList<Message> entity;
    private MessagesListAdapter adapter;

    public static MessagesFragment newInstance(int sectionNumber) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        ListView messagesListView = (ListView) rootView.findViewById(R.id.messagesListView);
        adapter = new MessagesListAdapter(getActivity(), entity = MessageEntities.getInstance().getMessages());
        messagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createDialog(adapter.getItem(position).message);
            }
        });
        messagesListView.setAdapter(adapter);
        return rootView;
    }

    public MessagesFragment() {
    }

    protected void createDialog(String message) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this.getActivity());
        adb.setTitle("Сообщение");
        adb.setMessage(message);
        adb.setIcon(android.R.drawable.ic_dialog_info);
        adb.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        adb.create();
        adb.show();
    }

    public void update() {
        MessageEntities.getInstance().flagEdit = false;
        entity.removeAll(entity);
        entity.addAll(MessageEntities.getInstance().getMessages());
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
        update();
    }
}
