package com.example.login.DriverPages;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class profileUpdate extends AppCompatActivity {
    private static final String TAG = "profileUpdate";

    EditText fnameTv, lnameTv, phoneTV, licenseTV, addressTv;
    TextView update;
    LinearLayout linearLayout1;


    FirebaseAuth firebaseAuth, mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, mReference;


    FirebaseDatabase mDatabase;
    StorageReference mStorage;

    private static final int GalleryRequest = 71;
    Context mContext = profileUpdate.this;
    String user_id;

    Uri imageUri;

    ImageView mBack, mUpdate, mProfile_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);

        //init views
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        mStorage = FirebaseStorage.getInstance().getReference();

        databaseReference = mReference.child("All Drivers").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        fnameTv = findViewById(R.id.fname);
        lnameTv = findViewById(R.id.lname);
        addressTv = findViewById(R.id.address);
        phoneTV = findViewById(R.id.phonenumber);
        licenseTV = findViewById(R.id.licensenumber);
        update = findViewById(R.id.update);
        mProfile_image = findViewById(R.id.iv_profile_image);


        fnameTv.setText(getIntent().getStringExtra("fname"));
        lnameTv.setText(getIntent().getStringExtra("lname"));
        phoneTV.setText(getIntent().getStringExtra("phone"));
        addressTv.setText(getIntent().getStringExtra("address"));
        licenseTV.setText(getIntent().getStringExtra("lic_number"));


        mBack = findViewById(R.id.back_arrow);
        mUpdate = findViewById(R.id.update_done);


        getUser_Profile_Data();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Back to Home Page");
                finish();
            }
        });

        mProfile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("images/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GalleryRequest);
            }
        });

    }


    private void getUser_Profile_Data() {


        mReference.child("All Driver").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Log.d(TAG, "onDataChange: User id :- " + FirebaseAuth.getInstance().getCurrentUser().getUid());
                        String image;

                        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        if (imageUri != null) {
                            image = dataSnapshot.child("userDp").getValue().toString();

                            Picasso.get().load(image).into(mProfile_image);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    //update onclick

    public void update1(View view) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("fname").setValue(fnameTv.getText().toString());
                dataSnapshot.getRef().child("lname").setValue(lnameTv.getText().toString());
                dataSnapshot.getRef().child("address").setValue(addressTv.getText().toString());
                dataSnapshot.getRef().child("phone").setValue(phoneTV.getText().toString());
                dataSnapshot.getRef().child("license_number").setValue(licenseTV.getText().toString());


                Toast.makeText(mContext, "Profile updated successfully...", Toast.LENGTH_SHORT).show();
                profileUpdate.this.finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryRequest && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri imagePath = data.getData();
            CropImage.activity(imagePath)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(profileUpdate.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                imageUri = result.getUri();
                Log.d(TAG, "onActivityResult: Image Uri " + imageUri.toString());

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                mProfile_image.setImageURI(imageUri);

                final StorageReference imagePath = mStorage.child(getString(R.string.all_drivers)).child(user_id).child("profile.jpg");

                imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        final String downloadurl = uri.toString();
                    }
                });

                imagePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressDialog.dismiss();
                        imagePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                final String downloadurl = uri.toString();
                                mReference.child(getString(R.string.all_drivers)).child(user_id).child("userDp").setValue(downloadurl);
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(profileUpdate.this, "Failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage("Profile Updated " + (int) progress + "%");
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

}