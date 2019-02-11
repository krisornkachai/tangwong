package com.example.krisorn.tangwong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.krisorn.tangwong.AdminDashBorad.AdminDashBoradAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDashBoradView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    AdminDashBoradAdapter adapter;
    public int countOrderNow=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("createAdmin","------");
        super.onCreate(savedInstanceState);

        loadListItem();
        setContentView(R.layout.admin_dash_borad);
        recyclerView = (RecyclerView)findViewById(R.id.list_admin_dashborad);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("list data","can create");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("canClickFab","--------");
                Snackbar.make(view, "create feature", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Intent i = new Intent(view.getContext(),AddFeatureActivity.class);
                startActivity(i);
            }
        });

        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                AdminDashBoradView.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(AdminDashBoradView.this);
        navigationView.bringToFront();
        //end side bar

//        findViewById(R.id.fab).setVisibility(View.INVISIBLE);
//        findViewById(R.id.fab).setVisibility(View.GONE);
      //  findViewById(R.id.layout_add_button).setVisibility(View.INVISIBLE);
        //findViewById(R.id.layout_add_button).setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String roomLiveNow= dataSnapshot.getValue(String.class);
                Log.d("canRetrive","livenow"+roomLiveNow);

                mDatabase.child("user").child(user.getUid()).child("owner").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("canRetrive","livenowin"+roomLiveNow);
                        for(long i = 0 ; i<dataSnapshot.getChildrenCount();i++){
                            if(dataSnapshot.child(String.valueOf(i)).getValue()==roomLiveNow){
                                findViewById(R.id.fab).setVisibility(View.VISIBLE);
                               // findViewById(R.id.fab).setVisibility(View.GONE);
                                Log.d("AdminDashBoradView","myRoom");
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }



    private void loadListItem(){
        Log.d("list data","can load list");


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        Log.d("list1", String.valueOf(countOrderNow));
        try {


            Log.d("listtttttttttttttttt", String.valueOf(countOrderNow));
            mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final String roomLiveNow= dataSnapshot.getValue(String.class);
                    Log.d("canRetrive","livenow");

                    mDatabase.child("room").child(roomLiveNow).child("feature").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            countOrderNow= (int) dataSnapshot.getChildrenCount();
                            Log.d("list2", String.valueOf(countOrderNow));
                            adapter = new AdminDashBoradAdapter(countOrderNow,AdminDashBoradView.this);
                            recyclerView.setAdapter(adapter);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }
        catch (Exception e ){


        }
        Log.d("list", String.valueOf(countOrderNow));





    }

   /* @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        }
        try{
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_own_room);
        drawer.closeDrawer(GravityCompat.START);}
        catch (Exception e){}
        return true;
    }


}
