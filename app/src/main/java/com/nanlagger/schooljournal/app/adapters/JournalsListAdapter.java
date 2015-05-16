package com.nanlagger.schooljournal.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.nanlagger.schooljournal.app.R;
import com.nanlagger.schooljournal.app.entities.Journal;
import com.nanlagger.schooljournal.app.entities.Lesson;
import com.nanlagger.schooljournal.app.entities.Scope;
import com.nanlagger.schooljournal.app.entities.ScopeEntities;

import java.util.ArrayList;

public class JournalsListAdapter extends BaseExpandableListAdapter {

    private ArrayList<Journal> mGroups;
    private Context mContext;
  
    public JournalsListAdapter(Context context, ArrayList<Journal> groups) {
        mContext = context;
        mGroups = groups;
    }
    
    @Override
    public int getGroupCount() {
        return mGroups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mGroups.get(groupPosition).getLessons().size();
    }

    @Override
    public Journal getGroup(int groupPosition) {
        return mGroups.get(groupPosition);
    }

    @Override
    public Lesson getChild(int groupPosition, int childPosition) {
        return mGroups.get(groupPosition).getLessons().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_group_view, null);
        }

        if (isExpanded){
           //Изменяем что-нибудь, если текущая Group раскрыта
        }
        else{
            //Изменяем что-нибудь, если текущая Group скрыта
        }

        TextView textGroup = (TextView) convertView.findViewById(R.id.textGroup);
        textGroup.setText(getGroup(groupPosition).subject_name);

        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_child_view, null);
        }

        TextView textChild = (TextView) convertView.findViewById(R.id.textChild);
        TextView textScope = (TextView) convertView.findViewById(R.id.textView3);
        Lesson lesson = getChild(groupPosition, childPosition);
        String lessonName = "Урок " + (childPosition + 1) + ": ";
        ArrayList<Scope> scopes = ScopeEntities.getInstance().getScopes(lesson.lesson_id);
        String scopesString = "";
        for (Scope scope : scopes) {
            String s = "";
            if(scope.scope == 0) {
                s = "н";
            } else {
                s = String.valueOf(scope.scope);
            }
            scopesString += s + " / ";
        }
        textChild.setText(lessonName);
        if (scopesString.length() > 2) {
            scopesString = scopesString.substring(0, scopesString.length() - 2);
        } else {
            scopesString = "Нет оценок";
        }
        textScope.setText(scopesString);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}