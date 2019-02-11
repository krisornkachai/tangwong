package com.example.krisorn.tangwong;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krisorn.tangwong.Database.Database;
import com.example.krisorn.tangwong.Model.Request;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.UUID;

public class room_information extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ProgressDialog mProgressDialog;
    public TextView txt_name;
    public TextView txt_id_room;
    public TextView txt_data_room;
    public ImageView img_room;
    public String roomid;
    public Long count_own_room_count;
    public Button btn_genqr_room;
    public Button btn_add_img_room;
    private static final int GALLERY_INTENT =2;
    private String url =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_information);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        txt_name = findViewById(R.id.txt_create_room_detail);
        txt_id_room = findViewById(R.id.txt_create_room_name);
        txt_data_room=findViewById(R.id.txt_create_room_type);
        btn_genqr_room=(Button)findViewById(R.id.btn_create_room);
      //  btn_add_img_room=(Button)findViewById(R.id.btn_add_room);
        img_room=(ImageView)findViewById(R.id.img_room);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               roomid = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
               Log.d("room_info","roomid"+roomid);

               mDatabase.child("room").child(roomid).addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    txt_id_room.setText(roomid);
                    txt_data_room.setText(dataSnapshot.child("data").getValue(String.class));
                    txt_name.setText(dataSnapshot.child("name").getValue(String.class));
                    Picasso.get().load(dataSnapshot.child("photoPath").getValue(String.class)).into(img_room);
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

        btn_genqr_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                Log.d("roomid",Long.toString(roomid));
                mDatabase.child("room").child(Long.toString(roomid)).child("photoPath").setValue("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fwww.maxpixel.net-Taxes-Control-Smart-Home-Icon-Technology-Home-3317459.png?alt=media&token=329741c8-000f-4c85-bd9f-ca5b4e99442d");
                mDatabase.child("room").child(Long.toString(roomid)).child("name").setValue(txt_name_room.getText().toString());
                mDatabase.child("room").child(Long.toString(roomid)).child("type").setValue(txt_type_room.getText().toString());
                mDatabase.child("room").child(Long.toString(roomid)).child("data").setValue(txt_data.getText().toString());
                mDatabase.child("room").child(Long.toString(roomid)).child("photoPath").setValue(url);

                mDatabase.child("user").child(user.getUid()).child("owner").child(String.valueOf(count_own_room_count)).setValue((Long.toString(roomid)));


*/
            }
        });



        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //      NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user);
//        navigationView.setNavigationItemSelectedListener(this);
//        navigationView.bringToFront();
        //end side bar

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

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
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_myroom) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
