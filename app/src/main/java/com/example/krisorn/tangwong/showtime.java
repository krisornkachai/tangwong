package com.example.krisorn.tangwong;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class showtime extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TimePicker time;
    public DatabaseReference nameCard;
    private String hr,mi;
    private ListView lvItems;
    private List<Product> lstProducts;
    private String id ;
    private String uid;
    private DatabaseReference mDatabase;
    private String sendtext;
    private  boolean check = true;
    private long numall ;
    private String showtime ;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtime);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        nameCard = database.getReference();
        lvItems = (ListView) findViewById(R.id.lvItems);



        lstProducts = new ArrayList<> ();
        Log.d ("GGGG","pass");
        nameCard.addListenerForSingleValueEvent (new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d ("GGGG","passDATA");
                uid = dataSnapshot.child ("temp").getValue (String.class);
                id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);














                // เดี๋ยววนตามจำนวนnotfi
                long value_child_notifi = dataSnapshot.child ("room").child (id).child ("time_notifi").getChildrenCount ();

                 for(int i = 0 ;i < value_child_notifi;i++){
                     if(dataSnapshot.child ("room").child (id).child ("time_notifi").hasChild (String.valueOf (i)))
                     {
                         String count = dataSnapshot.child ("room").child (id).child ("time_notifi").child (String.valueOf (i)).child ("countdown") .getValue (String.class).toString ();
                         String text =  dataSnapshot.child ("room").child (id).child ("time_notifi").child (String.valueOf (i)).child ("text") .getValue (String.class).toString ();
                         String time  = dataSnapshot.child ("room").child (id).child ("time_notifi").child (String.valueOf (i)).child ("time") .getValue (String.class);

                         lstProducts.add(new Product(text, System.currentTimeMillis() + Integer.parseInt(count) ,time));

                         Log.d ("GGGG",count);
                     }
                     else
                     {
                         value_child_notifi++;
                     }

                 }
                lvItems.setAdapter(new CountdownAdapter(showtime.this, lstProducts));
                numall = value_child_notifi;

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
                showtime.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(showtime.this);
        navigationView.bringToFront();
        //end side bar

    }

    private class Product {
        String name;
        long expirationTime;
        String time;

        public Product(String name, long expirationTime , String time) {
            this.name = name;
            this.expirationTime = expirationTime;
            this.time = time;
        }
    }


    public class CountdownAdapter extends ArrayAdapter<Product> {

        private LayoutInflater lf;
        private List<ViewHolder> lstHolders;
        private Handler mHandler = new Handler ();
        private Runnable updateRemainingTimeRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (lstHolders) {
                    long currentTime = System.currentTimeMillis();
                    for (ViewHolder holder : lstHolders) {
                        holder.updateTimeRemaining(currentTime);
                    }
                }
            }
        };

        public CountdownAdapter(showtime context, List<Product> objects) {
            super(context, 0, objects);
            lf = LayoutInflater.from(context);
            lstHolders = new ArrayList<>();
            startUpdateTimer();
        }

        private void startUpdateTimer() {
            Timer tmr = new Timer();
            tmr.schedule(new TimerTask () {
                @Override
                public void run() {
                    mHandler.post(updateRemainingTimeRunnable);
                }
            }, 1000, 1000);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = lf.inflate(R.layout.activity_showtime, parent, false);
                holder.tvProduct = (TextView) convertView.findViewById(R.id.tvProduct);
                holder.tvTimeRemaining = (TextView) convertView.findViewById(R.id.tvTimeRemaining);
                convertView.setTag(holder);
                synchronized (lstHolders) {
                    lstHolders.add(holder);
                }
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.setData(getItem(position));

            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvProduct;
        TextView tvTimeRemaining;
        Product mProduct;

        public void setData(Product item) {
            mProduct = item;
            tvProduct.setText(item.name);
            updateTimeRemaining(System.currentTimeMillis());
        }

        public void updateTimeRemaining(long currentTime) {
            long timeDiff = mProduct.expirationTime - currentTime;
            if (timeDiff > 0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                tvTimeRemaining.setText(mProduct.time);
            } else {
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
               /* mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child ("room").child (id).child ("time_noti").child ("status") .setValue ("1");
                mDatabase.child ("room").child (id).child ("time_noti").child ("text") .setValue (mProduct.name);*/

                nameCard.addListenerForSingleValueEvent (new ValueEventListener () {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        uid = dataSnapshot.child ("temp").getValue (String.class);
                        id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
                        int asd =0;
                        while(asd<numall)
                        {
                            if(dataSnapshot.child ("room").child (id).child ("time_notifi").hasChild (String.valueOf (asd)))
                            {
                                if(dataSnapshot.child ("room").child (id).child ("time_notifi").child (String.valueOf (asd)).child ("text").getValue (String.class).equals (mProduct.name))
                                {
                                    break;
                                }
                            }


                            asd++;
                        }



            Log.d("asd",id+"+"+uid);
                        long num = dataSnapshot.child ("room").child (id).child ("people_live").getChildrenCount ();
                        Log.d("asd","+"+num);

                        if(dataSnapshot.child ("room").child (id).hasChild ("time_noti")&&asd<numall) {
                            String text = dataSnapshot.child ("room").child (id).child ("time_notifi").child ("text").getValue (String.class);
                             for (long i = 1; i <= num; i++) {
                                 if(dataSnapshot.child ("room").child (id).child ("time_notifi").child (String.valueOf (asd)).child ("status").getValue (String.class).equals ("1")) {

                                     if (dataSnapshot.child ("room").child (id).child ("people_live").child (Long.toString (i)).child ("uid").getValue (String.class) != null) {
                                        String tempUid = dataSnapshot.child ("room").child (id).child ("people_live").child (Long.toString (i)).child ("uid").getValue (String.class);
                                       Log.d("asdd", String.valueOf (i));



                                       // Log.d("asd","+"+text);
                                         mDatabase.child ("user").child (tempUid).child ("time").child ("status").setValue ("1");
                                        mDatabase.child ("user").child (tempUid).child ("time").child ("room").setValue (id);
                                        mDatabase.child ("user").child (tempUid).child ("time").child ("text").setValue (mProduct.name);
                                    }
                                }
                                }
                            mDatabase.child ("room").child (id).child ("time_notifi").child (String.valueOf (asd)).child ("status") .setValue ("0");
                            mDatabase.child ("room").child (id).child ("time_notifi").child (String.valueOf (asd)).setValue (null);
                            Intent i = new Intent(showtime.this,showtime.class);
                            startActivity(i);



                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                tvTimeRemaining.setText("หมดเวลาเเล้ว");
            }
        }

        public void sendnoti(final String id2, String text) {

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