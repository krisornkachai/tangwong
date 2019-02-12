package com.example.krisorn.tangwong;

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
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class show_data extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference Polldatabase;
    public FirebaseAuth mAuth;
    private String getKey ;
    LinearLayout  mLinearLayout ;
    FormBuilder formBuilder;
    private String Question;
    private long getcount = 0;
    List<FormObject> formObjects = new ArrayList<FormObject> ();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_showdata);
        mAuth= FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        Polldatabase = FirebaseDatabase.getInstance().getReference();
        Polldatabase.addListenerForSingleValueEvent (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getKey = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                getcount = dataSnapshot.child("room").child(getKey).child("Poll").getChildrenCount();
                Log.d("GGOO",String.valueOf (getcount));
                formObjects.clear();
                for(int i = 1 ; i <= getcount ;i++){
                    long getChoice = dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Choice").getChildrenCount();
                    Question = dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Topic").getValue(String.class);
                    mLinearLayout = (LinearLayout) findViewById(R.id.interface_show);
                    formBuilder = new FormBuilder(show_data.this, mLinearLayout);
                    formObjects.add(new FormButton()
                            .setTitle(Question)
                            .setBackgroundColor(Color.GRAY)
                            .setTextColor(Color.WHITE)
                            .setRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("Forms", formBuilder.formMap.toString());
                                }
                            })
                    );

                    for (int j = 0;j < getChoice;j++){
                        Log.d("GGOO",dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Choice").child (String.valueOf (j)).child ("select").getValue (String.class)+" GET");
                        if(dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Choice").child (String.valueOf (j)).child ("select").getValue (String.class).equals ("1") ) {
                            String gettemp = dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Choice").child (String.valueOf (j)).child ("number").getValue (String.class);
                            Polldatabase.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Choice").child (String.valueOf (j)).child ("number").setValue (String.valueOf (gettemp ));
                        }
                    }
                    for (int j = 0;j < getChoice;j++){
                        String data = dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Choice").child(Long.toString(j)).child("sub-topic").getValue(String.class) ;
                        String number = dataSnapshot.child("room").child(getKey).child("Poll").child(String.valueOf (i)).child("Choice").child (Long.toString(j)).child ("number").getValue (String.class);
                        Log.d("GGOO",data+" data");
                        Log.d("GGOO",number+" number");
                        formObjects.add(new FormButton()
                                .setTitle(String.valueOf(j)+" "+data +" ถูกเลือกจำนวน : " + number +" คน")
                                .setBackgroundColor(Color.alpha(10))
                                .setTextColor(Color.WHITE)
                                .setRunnable(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i("Forms", formBuilder.formMap.toString());
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
                show_data.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(show_data.this);
        navigationView.bringToFront();
        //end side bar

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
