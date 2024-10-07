package com.example.renthub.Views;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.renthub.R;
import com.example.renthub.Views.Models.Rent;
import com.example.renthub.Views.RentRV.MyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LeaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        setContentView(R.layout.activity_lease);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Rent> rents = new ArrayList<Rent>();
        MyAdapter myAdapter = new MyAdapter(this, rents);
        recyclerView.setAdapter(myAdapter);

        db.collection("rents")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (com.google.firebase.firestore.QueryDocumentSnapshot document : task.getResult()) {
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            System.out.println(mAuth.getCurrentUser().getUid());
                            String uid = mAuth.getCurrentUser().getUid();
                            if (document.getString("userID").equals(uid)) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String total = document.getString("total");
                                String status = document.getString("status");
                                String carUID = document.getString("carUID");
                                String userUID = document.getString("userUID");
                                String start = document.getString("start");
                                String end = document.getString("end");
                                String location = document.getString("location");
                                String id = document.getId();
                                String carName = document.getString("carName");
                                Rent rent = new Rent(id, carUID, userUID, start, end, total, location, status, carName);
                                rents.add(rent);
                                myAdapter.notifyDataSetChanged();
                            }

                        }
                        // Notify data changed after adding all items

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }
}
