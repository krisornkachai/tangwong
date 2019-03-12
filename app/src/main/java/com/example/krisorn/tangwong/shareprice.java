package com.example.krisorn.tangwong;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class shareprice extends AppCompatActivity {

    
    private DatabaseReference mDatabase;
    private String id ;
    private TimePicker time;
    public DatabaseReference nameCard;
    private String hr,mi;
    private FirebaseAuth mAuth;
    private String uid;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shareprice);
        nameCard = database.getReference();
        int A;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("eiei").setValue("asdasd");
        mDatabase.child("eiei").setValue("dsacfvf");
        nameCard.addListenerForSingleValueEvent (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                uid = user.getUid();
                int first,sec;
                id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
                if(dataSnapshot.child("room").child(id).child("money").child("list").hasChild("+"))
                {
                     first = (int) dataSnapshot.child("room").child(id).child("money").child("list").child("+").getChildrenCount();
                }
                else
                {
                    first = 0;
                }
                if(dataSnapshot.child("room").child(id).child("money").child("list").hasChild("-"))
                {
                    sec = (int) dataSnapshot.child("room").child(id).child("money").child("list").child("-").getChildrenCount();
                }
                else
                {
                    sec = 0;
                }


                ArrayList<String> Arr = new ArrayList<String>();
               // Arr.add(mystring);
                id = dataSnapshot.child ("user").child (uid).child ("livenow").getValue (String.class);
               if(dataSnapshot.child("room").child(id).hasChild("money"))
                {
                    Arr.add("รายรับ");

                   int count = (int) dataSnapshot.child("room").child(id).child("money").child("list").child("+").getChildrenCount();
                    for(int i =1 ;i<=count;i++)
                    {
                        String b;
                        b = dataSnapshot.child("room").child(id).child("money").child("list").child("+").child(String.valueOf(i)).getValue(String.class);
                        Arr.add(b);
                    }
                    Arr.add("   ");
                    Arr.add("รายจ่าย");
                    int j=1;
                    int count2 = (int) dataSnapshot.child("room").child(id).child("money").child("list").child("-").getChildrenCount();
                   for(int i =(count+2) ;i<(count+2+count2);i++)
                    {
                        String b;
                        b = dataSnapshot.child("room").child(id).child("money").child("list").child("-").child(String.valueOf(j)).getValue(String.class);
                        Arr.add(b);
                        j++;
                    }


                     String c = dataSnapshot.child("room").child(id).child("money").child("total").getValue(String.class);
                    Arr.add("   ");
                   Arr.add("****");
                    Arr.add("****");
                    Arr.add("total : " + c);
                    int num = 1;
                    if(dataSnapshot.child("room").child(id).hasChild("people_live"))
                    {
                        num += (int) dataSnapshot.child("room").child(id).child("people_live").getChildrenCount();
                    }


                    int y = Integer.parseInt(c)/num;


                    Arr.add("Average : " + String.valueOf(y) );

                }

                //Log.d("asd","sd"+Arr[0]);
                ArrayAdapter adapter = new ArrayAdapter<String>(shareprice.this,
                        R.layout.listview, Arr);

                ListView listView = (ListView) findViewById(R.id._list);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
