package com.example.krisorn.tangwong;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class user_search extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private BottomNavigationView bottomNavigationView;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_user_search);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference();
        // mselectImage=(Button) findViewById(R.id.btn_addImage);
        mDatabase = FirebaseDatabase.getInstance().getReference();



        //bn_nav
        bottomNavigationView = findViewById(R.id.bottom_nav_user_search);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Log.d("click","click home");
                        Intent i3 = new Intent(user_search.this,user_roomActivity.class);
                        startActivity(i3);
                        return  true;
                    case R.id.search:
                        Log.d("click","click search");
                        Intent i = new Intent(user_search.this,user_search.class);
                        startActivity(i);

                        return  true;
                    case R.id.alert:
                        Log.d("click","click alert");
                        Intent i1 = new Intent(user_search.this,StatusAlert2.class);
                        startActivity(i1);
                        return  true;

                    case R.id.me_profile:
                        Log.d("click","click profile");
                        Intent i2 = new Intent(user_search.this,UsersActivity.class);
                        startActivity(i2);
                        return  true;

                    default:
                        Log.d("click","click .........");
                        return  false;

                }
            }


        });
        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                user_search.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user);
        navigationView.setNavigationItemSelectedListener(user_search.this);
        navigationView.bringToFront();
        //end side bar

    }
    public  void  click(View view){
        if(view.getId() == R.id.ToSearchQRcode){
            Intent i =new Intent(this,user_qrcode.class);
            startActivity(i);
        }else if(view.getId() == R.id.ToSearchId){
            Log.d("haaaaaa","Usersearch");
            Intent i =new Intent(this,user_idSearch.class);
            startActivity(i);}
    }



    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d("can select nav","can select nav");
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_room) {
            Intent i = new Intent(this,user_roomActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_add_room) {
            Intent i = new Intent(this,create_roomActiviity.class);
            startActivity(i);

        } else if (id == R.id.nav_profile) {
            Intent i = new Intent(this,UsersActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_cart) {
            Intent i = new Intent(this,Cart.class);
            startActivity(i);
        } else if (id == R.id.nav_qr) {
            Intent i = new Intent(this,user_qrcode.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(this,Status.class);
            startActivity(i);

        }else if(id==R.id.nav_myroom){
            Intent i = new Intent(this,own_room.class);
            startActivity(i);

        }else if(id == R.id.nav_logout){
            mAuth.signOut();
            Intent i = new Intent(this,EmailPasswordActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

