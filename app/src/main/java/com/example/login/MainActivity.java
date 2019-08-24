package com.example.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.DriverPages.driverLogin;
import com.example.login.PassengerPages.passengerLogin;
import com.example.login.PassengerPages.passengerPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    TextView titype, mRecoveryPassTv;
    int setPtype = 1;

    EditText password;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        titype = findViewById(R.id.titype);
        password = findViewById(R.id.password);
        mRecoveryPassTv = findViewById(R.id.recoveryPassTv);

        //password hide/show
        titype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setPtype == 1){
                    setPtype = 0;
                    password.setTransformationMethod(null);
                    if(password.getText().length()>0){
                        password.setSelection(password.getText().length());
                        titype.setBackgroundResource(R.drawable.ic_hide_password_eye);
                    }
                } else {
                    setPtype = 1;
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    if(password.getText().length()>0){
                        password.setSelection(password.getText().length());
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
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Email sent", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(MainActivity.this, "Failed...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //get and show error message
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void loginmsz(View view) {
        Toast.makeText(this, "Please select driver or passenger before login", Toast.LENGTH_SHORT).show();
    }

}
