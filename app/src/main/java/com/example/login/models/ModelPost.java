package com.example.login.models;

public class ModelPost {
    String pId, uId, uEmail, uName, uDp,  from, to, price1, price2, datetime, desciption, bagcus, pImage;

    public ModelPost() {
    }

    public ModelPost(String pId, String uId, String uEmail, String uName, String uDp, String from, String to, String price1, String price2, String datetime, String desciption, String bagcus, String pImage) {
        this.pId = pId;
        this.uId = uId;
        this.uEmail = uEmail;
        this.uName = uName;
        this.uDp = uDp;
        this.from = from;
        this.to = to;
        this.price1 = price1;
        this.price2 = price2;
        this.datetime = datetime;
        this.desciption = desciption;
        this.bagcus = bagcus;
        this.pImage= pImage;

    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuEmail() {
        return uEmail;
    }

    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuDp() {
        return uDp;
    }

    public void setuDp(String uDp) {
        this.uDp = uDp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPrice1() {
        return price1;
    }

    public void setPrice1(String price1) {
        this.price1 = price1;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public String getBagcus() {
        return bagcus;
    }

    public void setBagcus(String bagcus) {
        this.bagcus = bagcus;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }
}

