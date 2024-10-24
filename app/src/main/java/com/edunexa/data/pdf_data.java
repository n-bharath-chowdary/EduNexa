package com.edunexa.data;

public class pdf_data {
    String pdf_name, pdf_url, send_to, ukey, from;

    public pdf_data() {
    }

    public pdf_data(String pdf_name, String pdf_url, String send_to, String ukey, String from) {
        this.pdf_name = pdf_name;
        this.pdf_url = pdf_url;
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

    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(String pdf_name) {
        this.pdf_name = pdf_name;
    }

    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(String pdf_url) {
        this.pdf_url = pdf_url;
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
