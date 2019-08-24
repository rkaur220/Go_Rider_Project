package com.example.login.DriverPages;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.SearchView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import android.support.v7.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.List;

import static com.example.login.R.id.action_search;


public class postHistory extends AppCompatActivity {

    Toolbar toolbar;

    ListView listViewTrips;
    List<Artist> tripList;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseRef;
    DatabaseReference databaseReference;
    TripList adapter;
    String userID, hisName;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_history);

        //toolbar = (Toolbar) findViewById( R.id.toolbar );
        //toolbar.setTitle( getResources().getString( R.string.app_name ) );
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

        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tripList.clear();

                for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = tripSnapshot.getValue(Artist.class);

                    tripList.add(artist);
                    userID = artist.getUid();
                }

                adapter = new TripList(postHistory.this, tripList);
                listViewTrips.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


    private void searchPost(final String searchQuery) {
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tripList.clear();

                for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                    Artist artist = tripSnapshot.getValue(Artist.class);

                    if (artist.getFrom().toLowerCase().contains(searchQuery.toLowerCase()) ||
                            artist.getTo().toLowerCase().contains(searchQuery.toLowerCase())) {

                        tripList.add(artist);
                    }

                }

                adapter = new TripList(postHistory.this, tripList);
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

    //menu click
    public void logout(MenuItem menuItem) {
        Intent intent = new Intent(this, driverLogin.class);
        startActivity(intent);

    }


}