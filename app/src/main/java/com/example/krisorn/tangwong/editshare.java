package com.example.krisorn.tangwong;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class editshare extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private String id ;
    private TimePicker time;
    public DatabaseReference nameCard;
    private String hr,mi;
    private FirebaseAuth mAuth;
    private String uid;
    private int count,count2,sum;
    private RadioGroup radioGroup;
    private String a;
    private boolean aa =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editshare);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        nameCard = database.getReference();
        nameCard.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                uid = user.getUid();

                id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
                if(!dataSnapshot.child("room").child(id).hasChild("money"))
                {
                    count = 1;
                    count2 = 1;
                    sum = 0;
                }
                else
                {
                    count = (int) dataSnapshot.child("room").child(id).child("money").child("list").child("+").getChildrenCount();
                    count2 = (int) dataSnapshot.child("room").child(id).child("money").child("list").child("-").getChildrenCount();
                    count++;
                    count2++;
                    sum = Integer.parseInt(dataSnapshot.child("room").child(id).child("money").child("total").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @SuppressLint("ResourceType")
    public void click(View view) {

        int A;
        if(view.getId () == R.id.minus)
        {
            aa = false;
        }
        if(view.getId () == R.id.plus)
        {
            aa = true;
        }

        if(view.getId () == R.id.edit){
            mDatabase.child("eiei").setValue("asdasd");
            mDatabase.child("eiei").setValue("dsacfvf");

            EditText list = findViewById (R.id.list1);
            EditText price = findViewById (R.id.price);
            radioGroup = (RadioGroup) findViewById(R.id.choose);

            if(aa==false)
            {
                a = String.valueOf(price.getText().toString());
                sum = sum+(Integer.parseInt(a));
                mDatabase.child("room").child(id).child("money").child("list").child("+").child(String.valueOf(count)).setValue(String.valueOf(list.getText().toString())+" "+String.valueOf(price.getText().toString()));
            }
            else
            {
                 a = String.valueOf(price.getText().toString());
                 int b = Integer.parseInt(a);
                 sum = sum - b;
                mDatabase.child("room").child(id).child("money").child("list").child("-").child(String.valueOf(count2)).setValue(String.valueOf(list.getText().toString())+" "+String.valueOf(price.getText().toString()));
            }


            mDatabase.child("room").child(id).child("money").child("total").setValue(String.valueOf(sum));
            Intent i = new Intent(editshare.this,shareprice.class);
            startActivity(i);

        }


    }
}
