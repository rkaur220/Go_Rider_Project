package com.example.login.DriverPages;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.login.MainActivity;
import com.example.login.R;
import com.example.login.UsersFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import hotchemi.android.rate.AppRate;

public class driverHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    TextView userEmail, nameTv, emailTv;
    Button userLogout;
    ImageView avatarIv;
    LinearLayout linearLayout1;


    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String name, lname, email, phone, address, license_number, image, uID;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_more);


        //init firebase
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("All Drivers");

        //init views
        avatarIv = findViewById(R.id.avatarIv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);


        linearLayout1 = (LinearLayout) findViewById(R.id.driverRate);


        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();

            }
        });

        final ImageView imageView = findViewById(R.id.img);


        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override


            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    name = ""+ ds.child("fname").getValue();
                     lname = ""+ ds.child("lname").getValue();
                     email = ""+ ds.child("email").getValue();
                     phone = ""+ ds.child("phone").getValue();
                     address = ""+ds.child("address").getValue();
                     license_number= ""+ds.child("license_number").getValue();
                     image = ""+ ds.child("userDp").getValue();


                    nameTv.setText(name);
                    emailTv.setText(email);

                    try{
                        Picasso.get().load(image).error(R.drawable.ic_add_image).into(avatarIv);
                    }
                    catch (Exception e) {
                        Picasso.get().load(R.drawable.ic_add_image).error(R.drawable.ic_add_image).into(avatarIv);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userLogout = findViewById(R.id.btnLogout);
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(driverHome.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

    }

    private void openDialog() {
        DriverDialog driverDialog = new DriverDialog();
        driverDialog.show(getSupportFragmentManager(), "Rating Dialog");

    }

    public void addTrip(View view) {
        Intent myIntent = new Intent(this, AddPostActivity.class);
        startActivity(myIntent);
    }

    public void seePost(View view) {
        Intent myIntent = new Intent(this, postHistory.class);
        startActivity(myIntent);

    }

    public void profileUpdate(View view) {
        Intent myIntent = new Intent(this, profileUpdate.class);
        myIntent.putExtra("fname", name);
        myIntent.putExtra("lname", lname);
        myIntent.putExtra("email", email);
        myIntent.putExtra("address", address);
        myIntent.putExtra("phone", phone);
        myIntent.putExtra("lic_number", license_number);
        startActivity(myIntent);

    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_addTrip:
                Intent myIntent = new Intent(this, AddPostActivity.class);
                startActivity(myIntent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    public void shareApp(View view) {
        Intent share= new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Share your ride with us.");
        share.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" +getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(share, "Share the app with friends and family!"));
    }


    public void passList(View view) {
        Intent myIntent = new Intent(this, PassList.class);
        startActivity(myIntent);
    }

    public void todayTrip(View view) {
        Intent myIntent = new Intent(this, TodayTrip.class);
        startActivity(myIntent);

    }

}
