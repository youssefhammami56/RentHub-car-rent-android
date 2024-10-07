package com.example.renthub.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.renthub.R;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.SQLOutput;

public class PaymentActivity extends AppCompatActivity {
TextView date,location,price,status;
Button gohome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        setContentView(R.layout.activity_payment);
        date = findViewById(R.id.startdate);
        location = findViewById(R.id.pickup);
        price = findViewById(R.id.total);
        status = findViewById(R.id.status);
        gohome = findViewById(R.id.button);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

    db.collection("rents").document(id).get().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                String sdate = document.getString("startDate").toString();
                String slocation = document.getString("location").toString();
                String sprice = document.getString("total").toString();
                String sstatus = document.getString("status").toString();
                String ssdate =sdate.substring(0,10);
                date.setText(ssdate);
                location.setText(slocation);
                price.setText(sprice);
                status.setText(sstatus);
            } else {
                System.out.println("No such document");
            }
        }else {
            System.out.println("get failed with " + task.getException());
        }
    });
        gohome.setOnClickListener(v -> {
            Intent intent1 = new Intent(PaymentActivity.this,HomeActivity.class);
            startActivity(intent1);
            finish();
        });
    }
}