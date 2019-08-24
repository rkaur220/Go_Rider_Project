package com.example.login.PassengerPages;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.DriverPages.driverLogin;
import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class passengerLogin extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText userEmail, userPass;
    TextView titype, mRecoveryPassTv;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    Button userLogin;

    int setPtype = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_login);

        userEmail = findViewById(R.id.username);
        userPass = findViewById(R.id.password);
        userLogin =  findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);

        titype = findViewById(R.id.titype);
        mRecoveryPassTv = findViewById(R.id.recoveryPassTv);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("All Passengers");

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String email = userEmail.getText().toString().trim();
                String password = userPass.getText().toString().trim();

                if(TextUtils.isEmpty(email)) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(passengerLogin.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;

                }
                if(TextUtils.isEmpty(password)){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(passengerLogin.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.signInWithEmailAndPassword(userEmail.getText().toString(),
                        userPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if(task.isSuccessful()){

                            startActivity(new Intent(passengerLogin.this, PassProfile.class));

                        }else {
                            Toast.makeText(passengerLogin.this, "User name and Password do not match", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        //password hide/show
        titype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setPtype == 1){
                    setPtype = 0;
                    userPass.setTransformationMethod(null);
                    if(userPass.getText().length()>0){
                        userPass.setSelection(userPass.getText().length());
                        titype.setBackgroundResource(R.drawable.ic_hide_password_eye);
                    }
                } else {
                    setPtype = 1;
                    userPass.setTransformationMethod(new PasswordTransformationMethod());
                    if(userPass.getText().length()>0){
                        userPass.setSelection(userPass.getText().length());
                        titype.setBackgroundResource(R.drawable.ic_password_eye);
                    }
                }
            }
        });

        //recovery pass textview click
        mRecoveryPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoveryPasswordDialog();
            }
        });
    }

    private void showRecoveryPasswordDialog() {
        //Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recovery Password");

        LinearLayout linearLayout = new LinearLayout(this);

        final EditText emailEt = new EditText(this);
        emailEt.setHint("Please enter email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        emailEt.setMinEms(16);

        linearLayout.addView(emailEt);
        linearLayout.setPadding(10, 10, 10, 10);

        builder.setView(linearLayout);

        //recovery button
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //input email
                String email = emailEt.getText().toString().trim();
                beginRecovery(email);
            }
        });
        //cancel button
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                //dismiss dialog
                dialog.dismiss();
            }
        });

        //show dialog
        builder.create().show();
    }

    private void beginRecovery(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(passengerLogin.this, "Email sent", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(passengerLogin.this, "Failed...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //get and show error message
                Toast.makeText(passengerLogin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void nextRegister(View myView){
        Intent myIntent = new Intent(this, passengerPage.class);
        startActivity(myIntent);
    }

    public void passlogin(View view) {
        Intent myIntent = new Intent(this, passengerLogin.class);
        startActivity(myIntent);
    }

    public void driverlogin(View view) {
        Intent myIntent = new Intent(this, driverLogin.class);
        startActivity(myIntent);
    }
}
