package com.example.login.PassengerPages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.DriverPages.DriverDialog;
import com.example.login.DriverPages.profileUpdate;
import com.example.login.MainActivity;
import com.example.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PassProfile extends AppCompatActivity {
    private DrawerLayout drawer1;

    String name, lname, email, phone, address, license_number, image, uID;

    TextView userEmail, nameTv, emailTv, phoneTV;
    Button userLogout;
    ImageView avatarIv;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    LinearLayout linearLayout, linearLayout1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_profile);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        drawer1 = findViewById(R.id.drawer_layout1);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer1, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer1.addDrawerListener(toggle);
        toggle.syncState();





        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("All Passengers");

        avatarIv = findViewById(R.id.avatarIv);
        nameTv = findViewById(R.id.nameTv);
        emailTv = findViewById(R.id.emailTv);
        linearLayout = findViewById(R.id.todayRide);

        linearLayout1 = (LinearLayout) findViewById(R.id.driverRate);


        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();

            }
        });


        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override


            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                     name = "" + ds.child("fname").getValue();
                    lname = ""+ ds.child("lname").getValue();
                    email = "" + ds.child("email").getValue();
                     image = "" + ds.child("userDp").getValue();
                     phone = "" + ds.child("phone").getValue();
                    address = ""+ds.child("address").getValue();



                    nameTv.setText(name);
                    emailTv.setText(email);

                    try {
                        Picasso.get().load(image).error(R.drawable.ic_add_image).into(avatarIv);
                    } catch (Exception e) {
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
                Intent intent = new Intent(PassProfile.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
    }

    public void searchRide(View view) {
        Intent myIntent = new Intent(this, pass_Search_Post.class);
        startActivity(myIntent);

    }

    public void profileUpdate(View view) {
        Intent myIntent = new Intent(this, PassProfileUpdate.class);
        myIntent.putExtra("fname", name);
        myIntent.putExtra("lname", lname);
        myIntent.putExtra("email", email);
        myIntent.putExtra("address", address);
        myIntent.putExtra("phone", phone);
        startActivity(myIntent);

    }

    @Override
    public void onBackPressed() {
        if (drawer1.isDrawerOpen(GravityCompat.START)) {
            drawer1.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void shareApp(View view) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Share your ride with us.");
        share.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(share, "Share the app with friends and family!"));
    }

    public void passRating(View view) {
        Intent myIntent = new Intent(this, PassRating.class);
        startActivity(myIntent);

    }

    public void PassTodayRide(View view) {
        Intent myIntent = new Intent(this, PassTodayRide.class);
        startActivity(myIntent);
    }

    //Rating the app
    private void openDialog() {
        PassengerRateDialog passengerRateDialog = new PassengerRateDialog();
        passengerRateDialog.show(getSupportFragmentManager(), "Rating Dialog");

    }

    //side drawer menu click
    public void logout(MenuItem menuItem){
        Intent intent = new Intent(this, passengerLogin.class);
        startActivity(intent);

    }

}
