package com.example.krisorn.tangwong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class create_event extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DatabaseReference nameCard;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    CalendarView calendarView;
    TextView myDate2;
    EditText text;
    String roomid="1";
    String date;
    private FirebaseAuth mAuth;
    private String uid;
   private boolean check = false;
    boolean one=false;
    int i=0;
    private  int j,y;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_create_event);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid ();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child ("aa").setValue ("asd");
        mDatabase.child ("aa").setValue ("ddsa");

        nameCard =database.getReference();
        nameCard.addValueEventListener (new ValueEventListener () {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomid = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
                Log.d("test", String.valueOf (one));
                if(dataSnapshot.child ("room").child (roomid).hasChild ("event"))
                {
                    while(true)
                    {
                        if(!dataSnapshot.child("room").child(roomid).child("event").child (String.valueOf (i)).hasChild("status"))
                        {
                            break;
                        }
                        i++;
                    }
                    if(one==true)
                    {
                        for(int y=0;y<i;y++)
                        {
                            Log.d("test", dataSnapshot.child("room").child(roomid).child("event").child (String.valueOf (y)).child("date").getValue (String.class));

                            Log.d("test", String.valueOf (y));
                            //Log.d("aasd", dataSnapshot.child("room").child(roomid).child("event").child (String.valueOf (j)).child("date").getValue (String.class));
                            if(dataSnapshot.child("room").child(roomid).child("event").child (String.valueOf (y)).child("date").getValue (String.class).equals (date))
                            {
                                Log.d("aasd", "eiei");
                                check = true;
                                j=y;
                                break;
                            }
                            else
                            {
                                check = false;
                                j=-1;
                            }
                        }
                        one = false;

                    }

                    y=j;
                    Log.d("aasd", String.valueOf (y));
                }

            }


            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (month+1)+"/" + dayOfMonth + "/" + year;
                Log.d("aasd", date);
                one = true;
                mDatabase.child ("aa").setValue ("asd");
                mDatabase.child ("aa").setValue ("ddsa");

            }
        });


        calendarView = (CalendarView) findViewById (R.id.calender);
        calendarView.setOnDateChangeListener (new CalendarView.OnDateChangeListener () {



            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date = (month+1)+"/" + dayOfMonth + "/" + year;
                Log.d("aasd", date);
                one = true;
                mDatabase.child ("aa").setValue ("asd");
                mDatabase.child ("aa").setValue ("ddsa");


            }
        });


        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                create_event.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(create_event.this);
        navigationView.bringToFront();
        //end side bar


    }

    public void click(View view) {
        calendarView = (CalendarView) findViewById (R.id.calender);
        myDate2 = (TextView) findViewById (R.id.myDate);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child ("aa").setValue ("asd");
        mDatabase.child ("aa").setValue ("ddsa");

        calendarView.setOnDateChangeListener (new CalendarView.OnDateChangeListener () {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("wrgs", String.valueOf (dataSnapshot.child("room").child(roomid).child("event").child (String.valueOf (i)).child("status").getValue (String.class).equals ("1")));


            }


            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                 date = (month+1)+"/" + dayOfMonth + "/" + year;
                Log.d("aasd", date);

            }
        });


        if(view.getId()==R.id.create_event){


     Log.d("aasd", String.valueOf ("asd"));
            Log.d("wrgs",date);

            text = findViewById (R.id.text);
            mDatabase.child("room").child(roomid).child("event").child (String.valueOf (i)).child("text").setValue(text.getText ().toString ());
            mDatabase.child("room").child(roomid).child("event").child (String.valueOf (i)).child("status").setValue("1");
            mDatabase.child("room").child(roomid).child("event").child (String.valueOf (i)).child("date").setValue(date);



            mDatabase.child ("asd").setValue ("asd");
            mDatabase.child ("asd").setValue ("ddsa");



        }

        else if(view.getId()==R.id.delete){

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child ("asd").setValue ("asd");
            mDatabase.child ("asd").setValue ("ddsa");




            Log.d("aasda", String.valueOf (check));
            Log.d("aasda", String.valueOf (y));

           if(check==true)
            {
                mDatabase.child("room").child(roomid).child("event").child (String.valueOf (y)).child("text").setValue(null);
                mDatabase.child("room").child(roomid).child("event").child (String.valueOf (y)).child("status").setValue(null);
                mDatabase.child("room").child(roomid).child("event").child (String.valueOf (y)).child("date").setValue(null);
                check=false;

            }
            mDatabase.child ("asd").setValue ("asd");
            mDatabase.child ("asd").setValue ("ddsa");


            Intent i = new Intent (this,UsersActivity.class);
            startActivity (i);



        }



    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("can select nav","can select nav");
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_room) {
            Intent i = new Intent(this,user_roomActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_add_room) {
            Intent i = new Intent(this,create_roomActiviity.class);
            startActivity(i);
            return true;

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(this,UsersActivity.class);
            startActivity(i);
            return true;

        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(this,Cart.class);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_qr) {
            Intent i = new Intent(this,user_qrcode.class);
            startActivity(i);
            return true;
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this,Status.class);
            startActivity(i);
            return true;

        }else if(id==R.id.nav_myroom){
            Intent i = new Intent(this,own_room.class);
            startActivity(i);
            return true;
        }else if(id == R.id.nav_logout){
            mAuth.signOut();
            Intent i = new Intent(this,EmailPasswordActivity.class);
            startActivity(i);
            return true;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
