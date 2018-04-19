package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Delta on 18/04/2018.
 */

public class MessageModel implements Serializable {
    private String message_type;
    private String from_id;
    private String to_id;
    private String from_name;
    private String to_name;
    private String from_photo;
    private String to_photo;
    private String from_type;
    private String to_type;
    private String message;
    private String image;
    private long message_time;

    public MessageModel() {
    }

    public MessageModel(String message_type, String from_id, String to_id, String from_name, String to_name, String from_photo, String to_photo, String from_type, String to_type, String message, String image, long message_time) {
        this.message_type = message_type;
        this.from_id = from_id;
        this.to_id = to_id;
        this.from_name = from_name;
        this.to_name = to_name;
        this.from_photo = from_photo;
        this.to_photo = to_photo;
        this.from_type = from_type;
        this.to_type = to_type;
        this.message = message;
        this.image = image;
        this.message_time = message_time;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }

    public String getFrom_photo() {
        return from_photo;
    }

    public void setFrom_photo(String from_photo) {
        this.from_photo = from_photo;
    }

    public String getTo_photo() {
        return to_photo;
    }

    public void setTo_photo(String to_photo) {
        this.to_photo = to_photo;
    }

    public String getFrom_type() {
        return from_type;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public String getTo_type() {
        return to_type;
    }

    public void setTo_type(String to_type) {
        this.to_type = to_type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getMessage_time() {
        return message_time;
    }

    public void setMessage_time(long message_time) {
        this.message_time = message_time;
    }
}
