package com.example.login.PassengerPages;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.login.DriverPages.TripList;
import com.example.login.R;
import com.example.login.models.Artist;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.login.R.id.action_search;

public class PassTodayRide extends AppCompatActivity {

    ListView listViewTrips;
    List<Artist> tripList;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseRef;
    DatabaseReference databaseReference;
    passTripList adapter;
    String userID, dateTime;

    String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_today_ride);

        currentDate = new SimpleDateFormat("M/d/yyyy", Locale.getDefault()).format(new Date());

        listViewTrips = (ListView) findViewById(R.id.listViewTrips);

        tripList = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = firebaseDatabase.getReference("All Drivers");
        databaseReference = FirebaseDatabase.getInstance().getReference("Trips");
    }

    @Override
    protected void onStart() {
        super.onStart();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tripList.clear();

                for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = tripSnapshot.getValue(Artist.class);

                    dateTime = artist.getDateTime();

                    if (dateTime.equals(currentDate)) {

                        tripList.add(artist);

                    }

                }

                adapter = new passTripList(PassTodayRide.this, tripList);
                listViewTrips.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    private void searchPost(final String searchQuery) {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tripList.clear();

                for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = tripSnapshot.getValue(Artist.class);

                    dateTime = artist.getDateTime();

                    if (dateTime.equals(currentDate)) {


                        if (artist.getFrom().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            artist.getTo().toLowerCase().contains(searchQuery.toLowerCase())) {


                            tripList.add(artist);
                        }
                    }

                }

                adapter = new passTripList(PassTodayRide.this, tripList);
                listViewTrips.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu, menu);

        //searchview to search trips by origin/destination
        MenuItem item = menu.findItem(action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when user press search button
                if (!TextUtils.isEmpty(s)) {
                    searchPost(s);

                } else {
                    onStart();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called as and when user press any letter
                if (!TextUtils.isEmpty(s)) {
                    searchPost(s);

                } else {
                    onStart();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void logout(MenuItem menuItem){
        Intent intent = new Intent(this, passengerLogin.class);
        startActivity(intent);

    }

}
