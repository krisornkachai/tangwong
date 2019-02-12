package com.example.krisorn.tangwong.status;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.krisorn.tangwong.Model.Order;
import com.example.krisorn.tangwong.R;
import com.example.krisorn.tangwong.Status;
import com.example.krisorn.tangwong.UsersViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtNameRoom, txtSumPrice,txtStatus,txtNumberOfItem;

    public TextView getTxtNameRoom() {
        return txtNameRoom;
    }

    public void setTxtNameRoom(TextView txtNameRoom) {
        this.txtNameRoom = txtNameRoom;
    }

    public TextView getTxtSumPrice() {
        return txtSumPrice;
    }

    public void setTxtSumPrice(TextView txtSumPrice) {
        this.txtSumPrice = txtSumPrice;
    }

    public TextView getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatus(TextView txtStatus) {
        this.txtStatus = txtStatus;
    }

    public TextView getTxtNumberOfItem() {
        return txtNumberOfItem;
    }

    public void setTxtNumberOfItem(TextView txtNumberOfItem) {
        this.txtNumberOfItem = txtNumberOfItem;
    }

    public StatusViewHolder(@NonNull View itemView) {
        super(itemView);


        txtNameRoom= (TextView)itemView.findViewById(R.id.name_room);
        txtNumberOfItem=(TextView)itemView.findViewById(R.id.NumberOfItem);
        txtStatus= (TextView)itemView.findViewById(R.id.status_q);
        txtSumPrice= (TextView)itemView.findViewById(R.id.sumPice);
    }

    @Override
    public void onClick(View v) {
       // Log.d("statusPage","can click");
    }
}

public class StatusAdapter extends RecyclerView.Adapter<StatusViewHolder> {

    private Context context;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public int countOrderNow;
    private static final String[] MENU =
            {"ถึงคิวแล้ว", "กำลังทำ", "ดูรายการ"};

    String mSelected;

    public StatusAdapter(int countOrderNow, Context context) {
        this.countOrderNow = countOrderNow;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.status_layout, parent, false);


        return new StatusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StatusViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        //  TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getQuanlity(),Color.RED);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusPage", "can click");
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(context);
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
        });*/
        Log.d("list data", "can not get firebase");
        try {
            mDatabase.child("user").child(user.getUid()).child("orderNow").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String room = dataSnapshot.child(String.valueOf(position)).getValue(String.class);
                    Log.d("list data", "can get fribase1" + room + " " + position);
                    try {


                        mDatabase.child("room").child(room).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int i = 0;
                                Log.d("list data", "can get fribase2");
                                while (i < dataSnapshot.child("q").child("queue").getChildrenCount()) {
                                    Log.d("list data", String.valueOf(i));

                                    String getuid = dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class);
                                    Log.d("listStatus", "getuid= " + getuid);
                                    String usergetUid = user.getUid();

                                    if (getuid != null && getuid.equals(usergetUid)) {
                                        Log.d("listEqal", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("total").getValue(String.class));
                                        holder.txtNameRoom.setText(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("name").getValue(String.class));
                                        holder.txtNumberOfItem.setText(String.valueOf(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("items").getChildrenCount()));
                                        holder.txtStatus.setText(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("status").getValue(String.class));
                                        holder.txtSumPrice.setText(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("total").getValue(String.class));

                                        Log.d("listStatus", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("status").getValue(String.class));
                                    }
                                    i++;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } catch (Exception e) {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {


        }


        // holder.txtNameRoom.setText();

       /* Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());*/

    }

    @Override
    public int getItemCount() {
        Log.d("listtttttttttttttttt", String.valueOf(countOrderNow));
        return countOrderNow;
    }
}
