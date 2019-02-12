package com.example.krisorn.tangwong;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class user_Question extends AppCompatActivity {
     private String Question;
     LinearLayout  mLinearLayout ;
     FormBuilder formBuilder;
    private DatabaseReference Polldatabase;
    private FirebaseAuth mAuth;
    private String getKey ;
    private EditText topic ;
    private int getCount ;
    List<FormObject> formObjects = new ArrayList<FormObject>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createqeustion);
        topic = (EditText) findViewById(R.id.edittext);
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

    }

     public void click( View v){
        if(v.getId() == R.id.save_question) {
            mAuth=FirebaseAuth.getInstance();
            final FirebaseUser user = mAuth.getCurrentUser();

            Polldatabase = FirebaseDatabase.getInstance().getReference();
            Polldatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getKey =  dataSnapshot.child("user").child(user.getUid()).child("livenow").getValue(String.class);
                    long getCount =  dataSnapshot.child("room").child(getKey).child("Poll").getChildrenCount();
                    getCount+=1;
                    Polldatabase.child("room").child(getKey).child("Poll").child(Long.toString(getCount)).child("Topic").setValue(topic.getText().toString());
                    Polldatabase.child("room").child(getKey).child("showPoll").setValue("1");
                    Intent i = new Intent(user_Question.this,user_poll.class);
                    startActivity(i);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

     }


}
