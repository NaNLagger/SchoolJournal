package com.nanlagger.schooljournal.app.entities;

/**
 * Created by NaNLagger on 13.05.15.
 *
 * @author Stepan Lyashenko
 */
public class Scope {
    public int scope;
    public int lesson_id;

    public Scope(int scope, int lesson_id) {
        this.lesson_id = lesson_id;
        this.scope = scope;
    }
}
