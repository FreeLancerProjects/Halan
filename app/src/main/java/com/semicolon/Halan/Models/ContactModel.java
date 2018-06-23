package com.semicolon.Halan.Models;

import java.io.Serializable;

public class ContactModel implements Serializable {
    private String facebook_url;
    private String twitter_url;
    private String google_url;
    private String instgram_url;
    private String our_phone_number;
    private String email;
    private String snapchat_url;

    public String getFacebook_url() {
        return facebook_url;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public String getGoogle_url() {
        return google_url;
    }

    public String getInstgram_url() {
        return instgram_url;
    }

    public String getOur_phone_number() {
        return our_phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getSnapchat_url() {
        return snapchat_url;
    }
}
