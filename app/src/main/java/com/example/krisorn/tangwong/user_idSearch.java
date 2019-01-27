package com.example.krisorn.tangwong;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class user_idSearch extends AppCompatActivity {
    private EditText text_search ;
    private DatabaseReference searchRoom;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_idsearch);
        searchRoom = FirebaseDatabase.getInstance().getReference();

    }

    public void click(final View v){
        searchRoom = FirebaseDatabase.getInstance().getReference();
        text_search = findViewById(R.id.text_search);

        if(v.getId() == R.id.bt_searchId){
            searchRoom.child("room").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                    Log.d("haaaaaaaaaa","haa");
                    if (dataSnapshot.hasChild(text_search.getText().toString())){
                        Log.d("passs",dataSnapshot.getKey());
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                        builder1.setMessage("You want Tungwong this");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        mAuth=FirebaseAuth.getInstance();
                                        final FirebaseUser user = mAuth.getCurrentUser();
                                        long numberOfpeople_live = dataSnapshot.child("room").child(text_search.getText().toString()).child("people_live").getChildrenCount();

                                        searchRoom.child("user").child(user.getUid()).child("live").child(text_search.getText().toString()).setValue("1");
                                        searchRoom.child("user").child(user.getUid()).child("livenow").setValue(text_search.getText().toString());
                                        searchRoom.child("room").child(text_search.getText().toString()).child("people_live").child(Long.toString(numberOfpeople_live += 1)).child("uid").setValue (user.getUid ());

                                        Intent i = new Intent(user_idSearch.this,user_roomActivity.class);
                                        startActivity(i);
                                    }
                                });

                        builder1.setNegativeButton(
                                "No",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }else{
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                        builder1.setMessage("NOT FOUND !!");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this.getApplicationContext());
            builder1.setMessage("Please Input to enjoy Id");
            builder1.setCancelable(true);

            builder1.setPositiveButton(
                    "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();

        }

    }
}
