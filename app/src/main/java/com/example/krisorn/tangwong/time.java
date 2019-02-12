package com.example.krisorn.tangwong;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class time extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference mDatabase;
    private String id ;
    private TimePicker time;
    public DatabaseReference nameCard;
    private String hr,mi;
    private FirebaseAuth mAuth;
    private String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_time);
        time = (TimePicker)findViewById (R.id.timepicker);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        uid = user.getUid ();

        time.setOnTimeChangedListener (new TimePicker.OnTimeChangedListener () {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hr = String.valueOf (hourOfDay);
                mi = String.valueOf (minute);
            }

        });

        //side bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                time.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(time.this);
        navigationView.bringToFront();
        //end side bar

    }

    public void click(View view) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameCard = database.getReference();
        int A;
        nameCard.addListenerForSingleValueEvent (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid = mAuth.getUid ();
                nameCard.child ("temp").setValue (uid);
                id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        if(view.getId()==R.id.set){
           /* EditText hr = findViewById(R.id.hr);
            EditText mi = findViewById(R.id.min);
            EditText se = findViewById(R.id.sec);*/
            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("HH");

            final String H = df.format(c.getTime());


            SimpleDateFormat mm = new SimpleDateFormat("mm");

            final String m = mm.format(c.getTime());
            SimpleDateFormat ss = new SimpleDateFormat("ss");

            final String s = ss.format(c.getTime());


            mDatabase.addListenerForSingleValueEvent (new ValueEventListener () {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    int a = Integer.parseInt (hr);
                    int aa = Integer.parseInt (H);
                    aa=aa*3600;
                    a=a*3600;

                    long b = Integer.parseInt (mi);
                    long bb = Integer.parseInt (m);
                    b=b*60;
                    bb=bb*60;


                    int asdd = Integer.parseInt(String.valueOf (a+b));
                    int asdasd = Integer.parseInt(String.valueOf (aa+bb));

                    int asd = Integer.parseInt(String.valueOf (asdd-asdasd))*1000-(Integer.parseInt(s)*1000);
                    if(asdd-asdasd<=0)
                    {
                        asd = (24*60*60)+Integer.parseInt(String.valueOf (asdd-asdasd));
                        asd = asd*1000;
                    }
                    Log.d("aasdaa", String.valueOf (asd/1000));
                    EditText text = findViewById(R.id.text);
                    id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
                    long num = dataSnapshot.child ("room").child (id).child ("time_notifi").getChildrenCount ();
                    mDatabase.child ("room").child (id).child ("time_notifi").child (String.valueOf (num)).child ("countdown") .setValue (String.valueOf (asd));
                    mDatabase.child ("room").child (id).child ("time_notifi").child (String.valueOf (num)).child ("text") .setValue (text.getText ().toString ());
                    mDatabase.child ("room").child (id).child ("time_notifi").child (String.valueOf (num)).child ("time") .setValue (hr+":"+mi);
                    mDatabase.child ("room").child (id).child ("time_notifi").child (String.valueOf (num)).child ("status") .setValue ("1");
                    mDatabase.child ("room").child (id).child ("time_noti").child ("status") .setValue ("1");

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });





        }else if(view.getId () == R.id.events){
            Intent i = new Intent(time.this,showtime.class);
            startActivity(i);

        }
    }

    private void showNotification(String text) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://devahoy.com/posts/android-notification/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("การเเจ้งเตือน")
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)

                        .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1000, notification);


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