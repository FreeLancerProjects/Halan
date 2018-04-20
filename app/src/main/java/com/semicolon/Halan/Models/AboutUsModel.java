package com.semicolon.Halan.Models;

/**
 * Created by Elashry on 02/04/2018.
 */

public class AboutUsModel {

    private String content;
    private String type;
    private String title;
    private String facebook_url;
    private String twitter_url;
    private String instgram_url;
    private String google_url;

    public AboutUsModel(String content, String type, String title, String facebook_url, String twitter_url, String instgram_url, String google_url) {
        this.content = content;
        this.type = type;
        this.title = title;
        this.facebook_url = facebook_url;
        this.twitter_url = twitter_url;
        this.instgram_url = instgram_url;
        this.google_url = google_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFacebook_url() {
        return facebook_url;
    }

    public void setFacebook_url(String facebook_url) {
        this.facebook_url = facebook_url;
    }

    public String getTwitter_url() {
        return twitter_url;
    }

    public void setTwitter_url(String twitter_url) {
        this.twitter_url = twitter_url;
    }

    public String getInstgram_url() {
        return instgram_url;
    }

    public void setInstgram_url(String instgram_url) {
        this.instgram_url = instgram_url;
    }

    public String getGoogle_url() {
        return google_url;
    }

    public void setGoogle_url(String google_url) {
        this.google_url = google_url;
    }
}
