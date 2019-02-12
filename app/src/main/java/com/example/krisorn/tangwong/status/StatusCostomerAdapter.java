package com.example.krisorn.tangwong.status;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.krisorn.tangwong.StatusCostomer;
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

class StatusCostomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


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

    public StatusCostomerViewHolder(@NonNull View itemView) {
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

public class StatusCostomerAdapter extends RecyclerView.Adapter<StatusViewHolder> {

    private Context context;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public int countOrderNow;
    int coutForNoneUse = 0;

    private static  String[] MENU =
            {"ถึงคิวแล้ว","กำลังทำ","เอาออกจากคิว","ดูรายการ"};

    String mSelected;


    public StatusCostomerAdapter(int countOrderNow,Context context){
        this.countOrderNow=countOrderNow;
        this.context = context;
    }
    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.status_layout,parent,false);


        return new StatusViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StatusViewHolder holder, final int position) {

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        //  TextDrawable drawable = TextDrawable.builder().buildRound(""+listData.get(position).getQuanlity(),Color.RED);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("statusPage", "can click");
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(context);
                builder.setTitle("ตัวจัดการาคิว");
                mSelected="ถึงคิวแล้ว";
                builder.setSingleChoiceItems(MENU, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelected = MENU[which];
                        Log.d("statusNewbug","showMselect top" + mSelected);
                    }
                });
                try {
                    builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                     /*   Toast.makeText(getApplicationContext(), "คุณชอบ " +
                                mSelected, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();*/
                            try {
                                mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String room = dataSnapshot.getValue(String.class);
                                        Log.d("list data", "can get fribase1" + room + " " + position );
                                        try {

                                            mDatabase.child("room").child(room).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    int i = position;
                                                    Log.d("list data", "can get fribase2");
                                                    Log.d("list data", String.valueOf(i));
                                                    while(dataSnapshot.child("q").child("queue").child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class).equals("เอาออกจากคิว")) {
                                                        Log.d("listStatusaabb", dataSnapshot.child("q").child("queue").child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class));
                                                        coutForNoneUse++;
                                                    }
                                                    i=i+coutForNoneUse;

                                                    Log.d("list data", "can get fribase2");
                                                    Log.d("list data", String.valueOf(i));

                                                    String getuid =dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class);
                                                    Log.d("listStatus","getuid= "+getuid);
                                                  Log.d("statusNewbug","show mselected" + mSelected);
                                                    if(mSelected.equals("ถึงคิวแล้ว")){
                                                        mDatabase.child("room").child(room).child("q").child("queue").child(String.valueOf(i)).child("status").setValue("ถึงคิวแล้ว");
                                                        holder.txtStatus.setText("ถึงคิวแล้ว");
                                                        mDatabase.child ("user").child (dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class)).child ("notification").child ("status").setValue ("1");
                                                        mDatabase.child ("user").child (dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class)).child ("notification").child ("text").setValue ("ถึงคิวคุณแล้ว");
                                                        mDatabase.child ("user").child (dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class)).child ("notification").child ("room").setValue (String.valueOf(room));
                                                    
                                                    }
                                                    else  if(mSelected.equals("กำลังทำ")){

                                                        mDatabase.child("room").child(room).child("q").child("queue").child(String.valueOf(i)).child("status").setValue("กำลังทำ");
                                                        holder.txtStatus.setText("กำลังทำ");
                                                        mDatabase.child ("user").child (dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class)).child ("notification").child ("status").setValue ("1");
                                                        mDatabase.child ("user").child (dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class)).child ("notification").child ("text").setValue ("ใกล้ถึงคิวคุณแล้ว");
                                                        mDatabase.child ("user").child (dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class)).child ("notification").child ("room").setValue (String.valueOf(room));

                                                        Intent intentt = new Intent(context, StatusCostomer.class);
                                                        context.startActivity(intentt);
                                                    }else  if(mSelected.equals("เอาออกจากคิว")){

                                                        mDatabase.child("room").child(room).child("q").child("queue").child(String.valueOf(i)).child("status").setValue("เอาออกจากคิว");
                                                        holder.txtStatus.setText("เอาออกจากคิว");

                                                        Log.d("status","uid to notification" + dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class));


                                                        Intent intentt = new Intent(context, StatusCostomer.class);
                                                        context.startActivity(intentt);

                                                    }else  if(mSelected.equals("ดูรายการ")){
                                                       int countItem = (int) dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("items").getChildrenCount();
                                                        ;
                                                        final String[] list = new String[countItem+2];
                                                        for(int coutLoop =0;coutLoop<countItem;coutLoop++){
                                                            list[coutLoop]=dataSnapshot.child("q").child("queue").child(String.valueOf(i))
                                                                    .child("items").child(String.valueOf(coutLoop)).child("productName").getValue(String.class)
                                                                    + " x " +
                                                                    dataSnapshot.child("q").child("queue").child(String.valueOf(i))
                                                                            .child("items").child(String.valueOf(coutLoop)).child("quanlity").getValue(String.class);
                                                        }
                                                        list[countItem]="รายละเอียดเพิ่มเติม: " + dataSnapshot.child("q").child("queue").child(String.valueOf(i))
                                                                .child("moreDetail").getValue(String.class);

                                                        list[countItem+1]="ผู้เข้าคิว: " + dataSnapshot.child("q").child("queue").child(String.valueOf(i))
                                                                .child("nameuser").getValue(String.class);

                                                        AlertDialog.Builder builder =
                                                                new AlertDialog.Builder(context);
                                                        builder.setTitle("รายการ");
                                                        builder.setItems(list, new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                //  String selected2 = list[which];

                                                            }
                                                        });
                                                        builder.setNegativeButton("เสร็จสิน", null);
                                                        builder.create();

// สุดท้ายอย่าลืม show() ด้วย
                                                        builder.show();
                                                        Log.d("liststatus","ดูรายการ");


                                                    }

                                                    Log.d("listStatus", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("status").getValue(String.class));


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }catch (Exception e){}
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }catch (Exception e ){


                            }

                        }
                    });

                    builder.setNegativeButton("ยกเลิก", null);
                    builder.create();

// สุดท้ายอย่าลืม show() ด้วย
                    builder.show();
                }catch (Exception e){}


            }
        });
        Log.d("list data","can not get firebase");


        try {
            mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String room = dataSnapshot.getValue(String.class);
                    Log.d("list data", "can get fribase1" + room + " " + position );
                    try {


                        mDatabase.child("room").child(room).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int i = position;
                                Log.d("list data", "can get fribase2");
                                    Log.d("list data", String.valueOf(i));

                                    while(dataSnapshot.child("q").child("queue").child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class).equals("เอาออกจากคิว")) {

                                        Log.d("listStatusaaaaaaa", dataSnapshot.child("q").child("queue").child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class));
                                        coutForNoneUse++;
                                    }
                                    i=i+coutForNoneUse;
                                        String getuid = dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class);
                                        Log.d("listStatus", "getuid= " + getuid);
                                        String usergetUid = user.getUid();
                                        //  Log.d("listEqal", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("total").getValue(String.class));
                                        holder.txtNameRoom.setText(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("name").getValue(String.class));
                                        holder.txtNumberOfItem.setText(String.valueOf(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("items").getChildrenCount()));
                                        holder.txtStatus.setText(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("status").getValue(String.class));
                                        holder.txtSumPrice.setText(dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("total").getValue(String.class));

                                        Log.d("listStatus", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("status").getValue(String.class));

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }catch (Exception e){}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e ){


        }


        // holder.txtNameRoom.setText();

       /* Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);


        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductName());*/

    }

    @Override
    public int getItemCount() {
//        countOrderNow=0;
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mAuth = FirebaseAuth.getInstance();
//        final FirebaseUser user = mAuth.getCurrentUser();
//        Log.d("listtttttttttttttttt", user.getUid());
//
//
//            try {
//
//                mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        String room = dataSnapshot.getValue(String.class);
//                        Log.d("liststatus","now is room "+room);
//                        try {
//                            mDatabase.child("room").child(room).addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                    for(int countorder =0 ;countorder<dataSnapshot.child("q").child("queue").getChildrenCount();countorder++) {
//                                        String getuid = dataSnapshot.child("q").child("queue").child(String.valueOf(countorder)).child("uid").getValue(String.class);
//                                        Log.d("listStatus", dataSnapshot.child("q").child("queue").child(String.valueOf(countorder)).child("status").getValue(String.class));
//                                        if(!dataSnapshot.child("q").child("queue").child(String.valueOf(countorder)).child("status").getValue(String.class).equals("ถึงคิวแล้ว")){
//                                            countOrderNow++;
//                                        }
//                                    }
//
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//                        }catch (Exception e){
//                            Log.d("liststatusin", String.valueOf(e));
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//            }catch (Exception e ){
//                Log.d("liststatusout", String.valueOf(e));
//
//            }
Log.d("liststatus","cout q not finish"+countOrderNow);
        return countOrderNow;
    }
}
