package com.edunexa.data;

public class img_data {

    String img_name, img_url, send_to, ukey, from;

    public img_data() {
    }

    public img_data(String img_name, String img_url, String send_to, String ukey, String from) {
        this.img_name = img_name;
        this.img_url = img_url;
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

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
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
