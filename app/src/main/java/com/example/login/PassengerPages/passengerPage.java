package com.example.login.PassengerPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.login.DriverPages.driverReg;
import com.example.login.R;

public class passengerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_page);
    }

    public void nextDriver(View myView){
        Intent myIntent = new Intent(this, driverReg.class);
        startActivity(myIntent);
    }

    public void nextPassenger(View myView){
        Intent myIntent = new Intent(this, passengerReg.class);
        startActivity(myIntent);
    }
}
