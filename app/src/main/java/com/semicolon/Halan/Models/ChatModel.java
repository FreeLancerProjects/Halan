package com.semicolon.Halan.Models;

import java.io.Serializable;

public class ChatModel implements Serializable {
    private String curr_id;
    private String curr_type;
    private String curr_image;
    private String chat_type;
    private String Chat_image;

    public ChatModel(String curr_id, String curr_type, String curr_image, String chat_type, String chat_image) {
        this.curr_id = curr_id;
        this.curr_type = curr_type;
        this.curr_image = curr_image;
        this.chat_type = chat_type;
        Chat_image = chat_image;
    }

    public String getCurr_id() {
        return curr_id;
    }

    public String getCurr_type() {
        return curr_type;
    }

    public String getCurr_image() {
        return curr_image;
    }

    public String getChat_type() {
        return chat_type;
    }

    public String getChat_image() {
        return Chat_image;
    }
}
