package com.example.krisorn.tangwong;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
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
import android.widget.Toast;
//import android.widget.TextView;

import com.example.krisorn.tangwong.databinding.ActivityUsersBindingImpl;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.toUnsignedString;

public class UsersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public DatabaseReference nameCard;
    private UsersViewModel viewModel;
    private FirebaseAuth mAuth;
    // TODO Step 1: Declare binding instance instead view's (binding class is auto-generated)
    //private TextView textView;
    ActivityUsersBindingImpl binding;

    private EditText mtypeField;
    private EditText mdataField;
    private EditText nameField;


    private EditText jroomid;
    private long roomid;
    private int i=0;
    private int change=0;
    private long turnq=1;
    private String livenow;



    private ProgressDialog mProgressDialog;

    private Button mselectImage;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    // [END declare_auth]
    private static final int GALLERY_INTENT =2;

    private BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        viewModel = new UsersViewModel(this);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


         //bn_nav
        mProgressDialog= new ProgressDialog(this);
        mStorage=FirebaseStorage.getInstance().getReference();
       // mselectImage=(Button) findViewById(R.id.btn_addImage);

        mDatabase = FirebaseDatabase.getInstance().getReference();

         try {
             new DownloadImageTask((ImageView) findViewById(R.id.profile)).execute("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fstorage%2Femulated%2F0%2FDCIM%2FCamera%2FIMG_20181216_222350.jpg?alt=media&token=804a1f60-af35-4fe6-beb2-dabf51c3dd5a");
         }
         catch (Exception e){}
        initView();
        //get firebase


        nameCard = database.getReference();

        nameCard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String uid = user.getUid();
               /* Map map =(Map)dataSnapshot.getValue();
                String name = String.valueOf(map.get("name"));*/
              String name=dataSnapshot.child("user").child(uid).child("name").getValue(String.class);

             String pathPhoto=dataSnapshot.child("user").child(uid).child("pathPhoto").getValue(String.class);
              viewModel.setName(name);

              viewModel.setPathPhoto(pathPhoto);


              binding.name.setText(viewModel.getName());

              try {
                  new DownloadImageTask((ImageView) findViewById(R.id.profile)).execute(pathPhoto);
              }
              catch (Exception e) {
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //bn_nav
        bottomNavigationView = findViewById(R.id.bottom_nav);
        //bottomNavigationView.setOnNavigationItemReselectedListener((BottomNavigationView.OnNavigationItemReselectedListener) mOnNavigationItemSelectedListener);
        Log.d("cancreateNavigation","can create navigation");
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Log.d("click","click home");
                        //  Toast.makeText(UsersActivity.this,"HOME",Toast.LENGTH_SHORT);
                        //jump to activity
                        return  true;
                    case R.id.search:
                        Log.d("click","click search");
                        Intent i = new Intent(UsersActivity.this,Cart.class);
                        startActivity(i);

                        //  Toast.makeText(UsersActivity.this,"SEARCH",Toast.LENGTH_SHORT);
                        //jump to activity
                        return  true;
                    case R.id.alert:
                        Log.d("click","click alert");
                        //   Toast.makeText(UsersActivity.this,"ALERT",Toast.LENGTH_SHORT);
                        //jump to activity
                        return  true;

                    case R.id.me_profile:
                        Log.d("click","click profile");
                        //  Toast.makeText(UsersActivity.this,"PROFLIE",Toast.LENGTH_SHORT);
                        //jump to activity
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
                UsersActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user);
        navigationView.setNavigationItemSelectedListener(UsersActivity.this);
        navigationView.bringToFront();
        //end side bar



    }

    private void initView(){

       // FirebaseUser currentUser = mAuth.getCurrentUser();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        binding.setViewmodel(viewModel);

    }

    public void click(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(view.getId()==R.id.button){
        int current=parseInt(viewModel.getString(),10);
        current++;
        String strCurrent= String.valueOf(current);

        viewModel.setString(strCurrent);
        binding.textView2.setText(viewModel.getString());
        }
        else if(view.getId()==R.id.btn_addImage){
            Intent intent =new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }
        else if(view.getId()==R.id.addroom){
            setContentView(R.layout.activity_addroom);

        }

        else if(view.getId()==R.id.q){
            jroomid = findViewById(R.id.roomid);
            FirebaseUser user = mAuth.getCurrentUser();

                mDatabase.child("user").child(user.getUid()).child("live").child(jroomid.getText().toString()).setValue("1");



            setContentView(R.layout.activity_addq);

        }
        else if(view.getId()==R.id.enter){
            jroomid = findViewById(R.id.roomid);
            FirebaseUser user = mAuth.getCurrentUser();

         //   mDatabase.child("user").child(user.getUid()).child("nowlive").setValue(jroomid.getText().toString());


            Intent i =new Intent(this,addqActivity.class);
            startActivity(i);
         //   setContentView(R.layout.activity_addq);


        }

        else if(view.getId()==R.id.addq){

            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("room").child(livenow).child("q").child(Long.toString(turnq)).setValue(user.getUid());

        }
        else if(view.getId()==R.id.createroom){




                    mtypeField = findViewById(R.id.type);
            mdataField = findViewById(R.id.data);
            nameField=findViewById(R.id.name);

            mDatabase.child("room").child(Long.toString(roomid)).child("name").setValue(nameField.getText().toString());
            mDatabase.child("room").child(Long.toString(roomid)).child("type").setValue(mtypeField.getText().toString());
            mDatabase.child("room").child(Long.toString(roomid)).child("data").setValue(mdataField.getText().toString());
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("user").child(user.getUid()).child("owner").child(Long.toString(roomid)).setValue(nameField.getText().toString());
            setContentView(R.layout.activity_users);
        }
        else if(view.getId()==R.id.btn_show_room_user){
            Intent i = new Intent(this,user_roomActivity.class);
            startActivity(i);
        }
    }
    public void signOut(View view) {
        viewModel.setLogoutSatus();
        mAuth.signOut();
        Intent i = new Intent(this,EmailPasswordActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        tempData =data;
        tempRequestCode=requestCode;
        tempResultCode=resultCode;
        */

        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){

            Uri uri=data.getData();
            final StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());
           // mProgressDialog.setMessage("Uploading....");
            mProgressDialog.setMessage("uploading....");
            mProgressDialog.show();

           Log.d("11111111","11111111");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("uploadscuccess", "onSuccess: uri= "+ uri.toString());
                            String url = uri.toString();

                            mDatabase.child("user").child(mAuth.getCurrentUser().getUid()).child("pathPhoto").setValue(url);
                            Toast.makeText(UsersActivity.this,"upload Done",Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }
                    });
                }
            });

        }

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
        } else if (id == R.id.nav_qr) {
            Intent i = new Intent(this,user_qrcode.class);
            startActivity(i);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_user);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
