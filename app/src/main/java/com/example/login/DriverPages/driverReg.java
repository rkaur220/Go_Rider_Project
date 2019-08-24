package com.example.login.DriverPages;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.example.login.PassengerPages.passengerReg;
import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class driverReg extends AppCompatActivity {

    EditText fname, lname, email, password, confirmpass, phone, address, userDp, licensenumber;
    Button reg;
    TextView titype, titype1;
    int setPtype = 1, setPtype1 = 1;
    AwesomeValidation awesomeValidation;
    FirebaseAuth firebaseAuth;
    DatabaseReference DR;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reg);

        titype = findViewById(R.id.titype);
        titype1 = findViewById(R.id.titype1);


        awesomeValidation = new AwesomeValidation(BASIC);

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmpass = (EditText) findViewById(R.id.confirmpassword);
        phone = (EditText) findViewById(R.id.phonenumber);
        address = (EditText) findViewById(R.id.address);
        licensenumber = (EditText) findViewById(R.id.licensenumber);
        reg = (Button) findViewById(R.id.register);

        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(driverReg.this, R.id.fname, "[a-zA-Z\\s]+", R.string.pfnameerr);
        awesomeValidation.addValidation(driverReg.this, R.id.lname, "[a-zA-Z\\s]+", R.string.plnameerr);
        awesomeValidation.addValidation(driverReg.this, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.pemailerr);
        awesomeValidation.addValidation(driverReg.this, R.id.password, regexPassword, R.string.ppasserr);
        awesomeValidation.addValidation(driverReg.this, R.id.confirmpassword, R.id.password, R.string.pconpasserr);
        awesomeValidation.addValidation(driverReg.this, R.id.address, "[a-zA-Z0-9\\s]+", R.string.paddresserr);
        awesomeValidation.addValidation(driverReg.this, R.id.licensenumber, "[a-zA-Z0-9\\s]+", R.string.dlicensenumber);


        firebaseAuth = FirebaseAuth.getInstance();
        DR = FirebaseDatabase.getInstance().getReference().child("All Drivers");


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
        final String type, firstname, lastname, demail, dpassword, dconfpass, dphone, daddress, dlicensenumber;

        if (awesomeValidation.validate()) {
            type = "driver";
            firstname = fname.getText().toString();
            lastname = lname.getText().toString();
            demail = email.getText().toString();
            dpassword = password.getText().toString();
            dconfpass = confirmpass.getText().toString();
            dphone = phone.getText().toString();
            daddress = address.getText().toString();
            dlicensenumber = licensenumber.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(demail, dpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(driverReg.this, "You are Registered Successfully " + firstname, Toast.LENGTH_LONG).show();

                        String userid = firebaseAuth.getCurrentUser().getUid();
                        String DeviceToken = FirebaseInstanceId.getInstance().getToken();
                        DatabaseReference current_user = DR.child(userid);
                        current_user.child("id").setValue(userid);
                        current_user.child("userType").setValue(type);
                        current_user.child("fname").setValue(firstname);
                        current_user.child("lname").setValue(lastname);
                        current_user.child("email").setValue(demail);
                        current_user.child("password").setValue(dpassword);
                        current_user.child("confpass").setValue(dconfpass);
                        current_user.child("phone").setValue(dphone);
                        current_user.child("address").setValue(daddress);
                        current_user.child("license_number").setValue(dlicensenumber);
                        current_user.child("userDp").setValue(" ");




                        startActivity(new Intent(driverReg.this, driverLogin.class));
                    } else {
                        Toast.makeText(driverReg.this, task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                }
            });
        } else {
            Toast.makeText(driverReg.this, "Some fields are empty", Toast.LENGTH_LONG).show();

        }

    }

}
