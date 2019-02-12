package com.example.krisorn.tangwong;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.example.krisorn.tangwong.status.StatusAdapter2Adapter;
import com.example.krisorn.tangwong.status.StatusCostomerAdapter;
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

public class StatusAlert2 extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    StatusAdapter2Adapter adapter;
    public int countOrderNow=0;


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
        Log.d("list1", String.valueOf(countOrderNow));
        countOrderNow=0;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        Log.d("listtttttttttttttttt", user.getUid());
        try {
            mDatabase.child("user").child(user.getUid()).child("keep_noti").addListenerForSingleValueEvent(new ValueEventListener() {

                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (int countorder = 1; countorder <= dataSnapshot.getChildrenCount(); countorder++) {

                        Log.d("listStatus_noti", dataSnapshot.child(String.valueOf(countorder)).child("status").getValue(String.class));
                        if (!dataSnapshot.child(String.valueOf(countorder)).child("status").getValue(String.class).equals("ลบแล้ว")) {
                            countOrderNow++;
                        }
                    }
                    Log.d("liststatus_noti", "countnoti " + countOrderNow);
                    adapter = new StatusAdapter2Adapter(countOrderNow, StatusAlert2.this);
                    recyclerView.setAdapter(adapter);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
                        Log.d("liststatusin", String.valueOf(e));
                    }







    }


}
