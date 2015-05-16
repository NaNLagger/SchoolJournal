package com.nanlagger.schooljournal.app.entities;

import java.util.ArrayList;

/**
 * Created by NaNLagger on 12.05.15.
 *
 * @author Stepan Lyashenko
 */
public class Journal {
    public int journals_id;
    public String subject_name;

    public Journal(int id, String name) {
        journals_id = id;
        subject_name = name;
    }

    public ArrayList<Lesson> getLessons() {
        return LessonEntities.getInstance().getLessons(journals_id);
    }
}
