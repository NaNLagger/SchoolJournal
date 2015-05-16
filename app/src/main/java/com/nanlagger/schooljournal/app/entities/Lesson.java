package com.nanlagger.schooljournal.app.entities;

/**
 * Created by NaNLagger on 13.05.15.
 *
 * @author Stepan Lyashenko
 */
public class Lesson {
    public int journal_id;
    public int lesson_id;

    public Lesson(int id, int journal_id) {
        this.journal_id = journal_id;
        lesson_id = id;
    }
}
