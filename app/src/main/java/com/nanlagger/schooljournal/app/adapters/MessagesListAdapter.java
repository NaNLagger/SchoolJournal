package com.nanlagger.schooljournal.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.entities.Message;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 15.05.15.
 *
 * @author Stepan Lyashenko
 */
public class MessagesListAdapter extends BaseAdapter {
    private ArrayList<Message> messages;
    LayoutInflater lInflater;

    public MessagesListAdapter(Context context, ArrayList<Message> messages) {
        this.messages = messages;
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.adapter_message, null);
        }
        TextView message = (TextView) convertView.findViewById(R.id.messageTextView);
        String mess = getItem(position).message;
        mess = mess.length() > 25 ? mess.substring(0, 20) + "..." : mess;
        message.setText(mess);
        return convertView;
    }
}
