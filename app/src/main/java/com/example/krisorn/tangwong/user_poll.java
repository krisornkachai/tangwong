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

public class user_poll extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
     private String Question;
     LinearLayout  mLinearLayout ;
     FormBuilder formBuilder;
    private DatabaseReference Polldatabase;
    private FirebaseAuth mAuth;
    private String getKey ;
    private TextView texttemp ;
    private int numBtQuiz = 0;
    List<FormObject> formObjects = new ArrayList<FormObject>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createpoll);
        texttemp = findViewById(R.id.texttemp);
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        Polldatabase = FirebaseDatabase.getInstance().getReference();
        Polldatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getKey = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                long getCount = dataSnapshot.child("room").child(getKey).child("Poll").getChildrenCount();
                long getChoice = dataSnapshot.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Choice").getChildrenCount();
                Log.d("follows",Long.toString(getChoice));
                Question = dataSnapshot.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Topic").getValue(String.class);
                Log.d("follows",Question);
                texttemp.setText(Question);
                mLinearLayout = (LinearLayout) findViewById(R.id.content_poll);
                formBuilder = new FormBuilder(user_poll.this, mLinearLayout);
                  if(dataSnapshot.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Choice").getChildrenCount() >= 1) {
                      for (int i = 0; i < getChoice; i++) {
                          formObjects.add(new FormButton()
                                  .setTitle(dataSnapshot.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Choice").child(Long.toString(i)).child("sub-topic").getValue(String.class))
                                  .setBackgroundColor(Color.parseColor("#6FD06F"))
                                  .setTextColor(Color.WHITE)
                                  .setRunnable(new Runnable() {
                                      @Override
                                      public void run() {
                                          boolean isValid = formBuilder.validate();
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
                user_poll.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(user_poll.this);
        navigationView.bringToFront();
        //end side bar
    }

     public void  click(View v){
        if(v.getId() == R.id.start_question) {
            Toast.makeText(user_poll.this,"คุณสร้างโพลเสร็จเเล้ว",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,AdminDashBoradView.class);
            startActivity(i);

        }else if(v.getId() == R.id.increase_Quize){
            if( numBtQuiz < 1) {
                formObjects.clear();
                formObjects.add(new FormElement().setTag("text").setHint("text").setType(FormElement.Type.TEXT));
                formBuilder.build(formObjects);
                numBtQuiz++;
            }else if((formBuilder.formMap.get("text").getValue().equals(""))==false && (numBtQuiz >= 1) ){
                formObjects.clear();
                mAuth=FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();

                Polldatabase = FirebaseDatabase.getInstance().getReference();
                Polldatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        getKey =  dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                        long getCount =  dataSnapshot.child("room").child(getKey).child("Poll").getChildrenCount();
                        long getChoice = dataSnapshot.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Choice").getChildrenCount();
                        Polldatabase.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Choice").child(Long.toString(getChoice)).child("sub-topic").setValue(formBuilder.formMap.get("text").getValue());
                        Polldatabase.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Choice").child(Long.toString(getChoice)).child ("select").setValue ("0");
                        Polldatabase.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Choice").child (Long.toString(getChoice)).child ("number").setValue ("0");

                        Toast.makeText(user_poll.this,"เพิ่มตัวเลือกเเล้ว",Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(user_poll.this,user_poll.class);
                        startActivity(i);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }else{
                formObjects.clear();
            }
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
