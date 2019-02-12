package com.example.krisorn.tangwong;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krisorn.tangwong.Database.Database;
import com.example.krisorn.tangwong.Model.Order;
import com.example.krisorn.tangwong.Model.Request;
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

import static com.facebook.FacebookSdk.getApplicationContext;

public class Cart extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private BottomNavigationView bottomNavigationView;
    FirebaseDatabase database;
    DatabaseReference request;

    TextView txtTotalPrice;
    Button btnPlace;

    private static final String[] MENU =
            {"ถึงคิวแล้ว", "กำลังทำ", "ดูรายการ"};

    String mSelected;

    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String livenow;
    private long turnq=0;
    private long noOfOder =0;
    String name = null;
    String phoneNumber= null;
    String roomNumber= null;
    String nameuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("database","Oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Firebase
        database = FirebaseDatabase.getInstance();
        request = database.getReference();

        //Init

        recyclerView = (RecyclerView)findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView)findViewById(R.id.total);
        btnPlace = (Button)findViewById(R.id.btnPlaceOrder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","btnplaceClick");
                showAlertDialog();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("user").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                phoneNumber= dataSnapshot.child("phoneNumber").getValue(String.class);
                livenow=dataSnapshot.child("livenow").getValue(String.class);
                // roomNumber=livenow;


                try {
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                turnq = dataSnapshot.child("room").child(livenow).child("q").child("queue").getChildrenCount();
                                name = dataSnapshot.child("room").child(livenow).child("name").getValue(String.class);
                                nameuser = dataSnapshot.child("user").child(user.getUid()).child("name").getValue(String.class);
                                noOfOder = dataSnapshot.child("user").child(user.getUid()).child("orderNow").getChildrenCount();
                                Log.d("trunq", "trunq !!!!!" + turnq);
                            }catch (Exception e){}





                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }


                    });
                }catch (Exception e){}




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }


        });
        loadListItem();

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        Log.d("click","click home");
                        Intent i3 = new Intent(Cart.this,user_roomActivity.class);
                        startActivity(i3);
                        return  true;
                    case R.id.search:
                        Log.d("click","click search");
                        Intent i = new Intent(Cart.this,user_search.class);
                        startActivity(i);

                        return  true;
                    case R.id.alert:
                        Log.d("click","click alert");
                        Intent i1 = new Intent(Cart.this,StatusAlert2.class);
                        startActivity(i1);
                        return  true;

                    case R.id.me_profile:
                        Log.d("click","click profile");
                        Intent i2 = new Intent(Cart.this,UsersActivity.class);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_dash_board);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                Cart.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_dash_board);
        Log.d("testSideNav","---------------");
        navigationView.setNavigationItemSelectedListener(Cart.this);
        navigationView.bringToFront();
        //end side bar


    }

    private void showAlertDialog() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Log.d("CartPage", String.valueOf(cart));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("ระบุเพิ่มเติม");
        alertDialog.setMessage("ระบุเพิ่มเติม");

        Log.d("database","showAlertDialog");
        final EditText edtAddress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);





        alertDialog.setIcon(R.drawable.ic_shopping_cart);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                FirebaseUser user = mAuth.getCurrentUser();
                Request request = new Request(
                        "wait",
                        user.getUid(),
                        phoneNumber,
                        name,
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart,
                        roomNumber,
                        nameuser
                );
                Request requestUser = new Request(
                        "wait",
                        user.getUid(),
                        phoneNumber,
                        name,
                        edtAddress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart,
                        roomNumber,
                        nameuser
                );
                // user = mAuth.getCurrentUser();
                Log.d("database","livenow"+livenow);
                Log.d("database","turnq"+turnq);
        // mDatabase.child("room").child(livenow).child("q").child(Long.toString(turnq)).child("uid").setValue(user.getUid());
                mDatabase.child("room").child(livenow).child("q").child("queue").child(Long.toString(turnq)).setValue(request);
                mDatabase.child("user").child(user.getUid()).child("orderNow").child(String.valueOf(noOfOder)).setValue(livenow);
                new Database(getBaseContext()).cleanCart();
                cart.clear();
                Toast.makeText(Cart.this,"Thank you , order has been placed", Toast.LENGTH_SHORT).show();
                finish();


            }

        });

        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("database","can click no");
                dialog.dismiss();


            }
        });
     alertDialog.show();


    }

    private void showAlertDialog2(){
        AlertDialog.Builder builder =
                new AlertDialog.Builder(Cart.this);
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

    private void loadListItem(){
        Log.d("database","access loadlistitem");
    cart = new Database(this).getCarts();
    adapter = new CartAdapter(cart,this);
    recyclerView.setAdapter(adapter);

    int total =0 ;
    for (Order order:cart){
        try{

        total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuanlity()));
        livenow=order.getLivenow();
        }
        catch (Exception e){
        total = 0;
        }

    }


        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

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
