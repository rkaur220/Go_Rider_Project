package com.example.login.PassengerPages;


import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login.DriverPages.TripList;
import com.example.login.PassengerPages.ChatActivity;
import com.example.login.DriverPages.postHistory;
import com.example.login.R;
import com.example.login.models.Artist;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class passTripList extends ArrayAdapter<Artist> {

    private Activity context;
    private List<Artist> tripList;

    DatabaseReference databaseReference, DR;

    String seats;
    int avaibleSeats;
    int reduce =1;
    String pID;
    String newSeats;
    String driverName;



    public passTripList(Activity context, List<Artist> tripList) {
        super(context, R.layout.list_layout_pass, tripList);
        this.context = context;
        this.tripList = tripList;

        databaseReference = FirebaseDatabase.getInstance().getReference("Trips");

    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();



        final View listViewItem = inflater.inflate(R.layout.list_layout_pass, null, true);
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
        Button button = (Button) listViewItem.findViewById(R.id.bookSeats);



        final Artist artist = tripList.get(getCount()- position - 1);


        imageViewPostImage.setVisibility(View.GONE);
        textViewDateTimeStamp.setText(artist.getTimestamp());
        textViewName.setText(artist.getUserName());
        textViewFrom.setText("From: "+artist.getFrom());
        textViewTo.setText("To: "+artist.getTo());
        textViewDateTime.setText("Date & Time of the ride: "+artist.getDateTime()+" " +artist.getTime());
        //textViewPrice.setText("Price in "+artist.getPrice());
        textViewBag.setText("Bag allowance: "+artist.getBagAllowance());
        textViewDetails.setText("Meeting point & Details abut the trip: "+artist.getMeetingPoint());
        textViewPrice2.setText("Price per person: "+artist.getPrice()+artist.getTripPrice());
        seats = artist.getSeats();

        avaibleSeats = Integer.parseInt(seats);

        button.setText("Book ride here (Available seats: " +avaibleSeats+ " )");


        try{
            Picasso.get().load(artist.getUserDp()).error(R.drawable.registerlogo).into(imageViewDp);
        }
        catch (Exception e) {
            Picasso.get().load(R.drawable.registerlogo).error(R.drawable.registerlogo).into(imageViewDp);

        }

        //get the details of the one item in list
        /*listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String hisUID = artist.getUid();
                String hisName = artist.getUserName();
                Intent myIntent = new Intent(context, ChatActivity.class);
                myIntent.putExtra("uid", hisUID);
                myIntent.putExtra("userName", hisName);

                context.startActivity(myIntent);
                Toast.makeText(context, "uid "+hisUID +hisName, Toast.LENGTH_SHORT).show();
            }
        }); */

        if(avaibleSeats > 0) {

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final int which_item = position;
                    new AlertDialog.Builder(context)
                            .setIcon(R.drawable.ic_done_all)
                            .setTitle("Are you sure?")
                            .setMessage("Do you want to book this ride?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    pID = artist.getArtId();
                                    String driverID = artist.getUid();
                                    driverName = artist.getUserName();



                                    //notification
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                        String id = "_channel_01";
                                        int importance = NotificationManager.IMPORTANCE_LOW;
                                        NotificationChannel mChannel = new NotificationChannel(driverID, "notification", importance);
                                        mChannel.enableLights(true);

                                        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                                        NotificationCompat.Builder builder = new
                                                NotificationCompat.Builder(context.getApplicationContext(), driverID)
                                                .setSmallIcon(R.drawable.ic_new_notifications)
                                                .setContentTitle("Ride Confirmation")
                                                .setContentText("The Passenger booked the one ride with you")
                                                .setSound(uri);


                                        Intent notificationIntent = new Intent(context, postHistory.class);
                                        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                                        builder.setContentIntent(contentIntent);
                                        builder.setAutoCancel(true);


                                        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                                        if (notificationManager != null) {
                                            notificationManager.createNotificationChannel(mChannel);
                                            //mNotificationManager.notify(PRIMARY_FOREGROUND_NOTIF_SERVICE_ID, notification);

                                            notificationManager.notify(0, builder.build());

                                        }

                                    }

                                    Query query = FirebaseDatabase.getInstance().getReference("Trips").orderByChild("artId").equalTo(pID);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {


                                            }
                                            //seat booking
                                            new AlertDialog.Builder(context)
                                                    .setIcon(R.drawable.ic_done_all)
                                                    .setTitle("Ride Confirmation")
                                                    .setMessage("Your ride is confirmed.")
                                                    .setNegativeButton("OK", null)
                                                    .show();

                                            avaibleSeats = avaibleSeats - 1;

                                            newSeats = String.valueOf(avaibleSeats);
                                            //Log.i("Seats: ", newSeats);

                                            databaseReference.child(pID).child("seats").setValue(newSeats);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }).setNegativeButton("No", null)
                            .show();

                    seats = artist.getSeats();
                    avaibleSeats = Integer.parseInt(seats);

                    return;

                }
            });


        }

        return listViewItem;



    }

}
