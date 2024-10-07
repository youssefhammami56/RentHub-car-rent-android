package com.example.renthub.Views;

import static com.example.renthub.Views.SplashScreenActivity.country;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.renthub.R;
import com.example.renthub.Views.Models.DynamicRVModel;
import com.example.renthub.Views.Models.StaticRvModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ImageView imageView,lease;
    private RecyclerView recyclerView;
    private StaticRvAdapter staticRvAdapter;
    List<DynamicRVModel> items =  new ArrayList();
    DynamicRVAdapter dynamicRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        setContentView(R.layout.activity_home);
        ImageView imageView = findViewById(R.id.profile3);
        ArrayList<StaticRvModel> item = new ArrayList<>();
        item.add(new StaticRvModel(R.drawable.suv, "SUV"));
        item.add(new StaticRvModel(R.drawable.convertible, "CONVERTIBLE"));
        item.add(new StaticRvModel(R.drawable.coupe, "COUPE"));
        item.add(new StaticRvModel(R.drawable.hatchback, "HATCHBACK"));
        item.add(new StaticRvModel(R.drawable.sedan, "SEDAN"));
        item.add(new StaticRvModel(R.drawable.sport, "SPORTS"));
        item.add(new StaticRvModel(R.drawable.truck, "TRUCK"));
        item.add(new StaticRvModel(R.drawable.van, "VAN"));
        item.add(new StaticRvModel(R.drawable.pickup, "PICKUP"));
        item.add(new StaticRvModel(R.drawable.mini_bus, "MINI_BUS"));

      recyclerView = findViewById(R.id.rv_1);
        staticRvAdapter = new StaticRvAdapter(item);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(staticRvAdapter);



        RecyclerView drv = findViewById(R.id.rv_2);
        drv.setLayoutManager(new LinearLayoutManager(this));
        dynamicRVAdapter = new DynamicRVAdapter(drv, this, items);
        drv.setAdapter(dynamicRVAdapter);
        // Set up click listener for static RecyclerView items
        staticRvAdapter.setOnItemClickListener(new StaticRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(StaticRvModel item) {
                addItemToDynamicRecyclerView(item);
            }
        });
        imageView.setOnClickListener(v -> {
            Intent j = new Intent(HomeActivity.this, AccountActivity.class);
            startActivity(j);
        });
        lease = findViewById(R.id.lease);
        lease.setOnClickListener(v -> {
            Intent j = new Intent(HomeActivity.this, LeaseActivity.class);
            startActivity(j);
        });


    }

    private void addItemToDynamicRecyclerView(StaticRvModel staticRvModel) {
        //clear items first
        dynamicRVAdapter.clear();
        dynamicRVAdapter.notifyDataSetChanged();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cars")
                .whereEqualTo("type", staticRvModel.getText())
                   .whereEqualTo("availability", "yes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    private static final String TAG = "TAG";

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            items.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String price = document.getString("price");
                                String dinarprice = document.getString("price");
                                if (!country.equals("Tunisia")&& !country.equals("Tunisie")&& !country.equals("United States")&& !country.equals("États-Unis")) {
                                    //set euro
                                    price = String.valueOf(Integer.parseInt(price) * 0.84);
                                    //add euro symbol
                                    price = price + "€";
                                }else if(country.equals("Tunisia")||country.equals("Tunisie")){
                                    price = price + "DT";
                                }else if (country.equals("United States")||country.equals("États-Unis")){
                                    //set dollar
                                    price = String.valueOf(Integer.parseInt(price) * 1.21);
                                    price = price + "$";
                                }
                                String duid = document.getId();
                                DynamicRVModel item = new DynamicRVModel(name,price,duid);
                                items.add(item);
                            }
                            dynamicRVAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                            Toast.makeText(HomeActivity.this, "No Documents: ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
