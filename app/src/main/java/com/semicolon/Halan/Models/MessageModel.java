package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Delta on 18/04/2018.
 */

public class MessageModel implements Serializable {
    private String id_message;
    private String room_id_fk;
    private String image;
    private String from_name;
    private String from_id;
    private String from_type;
    private String from_photo;
    private String to_name;
    private String to_id;
    private String to_type;
    private String to_photo;
    private String message_date;
    private String message_time;
    private String message_type;
    private String message;
    private int success_send;
    private String notification_type;


    public MessageModel() {
    }

    public MessageModel(String room_id_fk, String image, String from_name, String from_id, String from_type, String from_photo, String to_name, String to_id, String to_type, String to_photo, String message_date, String message_time, String message_type, String message) {
        this.room_id_fk = room_id_fk;
        this.image = image;
        this.from_name = from_name;
        this.from_id = from_id;
        this.from_type = from_type;
        this.from_photo = from_photo;
        this.to_name = to_name;
        this.to_id = to_id;
        this.to_type = to_type;
        this.to_photo = to_photo;
        this.message_date = message_date;
        this.message_time = message_time;
        this.message_type = message_type;
        this.message = message;
    }

    public String getId_message() {
        return id_message;
    }

    public String getRoom_id_fk() {
        return room_id_fk;
    }

    public String getImage() {
        return image;
    }

    public String getFrom_name() {
        return from_name;
    }

    public String getFrom_id() {
        return from_id;
    }

    public String getFrom_type() {
        return from_type;
    }

    public String getFrom_photo() {
        return from_photo;
    }

    public String getTo_name() {
        return to_name;
    }

    public String getTo_id() {
        return to_id;
    }

    public String getTo_type() {
        return to_type;
    }

    public String getTo_photo() {
        return to_photo;
    }

    public String getMessage_date() {
        return message_date;
    }

    public String getMessage_time() {
        return message_time;
    }

    public String getMessage_type() {
        return message_type;
    }

    public String getMessage() {
        return message;
    }

    public int getSuccess_send() {
        return success_send;
    }

    public String getNotification_type() {
        return notification_type;
    }
}
