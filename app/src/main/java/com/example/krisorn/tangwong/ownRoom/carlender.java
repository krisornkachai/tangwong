package com.example.krisorn.tangwong.ownRoom;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.krisorn.tangwong.R;
import com.example.krisorn.tangwong.pool_interface;
import com.example.krisorn.tangwong.user_Question;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class carlender extends AppCompatActivity {
    public DatabaseReference nameCard;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    CalendarView calendarView;
    TextView myDate;
    String date;
    String roomid="1";
    int i=0;
    private FirebaseAuth mAuth;
    private String uid;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_carlender);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid ();


        calendarView = (CalendarView) findViewById (R.id.calender);
        myDate = (TextView) findViewById (R.id.myDate);


        calendarView.setOnDateChangeListener (new CalendarView.OnDateChangeListener () {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 date = (month+1)+"/" + dayOfMonth + "/" + year;
                mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.child ("aa").setValue ("test");
                mDatabase.child ("aa").setValue ("dvv");
                nameCard.addValueEventListener(new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        roomid = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        Log.d("aasd", String.valueOf (dataSnapshot.child ("room").child (roomid).child ("event").child ("text").hasChildren ()));

                        int count = (int) dataSnapshot.child ("room").child (roomid).child ("event").getChildrenCount ();
                        Log.d("qweำำ", String.valueOf (count));
                        for(int j=0;j<count;j++)
                        {
                            if(dataSnapshot.child ("room").child (roomid).hasChild ("event"))
                            {
                                if(dataSnapshot.child ("room").child (roomid).child ("event").child (String.valueOf (j)).hasChild ("status"))
                                {

                                    String date2 = dataSnapshot.child ("room").child (roomid).child ("event").child (String.valueOf (j)).child ("date").getValue (String.class);
                                    String message = dataSnapshot.child ("room").child (roomid).child ("event").child (String.valueOf (j)).child ("text").getValue (String.class);
                                    if(date2.equals (date))
                                    {
                                        myDate.setText (message);
                                        mDatabase.child ("eiei").setValue (null);
                                        break;
                                    }
                                    else
                                    {
                                        myDate.setText (" ");
                                    }

                                }
                            }

                        }








                    }

                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });

        nameCard = database.getReference();


        }

    public void click(View view) {
        if(view.getId()==R.id.button2) {
            Intent i = new Intent(this, pool_interface.class);
            startActivity(i);
        }
        else if(view.getId()==R.id.button3) {
            Intent i = new Intent(this, user_Question.class);
            startActivity(i);
        }

    }

    }
