package com.example.krisorn.tangwong;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.krisorn.tangwong.databinding.ActivityProfileExBindingImpl;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

//import android.support.v4.media.app.NotificationCompat;
//import android.support.v4.media.app.NotificationCompat;
//import android.widget.TextView;

public class UsersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public DatabaseReference nameCard;
    private UsersViewModel viewModel;
    private FirebaseAuth mAuth;
    // TODO Step 1: Declare binding instance instead view's (binding class is auto-generated)
    //private TextView textView;
    ActivityProfileExBindingImpl binding;

   /* private TextView Myroom;
    private TextView phoneNumberField;
    private TextView nameField;
    private TextView myJoinRoom;
    private TextView myEmail;*/

    private long roomid;
    private int i=0;
    private int change=0;
    private long turnq=1;
    private String livenow;
    private String id="2";
    private String roomq="1";
    private String tempuid="asd";
    private boolean check=false;
    private long num=0;
    private long newroom=0;
    private  String a = "0";
    private String timestatus = "0";
    private int count = 1;





    private ProgressDialog mProgressDialog;

    private Button mselectImage;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    // [END declare_auth]
    private static final int GALLERY_INTENT =2;

    private BottomNavigationView bottomNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        Log.d("userActivity","oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_ex);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        viewModel = new UsersViewModel(this);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        mProgressDialog= new ProgressDialog(this);
        mStorage=FirebaseStorage.getInstance().getReference();
        // mselectImage=(Button) findViewById(R.id.btn_addImage);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("test").orderByValue ();


        try {
            Log.d("userActivity","picture1");
            //new DownloadImageTask((ImageView) findViewById(R.id.profile_ex)).execute("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fstorage%2Femulated%2F0%2FDCIM%2FCamera%2FIMG_20181216_222350.jpg?alt=media&token=804a1f60-af35-4fe6-beb2-dabf51c3dd5a");
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fstorage%2Femulated%2F0%2FDCIM%2FCamera%2FIMG_20181216_222350.jpg?alt=media&token=804a1f60-af35-4fe6-beb2-dabf51c3dd5a").into((ImageView)findViewById(R.id.profile_ex));
        }
        catch (Exception e){}
        initView();
        //get firebase


        nameCard = database.getReference();

        nameCard.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                try {
                    String uid = user.getUid();


                    if (findViewById(R.id.roomid) != null) {
                        EditText check = findViewById(R.id.roomid);
                        while (true) {
                            if ((!dataSnapshot.child("room").child(check.getText().toString()).child("people_live").child(Long.toString(num)).hasChildren())) {
                                break;
                            } else if (!dataSnapshot.child("room").child(check.getText().toString()).child("people_live").child(Long.toString(num)).getValue().equals(uid)) {
                                break;
                            } else num++;
                        }
                    }

                    newroom = dataSnapshot.child("room").getChildrenCount();





               /* Map map =(Map)dataSnapshot.getValue();
                String name = String.valueOf(map.get("name"));*/
                    if (dataSnapshot.child("user").child(uid).hasChild("keep_noti")) {
                        count = (int) dataSnapshot.child("user").child(uid).child("keep_noti").getChildrenCount();
                        count++;
                    }

                    if (dataSnapshot.child("user").child(uid).child("notification").hasChild("status")) {
                        a = dataSnapshot.child("user").child(uid).child("notification").child("status").getValue((String.class));
                    }

                    if (dataSnapshot.child("user").child(uid).child("time").hasChild("status")) {
                        timestatus = dataSnapshot.child("user").child(uid).child("time").child("status").getValue((String.class));
                    }


                    if (a.equals("1")) {
                        id = dataSnapshot.child("user").child(uid).child("notification").child("room").getValue((String.class));
                        String b = dataSnapshot.child("user").child(uid).child("notification").child("text").getValue((String.class));
                        String roomname = dataSnapshot.child("room").child(id).child("name").getValue((String.class));

                        showNotification(b, roomname);
                        mDatabase.child("user").child(uid).child("notification").child("status").setValue("0");
                        mDatabase.child("room").child(id).child("q").child(roomq).child("noti_status").setValue("0");

                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

                        String H = df.format(c.getTime());

                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("text").setValue(b);
                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("room").setValue(roomname);
                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("time").setValue(H);
                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("status").setValue("ยังไม่ได้ลบ");
                       mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("roomid").setValue(id);


                        check = false;
                    }


                    if (timestatus.equals("1")) {

                        id = dataSnapshot.child("user").child(uid).child("time").child("room").getValue(String.class);
                        mDatabase.child("user").child(uid).child("time").child("status").setValue("0");
                        String timetext = dataSnapshot.child("user").child(uid).child("time").child("text").getValue(String.class);
                        String roomname = dataSnapshot.child("room").child(id).child("name").getValue((String.class));
                        showNotification(timetext, roomname);

                        Calendar c = Calendar.getInstance();

                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

                        String H = df.format(c.getTime());

                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("text").setValue(timetext);
                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("room").setValue(roomname);
                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("time").setValue(H);
                        mDatabase.child("user").child(uid).child("keep_noti").child(String.valueOf(count)).child("status").setValue("ยังไม่ได้ลบ");
                    }



                }catch (Exception e){
                    Intent i = new Intent(UsersActivity.this,EmailPasswordActivity.class);
                    startActivity(i);
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
                        Intent i3 = new Intent(UsersActivity.this,user_roomActivity.class);
                        startActivity(i3);
                        return  true;
                    case R.id.search:
                        Log.d("click","click search");
                        Intent i = new Intent(UsersActivity.this,user_search.class);
                        startActivity(i);

                        //  Toast.makeText(UsersActivity.this,"SEARCH",Toast.LENGTH_SHORT);
                        //jump to activity
                        return  true;
                    case R.id.alert:
                        Log.d("click","click alert");
                        Intent i1 = new Intent(UsersActivity.this,StatusAlert2.class);
                        startActivity(i1);
                        return  true;

                    case R.id.me_profile:
                        Log.d("click","click profile");
                        Intent i2 = new Intent(UsersActivity.this,UsersActivity.class);
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
                UsersActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_user);
        navigationView.setNavigationItemSelectedListener(UsersActivity.this);
        navigationView.bringToFront();
        //end side bar

       nameCard.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String name = dataSnapshot.child("user").child(user.getUid()).child("name").getValue(String.class);

        String pathPhoto = dataSnapshot.child("user").child(user.getUid()).child("pathPhoto").getValue(String.class);
        viewModel.setName(name);

        viewModel.setPathPhoto(pathPhoto);

        binding.nameEx.setText(viewModel.getName());
        mDatabase.child("user").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                binding.countMyRoom.setText(String.valueOf(dataSnapshot.child("live").getChildrenCount()));
                binding.countMyJoinRoom.setText(String.valueOf(dataSnapshot.child("owner").getChildrenCount()));
                binding.myPhoneNumber.setText(dataSnapshot.child("phoneNumber").getValue(String.class));
                binding.myEmail.setText(user.getEmail());
                try {
                    Log.d("userActivity", "picture2");
                    String pathPhoto = dataSnapshot.child("pathPhoto").getValue(String.class);
                    new DownloadImageTask((ImageView) findViewById(R.id.profile_ex)).execute(pathPhoto);
                    Picasso.get().load(pathPhoto).into((ImageView)findViewById(R.id.profile_ex));

                } catch (Exception e) {
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

    private void initView(){

        // FirebaseUser currentUser = mAuth.getCurrentUser();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_ex);
        binding.setViewmodel(viewModel);

    }

    public void click(View view) {



        if(view.getId()==R.id.changeProfilePicture){
            Intent intent =new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }
   /*     else if(view.getId()==R.id.btn_addImage){
            Intent intent =new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent,GALLERY_INTENT);
        }
        else if(view.getId()==R.id.addroom){
            Intent i = new Intent (this,create_event.class);
            startActivity (i);
            //setContentView(R.layout.activity_addroom);

        }
        else if(view.getId()==R.id.notification){
            Intent i = new Intent (this,time.class);
            startActivity (i);

        }

        else if(view.getId()==R.id.q){
            jroomid = findViewById(R.id.roomid);
            FirebaseUser user = mAuth.getCurrentUser();

            mDatabase.child("user").child(user.getUid()).child("live").child(jroomid.getText().toString()).setValue("1");
            mDatabase.child("room").child(jroomid.getText().toString()).child("people_live").child(Long.toString(num)).child("uid").setValue (user.getUid ());

        }
        else if(view.getId()==R.id.enter){
            jroomid = findViewById(R.id.roomid);
            FirebaseUser user = mAuth.getCurrentUser();

            Intent i =new Intent(this,addqActivity.class);
            startActivity(i);

        }

        else if(view.getId()==R.id.addq){

            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("room").child(livenow).child("q").child(Long.toString(turnq)).setValue(user.getUid());

        }
        else if(view.getId()==R.id.createroom){




            mtypeField = findViewById(R.id.type);
            mdataField = findViewById(R.id.data);
            nameField=findViewById(R.id.name);

            mDatabase.child("room").child(Long.toString(newroom)).child("name").setValue(nameField.getText().toString());
            mDatabase.child("room").child(Long.toString(newroom)).child("type").setValue(mtypeField.getText().toString());
            mDatabase.child("room").child(Long.toString(newroom)).child("data").setValue(mdataField.getText().toString());
            FirebaseUser user = mAuth.getCurrentUser();
            mDatabase.child("user").child(user.getUid()).child("owner").child(Long.toString(roomid)).setValue(nameField.getText().toString());
            setContentView(R.layout.activity_users);
        }
        else if(view.getId()==R.id.btn_show_room_user){
            Intent i = new Intent(this,user_roomActivity.class);
            startActivity(i);
        }

       */

    }


    public void signOut(View view) {
        viewModel.setLogoutSatus();
        mAuth.signOut();
        Intent i = new Intent(this,EmailPasswordActivity.class);
        startActivity(i);
    }
    public void test(View view) {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_INTENT && resultCode==RESULT_OK){

            Uri uri=data.getData();
            final StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment()+ UUID.randomUUID().toString());

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

    private void showNotification(String text,String text2) {Intent intent = new Intent(this, AdminDashBoradView.class);
        intent.putExtra("message", text);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(AdminDashBoradView.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(text2)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)

                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);
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
