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
import com.example.krisorn.tangwong.StatusAlert2;
import com.example.krisorn.tangwong.StatusCostomer;
import com.example.krisorn.tangwong.UsersViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.facebook.FacebookSdk.getApplicationContext;

class StatusAdapter2ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtNameRoom, txtSumPrice,txtStatus,txtNumberOfItem;
    public ImageView cardimg;

    public ImageView getCardimg() {
        return cardimg;
    }

    public void setCardimg(ImageView cardimg) {
        this.cardimg = cardimg;
    }

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

    public StatusAdapter2ViewHolder(@NonNull View itemView) {
        super(itemView);


        txtNameRoom= (TextView)itemView.findViewById(R.id.name_room);
        txtNumberOfItem=(TextView)itemView.findViewById(R.id.NumberOfItem);
        txtStatus= (TextView)itemView.findViewById(R.id.status_q);
        txtSumPrice= (TextView)itemView.findViewById(R.id.sumPice);
        cardimg=(ImageView)itemView.findViewById(R.id.img_card_user_room);
    }

    @Override
    public void onClick(View v) {
        // Log.d("statusPage","can click");
    }
}

public class StatusAdapter2Adapter extends RecyclerView.Adapter<StatusAdapter2ViewHolder> {

    private Context context;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public int countOrderNow;
    int coutForNoneUse = 0;

    private static  String[] MENU =
            {"ถึงคิวแล้ว","กำลังทำ","เอาออกจากคิว","ดูรายการ"};

    String mSelected;


    public StatusAdapter2Adapter(int countOrderNow,Context context){
        this.countOrderNow=countOrderNow;
        this.context = context;
    }
    @NonNull
    @Override
    public StatusAdapter2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.status_layout,parent,false);


        return new StatusAdapter2ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final StatusAdapter2ViewHolder holder, final int position) {

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
                builder.setTitle("ต้องการลบแจ้งเตือน");
              //  mSelected="ถึงคิวแล้ว";

                try {
                    builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            // ส่วนนี้สำหรับเซฟค่าลง database หรือ SharedPreferences.
                     /*   Toast.makeText(getApplicationContext(), "คุณชอบ " +
                                mSelected, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();*/
                            mDatabase.child("user").child(user.getUid()).child("keep_noti").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //  String room = dataSnapshot.child(String.valueOf(position)).getValue(String.class);
                                    //  Log.d("listdatanoti", "can get fribase1" + room + " " + position);
                                    int i = position+1;



                                    while(dataSnapshot.child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class).equals("ลบแล้ว")) {
                                        //  Log.d("listStatusaaaaaaa", dataSnapshot.child("q").child("queue").child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class));
                                        coutForNoneUse++;
                                    }
                                    i=i+coutForNoneUse;
                                    String getuid = dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class);
                                    Log.d("listStatus", "getuid= " + getuid);
                                    String usergetUid = user.getUid();
                                    //  Log.d("listEqal", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("total").getValue(String.class));

                                    mDatabase.child("user").child(user.getUid()).child("keep_noti").child(String.valueOf(i)).child("status").setValue("ลบแล้ว");


//                Log.d("listStatus", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("status").getValue(String.class));


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });






                            Intent intentt = new Intent(context, StatusAlert2.class);
                            context.startActivity(intentt);
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


        mDatabase.child("user").child(user.getUid()).child("keep_noti").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //  String room = dataSnapshot.child(String.valueOf(position)).getValue(String.class);
                //  Log.d("listdatanoti", "can get fribase1" + room + " " + position);
                int i = position+1;



                while(dataSnapshot.child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class).equals("ลบแล้ว")) {
                  //  Log.d("listStatusaaaaaaa", dataSnapshot.child("q").child("queue").child(String.valueOf(i+coutForNoneUse)).child("status").getValue(String.class));
                    coutForNoneUse++;
                }
                i=i+coutForNoneUse;
                String getuid = dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("uid").getValue(String.class);
                Log.d("listStatus", "getuid= " + getuid);
                String usergetUid = user.getUid();
                //  Log.d("listEqal", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("total").getValue(String.class));
               // Picasso.get().load(dataSnapshot.child(String.valueOf(i)).child("room").getValue(String.class)).into(holder.cardimg);
                mDatabase.child("room").child(dataSnapshot.child(String.valueOf(i)).child("roomid").getValue(String.class)).child("photoPath").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Picasso.get().load(dataSnapshot.getValue(String.class)).into(holder.cardimg);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                holder.txtNameRoom.setText(dataSnapshot.child(String.valueOf(i)).child("room").getValue(String.class));
                holder.txtNumberOfItem.setText(dataSnapshot.child(String.valueOf(i)).child("time").getValue(String.class));
                holder.txtStatus.setText(dataSnapshot.child(String.valueOf(i)).child("text").getValue(String.class));
                holder.txtSumPrice.setText("");

//                Log.d("listStatus", dataSnapshot.child("q").child("queue").child(String.valueOf(i)).child("status").getValue(String.class));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
