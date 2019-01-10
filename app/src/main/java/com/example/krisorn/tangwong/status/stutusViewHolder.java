package com.example.krisorn.tangwong.status;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.krisorn.tangwong.R;

import org.w3c.dom.Text;

public class stutusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtNameRoom, txtSumPrice,txtStatus,txtNumberOfItem;

    public stutusViewHolder(@NonNull View itemView) {
        super(itemView);

        txtNameRoom= (TextView)itemView.findViewById(R.id.name_room);
        txtNumberOfItem=(TextView)itemView.findViewById(R.id.NumberOfItem);
        txtStatus= (TextView)itemView.findViewById(R.id.status);
        txtSumPrice= (TextView)itemView.findViewById(R.id.sumPice);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


    }
}
