package com.example.krisorn.tangwong;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class pool_interface extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String Question;
    LinearLayout  mLinearLayout ;
    FormBuilder formBuilder;
    private DatabaseReference Polldatabase;
    public FirebaseAuth mAuth;
    private String getKey,getcount ;
    private  String temp ;
    private long max = 0 ;
    private TextView topic;
    private long number = 0;
    private ProgressDialog mProgressDialog;
    List<FormObject> formObjects = new ArrayList<FormObject>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_pool_interface);
                topic = findViewById(R.id.texttemp);
                mAuth= FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                Polldatabase = FirebaseDatabase.getInstance().getReference();
                Polldatabase.addListenerForSingleValueEvent (new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        getKey = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                        getcount = dataSnapshot.child("room").child(getKey).child("showPoll").getValue(String.class);
                        max = dataSnapshot.child("room").child(getKey).child("Poll").getChildrenCount();

                        long getChoice = dataSnapshot.child("room").child(getKey).child("Poll").child(getcount).child("Choice").getChildrenCount();

                Question = dataSnapshot.child("room").child(getKey).child("Poll").child(getcount).child("Topic").getValue(String.class);

                mLinearLayout = (LinearLayout) findViewById(R.id.poll_interface);
                formBuilder = new FormBuilder(pool_interface.this, mLinearLayout);
                formObjects.clear();
                topic.setText(Question);
                if(dataSnapshot.child("room").child(getKey).child("Poll").child(getcount).child("Choice").getChildrenCount() >= 1) {
                    for (int i = 0; i < getChoice; i++) {
                        final int finalI = i;
                        formObjects.add(new FormButton()
                                .setTitle(dataSnapshot.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(i)).child("sub-topic").getValue(String.class))
                                .setBackgroundColor(Color.CYAN)
                                .setTextColor(Color.WHITE)
                                .setRunnable(new Runnable() {
                                    @Override
                                    public void run() {

                                        boolean isValid = formBuilder.validate();
                                        if(isValid){
                                            Polldatabase.addListenerForSingleValueEvent (new ValueEventListener () {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child ("select").getValue ()=="1"){
                                                        Toast.makeText(pool_interface.this,"You didn't choose : " + dataSnapshot.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child("sub-topic").getValue(String.class),Toast.LENGTH_LONG).show();
                                                        Polldatabase.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child ("select").setValue ("0");
                                                        String gettemp = dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (getcount)).child("Choice").child (String.valueOf (finalI)).child ("number").getValue (String.class);
                                                        long integernumber = Integer.parseInt(gettemp);
                                                        integernumber--;
                                                        Log.d ("GGOO",String.valueOf (integernumber)+ "value");
                                                        Polldatabase.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child ("number").setValue (String.valueOf (integernumber));
                                                    }else {
                                                        String gettemp = dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (getcount)).child("Choice").child (String.valueOf (finalI)).child ("number").getValue (String.class);
                                                        long integernumber = Integer.parseInt(gettemp);
                                                        integernumber++;
                                                        Toast.makeText(pool_interface.this,"You choose : " + dataSnapshot.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child("sub-topic").getValue(String.class),Toast.LENGTH_LONG).show();
                                                        Polldatabase.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child ("select").setValue ("1");
                                                        Polldatabase.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child ("number").setValue (String.valueOf (integernumber));

                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }else {
                                            Polldatabase.child("room").child(getKey).child("Poll").child(getcount).child("Choice").child(Long.toString(finalI)).child ("select").setValue ("0");
                                        }
                                    }
                                })
                        );
                    }
                }

                formBuilder.build(formObjects);

                formObjects.clear();
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
                pool_interface.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(pool_interface.this);
        navigationView.bringToFront();
        //end side bar
    }
    public void  click(View v){

        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        Polldatabase = FirebaseDatabase.getInstance().getReference();
        Polldatabase.addListenerForSingleValueEvent (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getKey = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                temp = dataSnapshot.child("room").child(getKey).child("showPoll").getValue (String.class);
                number = Integer.parseInt(temp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(v.getId() == R.id.bn_next)
            Polldatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d("follows", String.valueOf(number) + "num");
                    Log.d("follows", String.valueOf(max) + "max");
                    if (number < max) {
                        getKey = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                        number++;
                        Polldatabase.child("room").child(getKey).child("showPoll").setValue(String.valueOf(number));
                        formObjects.clear();
                        Intent i = new Intent(pool_interface.this, pool_interface.class);
                        startActivity(i);
                    } else if (number == max) {
                        Intent i = new Intent(pool_interface.this, pool_interfacelast.class);
                        startActivity(i);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        else if(v.getId() == R.id.bn_previous){
            Log.d("follows",String.valueOf (number));
            Log.d("follows","0000");

                Log.d("follows","1111");
                Polldatabase.addListenerForSingleValueEvent (new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("follows",String.valueOf (number)+"num");
                        Log.d("follows",String.valueOf (max)+"max");
                        if(number > 1) {
                            getKey = dataSnapshot.child ("user").child (user.getUid ()).child ("livenow").getValue (String.class);
                            Log.d ("follows", "22222");
                            number--;
                            Polldatabase.child ("room").child (getKey).child ("showPoll").setValue (String.valueOf (number));
                            formObjects.clear ();
                            Intent i = new Intent (pool_interface.this, pool_interface.class);
                            startActivity (i);
                            Log.d ("follows", String.valueOf (number));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


        }
        formObjects.clear();

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