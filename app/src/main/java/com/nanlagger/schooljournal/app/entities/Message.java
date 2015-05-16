package com.nanlagger.schooljournal.app.entities;

/**
 * Created by NaNLagger on 15.05.15.
 *
 * @author Stepan Lyashenko
 */
public class Message {

    public int message_id;
    public String message;

    public Message(int id, String message) {
        this.message_id = id;
        this.message = message;
    }
}
