package com.example.login.DriverPages;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.login.R;
import com.example.login.models.Artist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AddPostActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    FirebaseUser user;

    String name1, email, uid, dp;
    Uri image_rui = null;
    ProgressDialog pd;

    List<Integer> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    EditText editTextName, tripPrice;
    Button buttonAdd, dateTime;
    Spinner spinnerFrom, spinnerTo, spinnerBaggage, spinnerPrice, spinnerSeats;
    TextView ResultDateTime, time1;
    int day, month, year, hour, minute;
    int day_x, month_x, year_x, hour_x, minute_x;

    DatabaseReference databaseReference, databaseRef;
    private String value, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);


        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("All Drivers");

        uid = user.getUid();
        email = user.getEmail();



        databaseReference = FirebaseDatabase.getInstance().getReference("Trips");

        editTextName = (EditText) findViewById(R.id.editTextName);
        tripPrice = (EditText) findViewById(R.id.tripPrice);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        spinnerFrom = (Spinner) findViewById(R.id.spinnerFromCity);
        spinnerBaggage = (Spinner) findViewById(R.id.allowedBags);
        spinnerTo = (Spinner) findViewById(R.id.to);
        spinnerPrice = (Spinner) findViewById(R.id.price);
        dateTime =  (Button) findViewById(R.id.btn_click);
        ResultDateTime = (TextView) findViewById(R.id.dateTime);
        time1 = (TextView) findViewById(R.id.time1);
        spinnerSeats = (Spinner) findViewById(R.id.seats);




        dateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddPostActivity.this,
                        AddPostActivity.this, year, month, day);
                datePickerDialog.show();

            }
        });



        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addValues();

            }
        });

        Query query = databaseRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    userName = ""+ ds.child("fname").getValue();
                    dp = ""+ ds.child("userDp").getValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void addValues() {

        final String timeStamp = String.valueOf(System.currentTimeMillis());

        //convert timestamp to mm/dd/yyyy hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String pTime = DateFormat.format("MM/dd/yyyy hh:mm aa", calendar).toString();


        String userDP = dp;
        String username = userName;
        String email = user.getEmail();
        String uid = user.getUid();
        String meetingPoint = editTextName.getText().toString().trim();
        String price2 = tripPrice.getText().toString().trim();
        String dateTime = ResultDateTime.getText().toString().trim();
        String time = time1.getText().toString().trim();
        String from = spinnerFrom.getSelectedItem().toString();
        String artFrom = spinnerFrom.getSelectedItem().toString();
        String to = spinnerTo.getSelectedItem().toString();
        String baggage = spinnerBaggage.getSelectedItem().toString();
        String price = spinnerPrice.getSelectedItem().toString();
        String seats = spinnerSeats.getSelectedItem().toString();


        if (!TextUtils.isEmpty(meetingPoint) && !TextUtils.isEmpty(price2) && !TextUtils.isEmpty(dateTime)
                && !TextUtils.isEmpty(baggage) && !from.equals("From") && !artFrom.equals("From")
                && !to.equals("To") && !baggage.equals("Bag Allowance") && !seats.equals("Seats")) {

            if (!artFrom.equals(to) && !to.equals(artFrom)) {


                String id = databaseReference.push().getKey();
                Artist artist = new Artist(id, meetingPoint, from, to, baggage, price, price2, dateTime, email, uid, username, userDP, pTime, artFrom, time, seats);

                databaseReference.child(id).setValue(artist);

                Toast.makeText(this, "Trip added", Toast.LENGTH_SHORT).show();

                editTextName.setText("");
                tripPrice.setText("");
                ResultDateTime.setText("");
                time1.setText("");
                spinnerTo.setSelection(0);
                spinnerFrom.setSelection(0);
                spinnerBaggage.setSelection(0);
                spinnerPrice.setSelection(0);
                spinnerSeats.setSelection(0);

                finish();
            } else {
                Toast.makeText(this, "Origin and Destination can't be same", Toast.LENGTH_SHORT).show();
            }

            } else {
                Toast.makeText(this, "Please enter all the information ", Toast.LENGTH_SHORT).show();
            }


    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        year_x = i;
        month_x = i1+1;
        day_x= i2;
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddPostActivity.this, AddPostActivity.this,
                hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        hour_x = i;
        minute_x = i1;

        ResultDateTime.setText(month_x+"/"+day_x+"/"+year_x);
        time1.setText(" "+hour_x+":"+minute_x);

    }

}