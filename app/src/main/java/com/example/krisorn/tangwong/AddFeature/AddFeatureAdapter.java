package com.example.krisorn.tangwong.AddFeature;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krisorn.tangwong.AdminDashBoradView;
import com.example.krisorn.tangwong.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

class AddFeatureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtDetail;
    public TextView txtNameRoom;
    public ImageView imageView;


    public TextView getTxtNameRoom() {
        return txtNameRoom;
    }

    public void setTxtNameRoom(TextView txtNameRoom) {
        this.txtNameRoom = txtNameRoom;
    }




    public AddFeatureViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.img_card_user_room);
        txtNameRoom= (TextView)itemView.findViewById(R.id.text_card_user_room);
        // txtNumberOfItem=(TextView)itemView.findViewById(R.id.NumberOfItem);
        txtDetail= (TextView)itemView.findViewById(R.id.text_card_detail);
        //  txtSumPrice= (TextView)itemView.findViewById(R.id.sumPice);
    }

    @Override
    public void onClick(View v) {
        // Log.d("statusPage","can click");
    }
}

public class AddFeatureAdapter extends RecyclerView.Adapter<AddFeatureViewHolder> {

    private Context context;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public int countOrderNow;
    public int countFeature =0 ;


    public AddFeatureAdapter(int countOrderNow,Context context){
        this.countOrderNow=countOrderNow;
        this.context = context;
    }
    @NonNull
    @Override
    public AddFeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.card_user_room,parent,false);


        return new AddFeatureViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddFeatureViewHolder holder, final int position) {
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==0){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("q");
                                    mDatabase.child("room").child(roomLiveNow).child("q").child("nameOfFeture").setValue("รายการ");
                                    mDatabase.child("room").child(roomLiveNow).child("q").child("detailOfFeture").setValue(dataSnapshot.child("data").getValue(String.class));
                                    mDatabase.child("room").child(roomLiveNow).child("q").child("typeOfFeture").setValue("StallShop");
                                    mDatabase.child("room").child(roomLiveNow).child("q").child("typeOfFetureShow").setValue("both");
                                    mDatabase.child ("room").child (roomLiveNow).child ("q").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_makegroup.png?alt=media&token=28954f50-beb4-44ca-b26d-4f8da2420ba6");

                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount()+1)))
                                            .setValue("viewqueue");
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("nameOfFeture").setValue("คิว");
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("detailOfFeture").setValue(dataSnapshot.child("data")
                                            .getValue(String.class));
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("typeOfFeture").setValue("viewqueue");
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("typeOfFetureShow").setValue("admin");
                                    mDatabase.child ("room").child (roomLiveNow).child ("viewqueue").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_q.png?alt=media&token=cf77e22b-9ad7-4528-b7fe-5f26d6962560");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                /*else   if (position==1){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("store");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("nameOfFeture").setValue("รายการสินค้า(แบบderivery)");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("detailOfFeture").setValue(dataSnapshot.child("data")

                                            .getValue(String.class));
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("typeOfFeture").setValue("store");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }*/
                /*else   if (position==2){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("viewqueue");
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("nameOfFeture").setValue("ดูคิวลูกค้า");
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("detailOfFeture").setValue(dataSnapshot.child("data")
                                            .getValue(String.class));
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("typeOfFeture").setValue("viewqueue");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }*/
                /*else   if (position==1){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("store");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("nameOfFeture").setValue("รายการสินค้า(แบบderivery)");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("detailOfFeture").setValue(dataSnapshot.child("data")

                                            .getValue(String.class));
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("typeOfFeture").setValue("store");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                */
                /*else   if (position==2){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("viewqueue");
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("nameOfFeture").setValue("ดูคิวลูกค้า");
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("detailOfFeture").setValue(dataSnapshot.child("data")
                                            .getValue(String.class));
                                    mDatabase.child("room").child(roomLiveNow).child("viewqueue").child("typeOfFeture").setValue("viewqueue");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                */
                /*else   if (position==3){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("store");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("nameOfFeture").setValue("รายการสินค้า(แบบderivery)");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("detailOfFeture").setValue(dataSnapshot.child("data")
                                            .getValue(String.class));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                */
                else   if (position==1){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("notification");
                                    mDatabase.child("room").child(roomLiveNow).child("notification").child("nameOfFeture").setValue("เตือน");
                                    mDatabase.child("room").child(roomLiveNow).child("notification").child("detailOfFeture").setValue("เตือนในกลุ่ม");
                                    mDatabase.child("room").child(roomLiveNow).child("notification").child("typeOfFeture").setValue("notification");
                                    mDatabase.child("room").child(roomLiveNow).child("notification").child("typeOfFetureShow").setValue("admin");
                                    mDatabase.child ("room").child (roomLiveNow).child ("notification").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_alert.png?alt=media&token=1df69419-0753-463d-bbee-52d4f24e7b48");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==2){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("canlender_settime");
                                    mDatabase.child("room").child(roomLiveNow).child("canlender_settime").child("nameOfFeture").setValue("ตั้งเวลา");
                                    mDatabase.child("room").child(roomLiveNow).child("canlender_settime").child("detailOfFeture").setValue("เเจ้งเตือนเวลา");
                                    mDatabase.child("room").child(roomLiveNow).child("canlender_settime").child("typeOfFeture").setValue("canlender_settime");
                                    mDatabase.child("room").child(roomLiveNow).child("canlender_settime").child("typeOfFetureShow").setValue("admin");
                                    mDatabase.child ("room").child (roomLiveNow).child ("canlender_settime").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_alert2.png?alt=media&token=88f4cd5d-9559-417d-be6c-d57d9580ce76");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==3){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("calender");
                                    mDatabase.child("room").child(roomLiveNow).child("calender").child("nameOfFeture").setValue("ดูปฎิทิน");
                                    mDatabase.child("room").child(roomLiveNow).child("calender").child("detailOfFeture").setValue("ดูปฎิทิน");
                                    mDatabase.child("room").child(roomLiveNow).child("calender").child("typeOfFeture").setValue("calender");
                                    mDatabase.child("room").child(roomLiveNow).child("calender").child("typeOfFetureShow").setValue("both");
                                    mDatabase.child ("room").child (roomLiveNow).child ("calender").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_calender.png?alt=media&token=8d8652df-2123-470e-8f26-12928dbb704f");

                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount()+1)))
                                            .setValue("calender_event");
                                    mDatabase.child("room").child(roomLiveNow).child("calender_event").child("nameOfFeture").setValue("สร้างกิจกรรม");
                                    mDatabase.child("room").child(roomLiveNow).child("calender_event").child("detailOfFeture").setValue("สร้างกิจกรรม");
                                    mDatabase.child("room").child(roomLiveNow).child("calender_event").child("typeOfFeture").setValue("calender_event");
                                    mDatabase.child("room").child(roomLiveNow).child("calender_event").child("typeOfFetureShow").setValue("admin");
                                    mDatabase.child ("room").child (roomLiveNow).child ("calender_event").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_poll.png?alt=media&token=154f5b7d-a6aa-4b59-946d-863e8128829f");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

/*
             else   if (position==1){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("store");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("nameOfFeture").setValue("รายการสินค้า(แบบderivery)");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("detailOfFeture").setValue(dataSnapshot.child("data")
                                            .getValue(String.class));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==2){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("store");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("nameOfFeture").setValue("รายการสินค้า(แบบderivery)");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("detailOfFeture").setValue(dataSnapshot.child("data")
                                            .getValue(String.class));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==3){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("store");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("nameOfFeture").setValue("รายการสินค้า(แบบderivery)");
                                    mDatabase.child("room").child(roomLiveNow).child("store").child("detailOfFeture").setValue(dataSnapshot.child("data")
                                            .getValue(String.class));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==4){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("notification");
                                    mDatabase.child("room").child(roomLiveNow).child("notification").child("nameOfFeture").setValue("แจ้งเตือน");
                                    mDatabase.child("room").child(roomLiveNow).child("notification").child("detailOfFeture").setValue("แจ้งเตือนคนในกลุ่ม");
                                    mDatabase.child("room").child(roomLiveNow).child("notification").child("typeOfFeture").setValue("notification");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==5){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("canlender_settime");
                                    mDatabase.child("room").child(roomLiveNow).child("canlender_settime").child("nameOfFeture").setValue("แจ้งเตือนตั้งเวลา");
                                    mDatabase.child("room").child(roomLiveNow).child("canlender_settime").child("detailOfFeture").setValue("แจ้งเตือนตั้งเวลาคนในกลุ่ม");
                                    mDatabase.child("room").child(roomLiveNow).child("canlender_settime").child("typeOfFeture").setValue("canlender_settime");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==6){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("calender");
                                    mDatabase.child("room").child(roomLiveNow).child("calender").child("nameOfFeture").setValue("ดูปฎิทิน");
                                    mDatabase.child("room").child(roomLiveNow).child("calender").child("detailOfFeture").setValue("ดูปฎิทิน");
                                    mDatabase.child("room").child(roomLiveNow).child("calender").child("typeOfFeture").setValue("calender");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                */else   if (position==4){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            Log.d("canRetrive","livenow");

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("poll");
                                    mDatabase.child("room").child(roomLiveNow).child("poll").child("nameOfFeture").setValue("โพล");
                                    mDatabase.child("room").child(roomLiveNow).child("poll").child("detailOfFeture").setValue("โพล");
                                    mDatabase.child("room").child(roomLiveNow).child("poll").child("typeOfFeture").setValue("poll");
                                    mDatabase.child("room").child(roomLiveNow).child("poll").child("typeOfFetureShow").setValue("admin");
                                    mDatabase.child ("room").child (roomLiveNow).child ("poll").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_pool.png?alt=media&token=480efec9-9182-433e-aae1-c97f147d8681");

                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount()+1)))
                                            .setValue("pollview");
                                    mDatabase.child("room").child(roomLiveNow).child("pollview").child("nameOfFeture").setValue("ดูโพล");
                                    mDatabase.child("room").child(roomLiveNow).child("pollview").child("detailOfFeture").setValue("ดูโพล");
                                    mDatabase.child("room").child(roomLiveNow).child("pollview").child("typeOfFeture").setValue("pollview");
                                    mDatabase.child("room").child(roomLiveNow).child("pollview").child("typeOfFetureShow").setValue("user");
                                    mDatabase.child ("room").child (roomLiveNow).child ("pollview").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_polling.png?alt=media&token=9f63ca6d-2953-4235-bb7f-1a5bcc31b596");


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==5){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("map");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("nameOfFeture").setValue("แผนที่");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("detailOfFeture").setValue("ระบุตำเเหน่งของคุณ");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("typeOfFeture").setValue("map");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("typeOfFetureShow").setValue("both");
                                    mDatabase.child ("room").child (roomLiveNow).child ("map").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_pool.png?alt=media&token=480efec9-9182-433e-aae1-c97f147d8681");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else   if (position==5){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);

                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("share_money");
                                    mDatabase.child("room").child(roomLiveNow).child("share_money").child("nameOfFeture").setValue("แบ่งกันจ่าย");
                                    mDatabase.child("room").child(roomLiveNow).child("share_money").child("detailOfFeture").setValue("แบ่งรายจ่ายกับกลุ่ม");
                                    mDatabase.child("room").child(roomLiveNow).child("share_money").child("typeOfFeture").setValue("share_money");
                                    mDatabase.child("room").child(roomLiveNow).child("share_money").child("typeOfFetureShow").setValue("admin");
                                    mDatabase.child ("room").child (roomLiveNow).child ("share_money").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_pool.png?alt=media&token=480efec9-9182-433e-aae1-c97f147d8681");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else   if (position==6){

                    mDatabase.child("user").child(user.getUid()).child("livenow").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String roomLiveNow= dataSnapshot.getValue(String.class);
                            mDatabase.child("room").child(roomLiveNow).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    mDatabase.child("room").child(roomLiveNow).child("feature").child(String.valueOf((dataSnapshot.child("feature").getChildrenCount())))
                                            .setValue("map");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("nameOfFeture").setValue("แผนที่");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("detailOfFeture").setValue("ระบุตำเเหน่งของคุณ");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("typeOfFeture").setValue("map");
                                    mDatabase.child("room").child(roomLiveNow).child("map").child("typeOfFetureShow").setValue("both");
                                    mDatabase.child ("room").child (roomLiveNow).child ("map").child ("typepicture").setValue ("https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_pool.png?alt=media&token=480efec9-9182-433e-aae1-c97f147d8681");

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                Intent i = new Intent(v.getContext(), AdminDashBoradView.class);
                context.startActivity(i);
                Log.d("statusPage","can click addfeature");

            }});
        if(position==0){

            holder.txtNameRoom.setText("เพิ่มรายการ");
            holder.txtDetail.setText("เพิ่มข้อมูลรายละเอียดต่างๆ");
            String imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_makegroup.png?alt=media&token=28954f50-beb4-44ca-b26d-4f8da2420ba6";
            Picasso.get().load(imgUrl).into(holder.imageView);

        }
       /* if (position==1){
            holder.txtNameRoom.setText("เพิ่มร้านค้า");
            holder.txtDetail.setText("เพิ่มร้านค้า แบบdelivery");
            String imgUrl = "http://fr-asia.com/wp-content/uploads/tmp/delivery2-400x400.jpg";
            Picasso.get().load(imgUrl).into(holder.imageView);
        }*/
      /*  if (position==2){
            holder.txtNameRoom.setText("เพิ่มดูสถาณะร้านค้า");
            holder.txtDetail.setText("เอาไว้ดูลำดับคิวลูกค้า");
            String imgUrl = "http://เซ้งร้าน.com/img/dorm/img_1437667740.jpeg";
            Picasso.get().load(imgUrl).into(holder.imageView);
        }*/
      /*  if (position==1){
            holder.txtNameRoom.setText("เพิ่มตัวจัดการคิว");
            holder.txtDetail.setText("ทุกคนสามารถเข้าคิวผ่านแอพได้");
            String imgUrl = "https://i.ytimg.com/vi/FOrGijZK5m0/maxresdefault.jpg";
            Picasso.get().load(imgUrl).into(holder.imageView);
        }*/
        if (position==1){
            holder.txtNameRoom.setText("เตือน");
            holder.txtDetail.setText("แจ้งเตือนทุกคนในกลุ่ม");
            String imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_alert.png?alt=media&token=1df69419-0753-463d-bbee-52d4f24e7b48";
            Picasso.get().load(imgUrl).into(holder.imageView);
        }
        if (position==2){
            holder.txtNameRoom.setText("ตั้งเวลา");
            holder.txtDetail.setText("แจ้งเวลาทุกคนในกลุ่ม");
            String imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_alert2.png?alt=media&token=88f4cd5d-9559-417d-be6c-d57d9580ce76";
            Picasso.get().load(imgUrl).into(holder.imageView);
        }
        if (position==3){
            holder.txtNameRoom.setText("ปฎิทิน");
            holder.txtDetail.setText("ดูปฎิทิน");
            String imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_calender.png?alt=media&token=8d8652df-2123-470e-8f26-12928dbb704f";
            Picasso.get().load(imgUrl).into(holder.imageView);
        }
        if (position==4){
            holder.txtNameRoom.setText("โพล");
            holder.txtDetail.setText("โพล");
            String imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_pool.png?alt=media&token=480efec9-9182-433e-aae1-c97f147d8681";
            Picasso.get().load(imgUrl).into(holder.imageView);
        }if (position==6){
            holder.txtNameRoom.setText("เเชร์เเผนที่");
            holder.txtDetail.setText("ระบุตำเเหน่งที่อยู่ของคุณ");
            String imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_q.png?alt=media&token=cf77e22b-9ad7-4528-b7fe-5f26d6962560";
        }
        if(position==5){

            holder.txtNameRoom.setText("แบ่งกันจ่าย");
            holder.txtDetail.setText("รายละเอียด");
            String imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2Fic_makegroup.png?alt=media&token=28954f50-beb4-44ca-b26d-4f8da2420ba6";
            Picasso.get().load(imgUrl).into(holder.imageView);

        }

    }

    @Override
    public int getItemCount() {

        return 7;
    }
}
