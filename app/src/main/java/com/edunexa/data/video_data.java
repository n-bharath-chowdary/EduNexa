package com.edunexa.data;

public class video_data {
    String video_name, video_url, send_to, ukey, from;

    public video_data() {
    }

    public video_data(String video_name, String video_url, String send_to, String ukey, String from) {
        this.video_name = video_name;
        this.video_url = video_url;
        this.send_to = send_to;
        this.ukey = ukey;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getSend_to() {
        return send_to;
    }

    public void setSend_to(String send_to) {
        this.send_to = send_to;
    }

    public String getUkey() {
        return ukey;
    }

    public void setUkey(String ukey) {
        this.ukey = ukey;
    }
}
