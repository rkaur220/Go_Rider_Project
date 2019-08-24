package com.example.login.DriverPages;

public class DriverProfile {
    public String fname, lname, email, address,license_number, phone;

    public DriverProfile(){

    }

    public DriverProfile(String fname, String lname, String email, String address, String license_number, String phone) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.address = address;
        this.license_number = license_number;
        this.phone = phone;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLicense_number() {
        return license_number;
    }

    public void setLicense_number(String license_number) {
        this.license_number = license_number;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
