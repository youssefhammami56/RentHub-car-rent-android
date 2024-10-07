package com.example.renthub.Views.RentRV;

import static com.example.renthub.Views.SplashScreenActivity.city;
import static com.example.renthub.Views.SplashScreenActivity.country;
import static com.example.renthub.Views.SplashScreenActivity.lalltitude;
import static com.example.renthub.Views.SplashScreenActivity.longtitude;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renthub.R;
import com.example.renthub.Views.Models.Rent;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    List<Rent> rents;

    public MyAdapter(Context context, List<Rent> rents) {
        this.context = context;
        this.rents = rents;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.lease_rv_item_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(rents.get(position).getCarName());
        holder.StatusView.setText(rents.get(position).getStatus());
        holder.priceView.setText(rents.get(position).getTotal());
        String status = rents.get(position).getStatus();
        if (status.equals("pending")) {
            holder.StatusView.setTextColor(context.getResources().getColor(R.color.yellow));
            holder.imageView.setImageResource(R.drawable.pending);
        } else if (status.equals("approved")) {
            holder.StatusView.setTextColor(context.getResources().getColor(R.color.green));
            holder.imageView.setImageResource(R.drawable.accepted);
        } else if (status.equals("rejected")) {
            holder.StatusView.setTextColor(context.getResources().getColor(R.color.red));
            holder.imageView.setImageResource(R.drawable.rejected);
        }
        if(status.equals("approved")){
           holder.maptext.setText("Click to view map");
              holder.mapicon.setImageResource(R.drawable.map);
        }
        if(status.equals("approved")){
            holder.itemView.setOnClickListener(v -> {
               if(city.equals("")){
                   Toast.makeText(context,"city not found",Toast.LENGTH_SHORT).show();
               }else if (country.equals("")){
                   Toast.makeText(context,"city not found",Toast.LENGTH_SHORT).show();
               }
               else{
                   String destination = rents.get(position).getLocation();
                   //Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/"+city+","+country+"/"+destination);
                   //use lat and long
                     Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/"+lalltitude+","+longtitude+"/"+destination);
                   Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                     mapIntent.setPackage("com.google.android.apps.maps");
                     mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     context.startActivity(mapIntent);

               }
            });
        }
    }

    @Override
    public int getItemCount() {
        return rents.size();
    }
}
