package com.example.login.DriverPages;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import java.util.List;

public class TripList extends ArrayAdapter<Artist> {

    String uid;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseRef;

    FirebaseUser user;

    private Activity context;
    private List<Artist> tripList;

    public TripList(Activity context, List<Artist> tripList) {
        super(context, R.layout.list_layout, tripList);
        this.context = context;
        this.tripList = tripList;


        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("All Drivers");

        uid = user.getUid();


        databaseReference = FirebaseDatabase.getInstance().getReference("Trips");

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();


        final View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        ImageView imageViewDp = (ImageView) listViewItem.findViewById(R.id.uPictureIv);
        TextView textViewDateTimeStamp = (TextView) listViewItem.findViewById(R.id.textViewDateTimeStamp);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        ImageView imageViewPostImage = (ImageView) listViewItem.findViewById(R.id.textViewImage);
        TextView textViewFrom = (TextView) listViewItem.findViewById(R.id.textViewFrom);
        TextView textViewTo = (TextView) listViewItem.findViewById(R.id.textViewTo);
        TextView textViewDateTime = (TextView) listViewItem.findViewById(R.id.textViewDateTime);
        TextView textViewPrice2 = (TextView) listViewItem.findViewById(R.id.textViewPrice2);
        TextView textViewBag = (TextView) listViewItem.findViewById(R.id.textViewBagDetails);
        TextView textViewDetails = (TextView) listViewItem.findViewById(R.id.textViewDetails);
        TextView seatList = (TextView) listViewItem.findViewById(R.id.textSeats);

        final Artist artist = tripList.get(getCount() - position - 1);

        imageViewPostImage.setVisibility(View.GONE);
        textViewDateTimeStamp.setText(artist.getTimestamp());
        textViewName.setText(artist.getUserName());
        textViewFrom.setText("From: " + artist.getFrom());
        textViewTo.setText("To: " + artist.getTo());
        textViewDateTime.setText("Date & Time of the ride: " + artist.getDateTime()+" " +artist.getTime());
        //textViewPrice.setText("Price in "+artist.getPrice());
        textViewBag.setText("Bag allowance: " + artist.getBagAllowance());
        textViewDetails.setText("Meeting point & Details abut the trip: " + artist.getMeetingPoint());
        textViewPrice2.setText("Price per person: " + artist.getPrice() + artist.getTripPrice());
        seatList.setText("Empty seats: " + artist.getSeats());


        try{
            Picasso.get().load(artist.getUserDp()).error(R.drawable.registerlogo).into(imageViewDp);;
        }
        catch (Exception e) {
            Picasso.get().load(R.drawable.registerlogo).error(R.drawable.registerlogo).into(imageViewDp);

        }


        //get the details of the one item in list
        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int which_item = position;
                new AlertDialog.Builder(context)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you really want to delete this trip?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String pID = artist.getArtId();
                                Query query = FirebaseDatabase.getInstance().getReference("Trips").orderByChild("artId").equalTo(pID);
                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            ds.getRef().removeValue(); //remove value from firebase where id matches
                                        }
                                        //delete message
                                        Toast.makeText(context, "Your trip is deleted successfuly", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }).setNegativeButton("No", null)
                        .show();
                return;

            }
        });


        return listViewItem;


    }


}
