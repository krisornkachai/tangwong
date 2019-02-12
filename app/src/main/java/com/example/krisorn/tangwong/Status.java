package com.example.krisorn.tangwong;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krisorn.tangwong.Database.Database;
import com.example.krisorn.tangwong.Model.Order;
import com.example.krisorn.tangwong.Model.Request;
import com.example.krisorn.tangwong.status.StatusAdapter;
import com.google.android.gms.common.internal.service.Common;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Status extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    StatusAdapter adapter;
    public int countOrderNow=0;
    private static final String[] MENU =
            {"ถึงคิวแล้ว", "กำลังทำ", "ดูรายการ"};

    String mSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_status);
        recyclerView = (RecyclerView)findViewById(R.id.listStatus);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
Log.d("list data","can create");
loadListItem();
 }



    private void loadListItem(){
    Log.d("list data","can load list");


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Log.d("list1", String.valueOf(countOrderNow));
        try {
            mDatabase.child("user").child(user.getUid()).child("orderNow").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    countOrderNow= (int) dataSnapshot.getChildrenCount();
                    Log.d("list2", String.valueOf(countOrderNow));

                    adapter = new StatusAdapter(countOrderNow,Status.this);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        catch (Exception e ){


        }
        Log.d("list", String.valueOf(countOrderNow));



        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                Status.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(Status.this);
        navigationView.bringToFront();
        //end side bar


    }

    private void showAlertDialog(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(Status.this);
        builder.setTitle("Select Favorite Team");
        builder.setSingleChoiceItems(MENU, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mSelected = MENU[which];
            }
        });
        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                Toast.makeText(getApplicationContext(), "คุณชอบ " +
                        mSelected, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("ไม่ชอบซักทีม", null);
        builder.create();

// สุดท้ายอย่าลืม show() ด้วย
        builder.show();

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
