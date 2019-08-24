package com.example.login.PassengerPages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.login.DriverPages.RateAppDriverPage;
import com.example.login.DriverPages.driverHome;
import com.example.login.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

public class RatePage extends AppCompatActivity {

    DatabaseReference databaseRef, databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser user;

    String uid;
    Button submitRating;

    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_page);

        //init views
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("All Passengers");

        uid = user.getUid();


        submitRating = findViewById(R.id.submitRating);

        //submit the rating on the click of button
        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRating();
            }
        });

        //Rating the app
        final SmileRating smileRating = (SmileRating) findViewById(R.id.smile_rating);
        smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
            @Override
            public void onSmileySelected(@BaseRating.Smiley int smiley, boolean reselected) {
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.


                switch (smiley) {
                    case SmileRating.BAD:
                        Toast.makeText(RatePage.this, "BAD", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GOOD:
                        Toast.makeText(RatePage.this, "GOOD", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.GREAT:
                        Toast.makeText(RatePage.this, "GREAT", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.OKAY:
                        Toast.makeText(RatePage.this, "OKAY", Toast.LENGTH_SHORT).show();
                        break;
                    case SmileRating.TERRIBLE:
                        Toast.makeText(RatePage.this, "TERRIBLE", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });

        smileRating.setOnRatingSelectedListener(new SmileRating.OnRatingSelectedListener() {
            @Override
            public void onRatingSelected(int level, boolean reselected) {
                // level is from 1 to 5 (0 when none selected)
                // reselected is false when user selects different smiley that previously selected one
                // true when the same smiley is selected.
                // Except if it first time, then the value will be false.
                Toast.makeText(RatePage.this, "Rate " + level + " Star", Toast.LENGTH_SHORT).show();

                rate = level;

            }
        });
    }

    private void submitRating() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Ratings");
        final String rating = Integer.toString(rate);

        if (rate != 0) {
            String userid = uid;
            databaseReference.child(userid).setValue(rating);

            Toast.makeText(this, "Your " + rating + " rating for this app is saved", Toast.LENGTH_SHORT).show();

            finish();

        } else {
            Toast.makeText(this, "Please select one smiley", Toast.LENGTH_SHORT).show();
        }


    }
}
