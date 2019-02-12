package com.example.krisorn.tangwong;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
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

public class create_itemActiviity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ProgressDialog mProgressDialog;
    public TextView txt_data;
    public TextView txt_name_room;
    public TextView txt_type_room;
    public ImageView img_room;
    public String roomid;
    public Long count_menu;
    public Button btn_create_room;
    public Button btn_add_img_room;
    private static final int GALLERY_INTENT =2;
    private String url =null;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mProgressDialog= new ProgressDialog(this);
        txt_data = findViewById(R.id.txt_create_item_detail);
        txt_name_room = findViewById(R.id.txt_create_item_name);
        txt_type_room=findViewById(R.id.txt_create_item_type);
        btn_create_room=(Button)findViewById(R.id.btn_create_item);
        btn_add_img_room=(Button)findViewById(R.id.btn_add_item);
        img_room=(ImageView)findViewById(R.id.img_add_item);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                roomid = dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                Log.d("crateRoomid", roomid);
                count_menu=dataSnapshot.child("room").child(roomid).child("menu").getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_create_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("roomid",roomid);
                //  mDatabase.child("room").child(roomid).child("menu").child(String.valueOf(count_menu)).child("pathPhoto").setValue("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fwww.maxpixel.net-Taxes-Control-Smart-Home-Icon-Technology-Home-3317459.png?alt=media&token=329741c8-000f-4c85-bd9f-ca5b4e99442d");
                mDatabase.child("room").child(roomid).child("menu").child(String.valueOf(count_menu)).child("name").setValue(txt_name_room.getText().toString());
                mDatabase.child("room").child(roomid).child("menu").child(String.valueOf(count_menu)).child("price").setValue(txt_type_room.getText().toString());
                mDatabase.child("room").child(roomid).child("menu").child(String.valueOf(count_menu)).child("detail").setValue(txt_data.getText().toString());
                mDatabase.child("room").child(roomid).child("menu").child(String.valueOf(count_menu)).child("pathPhoto").setValue(url);
                showAlertDialog();

            }
        });

        btn_add_img_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
//side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                create_itemActiviity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(create_itemActiviity.this);
        navigationView.bringToFront();
        //end side bar


        bottomNavigationView = findViewById(R.id.bottom_add_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Log.d("click","click home");
                        Intent i3 = new Intent(create_itemActiviity.this,user_roomActivity.class);
                        startActivity(i3);
                        return  true;
                    case R.id.search:
                        Log.d("click","click search");
                        Intent i = new Intent(create_itemActiviity.this,user_search.class);
                        startActivity(i);

                        return  true;
                    case R.id.alert:
                        Log.d("click","click alert");
                        Intent i1 = new Intent(create_itemActiviity.this,StatusAlert2.class);
                        startActivity(i1);
                        return  true;

                    case R.id.me_profile:
                        Log.d("click","click profile");
                        Intent i2 = new Intent(create_itemActiviity.this,UsersActivity.class);
                        startActivity(i2);
                        return  true;

                    default:
                        Log.d("click","click .........");
                        return  false;

                }
            }


        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){

            Uri uri=data.getData();
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            final StorageReference filepath = storageReference.child("Photos").child(uri.getLastPathSegment()+ UUID.randomUUID());

            mProgressDialog.setMessage("Uploading....");
            mProgressDialog.show();
            Log.d("11111111","11111111");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("uploadscuccess", "onSuccess: uri= "+ uri.toString());
                            url = uri.toString();

                            Picasso.get().load(url).into(img_room);
                            Toast.makeText(create_itemActiviity.this,"upload Done",Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
            });

        }

    }

    private void showAlertDialog() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(create_itemActiviity.this);
        alertDialog.setTitle("สำเร็จ");
        alertDialog.setMessage("การสร้างห้องสำเร็จแล้ว");

        Log.d("showAlertDialog","showAlertDialog");
        final EditText edtAddress = new EditText(create_itemActiviity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);


        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });


        alertDialog.show();
    }

}