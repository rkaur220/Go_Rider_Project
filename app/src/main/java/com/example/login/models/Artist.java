package com.example.login.models;

public class Artist {

    String artId;
    String meetingPoint;
    String artFrom;
    String to;
    String bagAllowance;
    String price;
    String tripPrice;
    String dateTime;
    String email;
    String uid;
    String userName;
    String userDp;
    String timestamp;
    String from;
    String time;
    String seats;


    public Artist(){

    }

    public Artist(String artId, String meetingPoint, String artFrom, String to, String bagAllowance,
                  String price, String tripPrice, String dateTime, String email, String uid, String userName,
                  String userDp, String timestamp, String from, String time, String seats) {
        this.artId = artId;
        this.meetingPoint = meetingPoint;
        this.artFrom = artFrom;
        this.to = to;
        this.bagAllowance = bagAllowance;
        this.price = price;
        this.tripPrice = tripPrice;
        this.dateTime =  dateTime;
        this.email = email;
        this.uid = uid;
        this.userName = userName;
        this.userDp = userDp;
        this.timestamp = timestamp;
        this.from = from;
        this.time = time;
        this.seats = seats;

    }

    public void setArtId(String artId) {
        this.artId = artId;
    }

    public void setMeetingPoint(String meetingPoint) {
        this.meetingPoint = meetingPoint;
    }

    public void setArtFrom(String artFrom) {
        this.artFrom = artFrom;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setBagAllowance(String bagAllowance) {
        this.bagAllowance = bagAllowance;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setTripPrice(String tripPrice) {
        this.tripPrice = tripPrice;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserDp(String userDp) {
        this.userDp = userDp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getArtId() {
        return artId;
    }

    public String getMeetingPoint() {
        return meetingPoint;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getBagAllowance(){
        return bagAllowance;
    }

    public String getPrice(){
        return price;
    }

    public String getTripPrice(){
        return tripPrice;
    }

    public String getDateTime(){
        return dateTime;
    }

    public String getEmail(){
        return email;
    }

    public String getUid(){
        return uid;
    }

    public String getUserName(){
        return userName;
    }

    public String getUserDp(){
        return userDp;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getArtFrom(){
        return artFrom;
    }

    public String getTime(){
        return time;
    }

    public String getSeats() {
        return seats;
    }


}
