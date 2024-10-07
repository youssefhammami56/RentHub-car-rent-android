package com.example.renthub.Views.RentRV;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renthub.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView,mapicon;
    TextView nameView, StatusView, priceView,maptext;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageview);
        nameView = itemView.findViewById(R.id.name);
        StatusView = itemView.findViewById(R.id.status);
        priceView = itemView.findViewById(R.id.total);
        maptext = itemView.findViewById(R.id.mapclick);
        mapicon = itemView.findViewById(R.id.mapicon);

    }
}
