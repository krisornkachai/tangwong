package com.example.krisorn.tangwong.AddminListItem;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.krisorn.tangwong.AdminDashBoradView;
import com.example.krisorn.tangwong.Model.Order;
import com.example.krisorn.tangwong.R;
import com.example.krisorn.tangwong.UsersViewModel;
import com.example.krisorn.tangwong.item_detail;
import com.example.krisorn.tangwong.list_itemActivity;
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

class AddminListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView txtDetail;
    public TextView txtNameRoom;
    public ImageView imageView;


    public TextView getTxtNameRoom() {
        return txtNameRoom;
    }

    public void setTxtNameRoom(TextView txtNameRoom) {
        this.txtNameRoom = txtNameRoom;
    }




    public AddminListItemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView=(ImageView) itemView.findViewById(R.id.img_card_user_room);
        txtNameRoom= (TextView)itemView.findViewById(R.id.text_card_user_room);
        // txtNumberOfItem=(TextView)itemView.findViewById(R.id.NumberOfItem);
        txtDetail= (TextView)itemView.findViewById(R.id.text_card_detail);
        //  txtSumPrice= (TextView)itemView.findViewById(R.id.sumPice);
    }

    @Override
    public void onClick(View v) {
        Log.d("statusPage","can click admin list item");
    }
}

public class AddminListItemAdapter extends RecyclerView.Adapter<AddminListItemViewHolder> {

    private Context context;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    public int countOrderNow;

    String title=null;
    String livenow=null;
    String detail=null;
    String imgUrl=null;


    public AddminListItemAdapter(int countOrderNow,Context context){
        this.countOrderNow=countOrderNow;
        this.context = context;
    }
    @NonNull
    @Override
    public AddminListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.card_user_room,parent,false);


        return new AddminListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AddminListItemViewHolder holder, final int position) {
        Log.d("canBlindView","addMinLitstitem");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference();
        final String uid = user.getUid();

        mDatabase = database.getReference();


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final int i= position;
                Log.d("onClick","onClick"+i);

                mDatabase.child("user").child(uid).child("live").child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mDatabase.child("user").child(uid).child("liveItemNow").setValue(String.valueOf(i));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent i2 = new Intent(context,item_detail.class);
                context.startActivity(i2);
              /*  Intent i = new Intent(v.getContext(),list_itemActivity.class);
                context.startActivity(i);*/

                Log.d("statusPage","can click addminlist item");

                mDatabase.child("user").child(user.getUid()).child("owner").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String room = dataSnapshot.child(String.valueOf(position)).getValue(String.class);
                        mDatabase.child("user").child(user.getUid()).child("livefeaturenow").setValue("1111111111");
                        //  Log.d("ttttttttttttt",mDatabase.getPath().getParent().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        //  mDatabase.child("user").child(uid).orderByChild("live").equalTo(i).limitToFirst(1).addValueEventListener(new ValueEventListener() {
        mDatabase.child("user").child(uid).child("livenow").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //eventsRef.orderByChild("Name").equalTo("Fb Meetup").limitToFirst(1)
                livenow =dataSnapshot.getValue(String.class);
                Log.d("live","live "+ title + " " + position);
                mDatabase.child("room").child(livenow).child("menu").child(String.valueOf(position)).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        title = dataSnapshot.child("name").getValue(String.class);
                        detail=dataSnapshot.child("detail").getValue(String.class);
                        imgUrl=dataSnapshot.child("pathPhoto").getValue(String.class);
                        Picasso.get().load(imgUrl).into(holder.imageView);
                        holder.txtNameRoom.setText(title);
                        holder.txtDetail.setText(detail);
                        Log.d("addMinList","This is addmin list item yahhh");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                // String pathPhoto=dataSnapshot.child("user").child(uid).child("pathPhoto").getValue(String.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //myViewHolder.imageView=dataset.get(i).imgGroup;

        // imgUrl = "https://firebasestorage.googleapis.com/v0/b/tangwong-862c9.appspot.com/o/Photos%2F1934229398?alt=media&token=ec82532d-8a0b-4ab4-bb12-a9a6be8189fd";

        //   new PicassoImageGetter()

    }

    @Override
    public int getItemCount() {

        return countOrderNow;
    }
}
