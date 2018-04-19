package com.semicolon.Halan.Models;

import java.io.Serializable;

/**
 * Created by Delta on 19/04/2018.
 */

public class ChatModel implements Serializable {
    private String curr_id;
    private String chat_id;
    private String curr_name;
    private String chat_name;
    private String curr_type;
    private String chat_type;
    private String curr_img;
    private String chat_img;

    public ChatModel() {
    }

    public ChatModel(String curr_id, String chat_id, String curr_type, String chat_type, String curr_img, String chat_img) {
        this.curr_id = curr_id;
        this.chat_id = chat_id;
        this.curr_type = curr_type;
        this.chat_type = chat_type;
        this.curr_img = curr_img;
        this.chat_img = chat_img;
    }

    public String getCurr_id() {
        return curr_id;
    }

    public void setCurr_id(String curr_id) {
        this.curr_id = curr_id;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getCurr_name() {
        return curr_name;
    }

    public void setCurr_name(String curr_name) {
        this.curr_name = curr_name;
    }

    public String getChat_name() {
        return chat_name;
    }

    public void setChat_name(String chat_name) {
        this.chat_name = chat_name;
    }

    public String getCurr_type() {
        return curr_type;
    }

    public void setCurr_type(String curr_type) {
        this.curr_type = curr_type;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public String getCurr_img() {
        return curr_img;
    }

    public void setCurr_img(String curr_img) {
        this.curr_img = curr_img;
    }

    public String getChat_img() {
        return chat_img;
    }

    public void setChat_img(String chat_img) {
        this.chat_img = chat_img;
    }
}
