package com.example.login.PassengerPages;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.santalu.maskedittext.MaskEditText;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class passengerReg extends AppCompatActivity {


    EditText fname, lname, email, phone, password, confirmpass, address, userDp;

    TextView titype, titype1;
    int setPtype = 1, setPtype1 = 1;
    Button reg;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    DatabaseReference DR;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_reg);

        titype = findViewById(R.id.titype);
        titype1 = findViewById(R.id.titype1);

        awesomeValidation = new AwesomeValidation(BASIC);

        fname = (EditText) findViewById(R.id.pfname);
        lname = (EditText) findViewById(R.id.plname);
        email = (EditText) findViewById(R.id.pemail);
        password = (EditText) findViewById(R.id.ppassword);
        confirmpass = (EditText) findViewById(R.id.pconfirmpassword);
        phone = (EditText) findViewById(R.id.pphonenumber);
        address = (EditText) findViewById(R.id.paddress);
        reg = (Button) findViewById(R.id.pregister);


        //validation for text fields
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(passengerReg.this, R.id.pfname, "[a-zA-Z\\s]+", R.string.pfnameerr);
        awesomeValidation.addValidation(passengerReg.this, R.id.plname, "[a-zA-Z\\s]+", R.string.plnameerr);
        awesomeValidation.addValidation(passengerReg.this, R.id.pemail, android.util.Patterns.EMAIL_ADDRESS, R.string.pemailerr);
        awesomeValidation.addValidation(passengerReg.this, R.id.ppassword, regexPassword, R.string.ppasserr);
        awesomeValidation.addValidation(passengerReg.this, R.id.pconfirmpassword, R.id.ppassword, R.string.pconpasserr);
        awesomeValidation.addValidation(passengerReg.this, R.id.paddress, "[a-zA-Z0-9\\s]+", R.string.paddresserr);


        firebaseAuth = FirebaseAuth.getInstance();
        DR = FirebaseDatabase.getInstance().getReference().child("All Passengers");


        reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startSignup();

            }
        });

        //password hide/show
        titype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setPtype == 1) {
                    setPtype = 0;
                    password.setTransformationMethod(null);
                    if (password.getText().length() > 0) {
                        password.setSelection(password.getText().length());
                        titype.setBackgroundResource(R.drawable.ic_hide_password_eye);
                    }
                } else {
                    setPtype = 1;
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    if (password.getText().length() > 0) {
                        password.setSelection(password.getText().length());
                        titype.setBackgroundResource(R.drawable.ic_password_eye);
                    }
                }
            }
        });

        //confirm password hide/show
        titype1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (setPtype1 == 1) {
                    setPtype1 = 0;
                    confirmpass.setTransformationMethod(null);
                    if (confirmpass.getText().length() > 0) {
                        confirmpass.setSelection(confirmpass.getText().length());
                        titype1.setBackgroundResource(R.drawable.ic_hide_password_eye);
                    }
                } else {
                    setPtype1 = 1;
                    confirmpass.setTransformationMethod(new PasswordTransformationMethod());
                    if (confirmpass.getText().length() > 0) {
                        confirmpass.setSelection(confirmpass.getText().length());
                        titype1.setBackgroundResource(R.drawable.ic_password_eye);
                    }
                }
            }
        });

    }

    private void startSignup() {

        final String type, firstname, lastname, femail, fpassword, confpass, fphone, faddress;

        if (awesomeValidation.validate()) {
            type = "passenger";
            firstname = fname.getText().toString();
            lastname = lname.getText().toString();
            femail = email.getText().toString();
            fpassword = password.getText().toString();
            confpass = confirmpass.getText().toString();
            fphone = phone.getText().toString();
            faddress = address.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(femail, fpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(passengerReg.this, "You are Registered Successfully " + firstname, Toast.LENGTH_LONG).show();

                        String userid = firebaseAuth.getCurrentUser().getUid();
                        DatabaseReference current_user = DR.child(userid);
                        current_user.child("id").setValue(userid);
                        current_user.child("userType").setValue(type);
                        current_user.child("fname").setValue(firstname);
                        current_user.child("lname").setValue(lastname);
                        current_user.child("email").setValue(femail);
                        current_user.child("password").setValue(fpassword);
                        current_user.child("confpass").setValue(confpass);
                        current_user.child("phone").setValue(fphone);
                        current_user.child("address").setValue(faddress);
                        current_user.child("userDp").setValue(" ");



                        startActivity(new Intent(passengerReg.this, passengerLogin.class));

                    } else {
                        Toast.makeText(passengerReg.this, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            Toast.makeText(passengerReg.this, "Some fields are empty", Toast.LENGTH_LONG).show();
        }
    }


}
