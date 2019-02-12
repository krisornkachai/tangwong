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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class notification extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseReference mDatabase;
    private String id="2";

    private FirebaseAuth mAuth;
    public DatabaseReference nameCard;
    private String iduser;
    private String uid;
    private Spinner name;
    private  List<String> namelist = new ArrayList<String>();
    private boolean check = false;
    private int ALL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_notification);
        mAuth = FirebaseAuth.getInstance();



        name = (Spinner) findViewById(R.id.list);




        final FirebaseUser user = mAuth.getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        uid = user.getUid ();
/*        mDatabase.child ("asd").setValue ("asd");
        mDatabase.child ("asd").setValue ("ddsa");*/
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameCard =database.getReference();
        name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iduser = String.valueOf (position);
                Log.d("aaaaaaaaa",iduser);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        nameCard.addValueEventListener (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);

                int loop = (int) dataSnapshot.child ("room").child (id).child ("people_live").getChildrenCount ();
                ALL=loop;
                Log.d("follows",String.valueOf (loop));
                if(check==false)
                {
                    namelist.add("Select");
                    for (int i=1;i<=loop;i++)
                    {
                        String tempuid = dataSnapshot.child ("room").child (id).child ("people_live").child (String.valueOf (i)).child ("uid").getValue (String.class);
                        String tempname = dataSnapshot.child ("user").child (tempuid).child ("name").getValue (String.class);
                        namelist.add(String.valueOf (i)+" "+tempname);
                    }
                    namelist.add("All");
                    check=true;
                }




                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(notification.this, android.R.layout.simple_spinner_item, namelist);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                // attaching data adapter to spinner
                name.setAdapter(dataAdapter);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });

        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                notification.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(notification.this);
        navigationView.bringToFront();
        //end side bar

    }

    public void click(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if(view.getId()==R.id.setnoti){
            // EditText roomq = findViewById (R.id.q);
            final int a = ALL+1;
            Log.d("saaaaaaaaa", String.valueOf (a));
            Log.d("saaaaaaaaa", iduser);
            if(iduser.equals (String.valueOf(a)))
            {

                for (long i=1;i<=ALL;i++)
                {
                    Log.d("saaaaaaaaa", "eieieei");
                    mDatabase.child ("room").child (id).child ("people_live").child (String.valueOf (i)).child ("noti_status").setValue ("1");
                }
            }
            else if(!(iduser.equals (String.valueOf(0))))
            {
                mDatabase.child ("room").child (id).child ("people_live").child (iduser).child ("noti_status").setValue ("1");
            }

            FirebaseDatabase database2 = FirebaseDatabase.getInstance();


            //nameCard =database.getReference();
            mDatabase.child("eiei").setValue("asdasd");
            mDatabase.child("eiei").setValue("dsacfvf");
            //mDatabase.child ("room").child (id).child ("q").child (roomq).child ("uid").setValue ("0");

            nameCard = database.getReference();

            nameCard.addListenerForSingleValueEvent (new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //final FirebaseUser user = mAuth.getCurrentUser ();
                    //String uid = user.getUid ();
                    // String iduser = String.valueOf(name.getId ());

                    EditText message = findViewById (R.id.text);

                    //  Log.d("aasd",dataSnapshot.child ("room").child (id).child ("q").child (iduser).child ("noti_status").getValue ((String.class)));
                    if(!(iduser.equals (String.valueOf(0)))) {

                        if (iduser.equals (String.valueOf (a))) {
                            for (long i = 1; i <= ALL; i++) {
                                if (dataSnapshot.child ("room").child (id).child ("people_live").child (String.valueOf (i)).child ("noti_status").getValue ((String.class)).equals ("1")) {
                                    String tempuid = dataSnapshot.child ("room").child (id).child ("people_live").child (String.valueOf (i)).child ("uid").getValue ((String.class));
                                    mDatabase.child ("user").child (tempuid).child ("notification").child ("status").setValue ("1");
                                    mDatabase.child ("user").child (tempuid).child ("notification").child ("text").setValue (message.getText ().toString ());
                                    mDatabase.child ("user").child (tempuid).child ("notification").child ("room").setValue (id);
                                }
                            }
                        } else {
                            if (dataSnapshot.child ("room").child (id).child ("people_live").child (iduser).child ("noti_status").getValue ((String.class)).equals ("1")) {
                                //roomq = findViewById(R.id.roomid).toString ();
                                String tempuid = dataSnapshot.child ("room").child (id).child ("people_live").child (iduser).child ("uid").getValue ((String.class));

                                Log.d ("aasd", tempuid);
                                // mDatabase.child ("room").child (id).child ("q").child (roomq.getText ().toString ()).child ("text").setValue (message.getText ().toString ());
                                mDatabase.child ("user").child (tempuid).child ("notification").child ("status").setValue ("1");
                                mDatabase.child ("user").child (tempuid).child ("notification").child ("text").setValue (message.getText ().toString ());
                                mDatabase.child ("user").child (tempuid).child ("notification").child ("room").setValue (id);
                            }
                        }
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


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