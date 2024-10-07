package com.example.renthub.Views;

import static com.example.renthub.Views.SplashScreenActivity.country;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renthub.R;

import com.example.renthub.Views.Models.AvailabiltyDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RentCarActivity extends AppCompatActivity {
    ImageView imageView;
    CalendarView calendarView, calendarView2;
    Spinner spinner;
    TextView price;
    Button confirm;
    public static String selectedDate1;
    public static String selectedDate2;
    public static String pricee;
    public static int total;
    private boolean isDate1Selected = false;
    private boolean isDate2Selected = false;
    private static String carname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent_car);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        imageView = findViewById(R.id.imageView2);
        calendarView = findViewById(R.id.calendarView3);
        calendarView2 = findViewById(R.id.calendarView4);
        price = findViewById(R.id.price);
        confirm = findViewById(R.id.confirm);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.location,
                android.R.layout.simple_spinner_dropdown_item);
        //link the adapter to the spinner
        spinner = findViewById(R.id.spinner);
        spinner.setAdapter(adapter);


        Intent intent = getIntent();
        String uid = intent.getStringExtra("DocumentUID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println(uid);
        DocumentReference docRef = db.collection("cars").document(uid);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    pricee = document.getString("price");

                    carname = document.getString("name");
                    price.setText(pricee);

                } else {
                    System.out.println("No such document");
                }
            } else {
                System.out.println("get failed with " + task.getException());
            }
        });
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                selectedDate1 = String.format("%02d-%02d-%04d %02d:%02d:%02d", dayOfMonth, month + 1, year, 23, 59, 59);
                Toast.makeText(RentCarActivity.this, "Selected Date 1: " + selectedDate1, Toast.LENGTH_SHORT).show();
                isDate1Selected = true;
            }
        });

        calendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                selectedDate2 = String.format("%02d-%02d-%04d %02d:%02d:%02d", dayOfMonth, month + 1, year, 23, 59, 59);
                Toast.makeText(RentCarActivity.this, "Selected Date 2: " + selectedDate2, Toast.LENGTH_SHORT).show();
                isDate2Selected = true;
            }
        });

        confirm.setOnClickListener(v -> {
            if (isDate1Selected && isDate2Selected && (spinner != null)) {


                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                    Date date1 = sdf.parse(selectedDate1);
                    Date date2 = sdf.parse(selectedDate2);
                    long difference_In_Time = date2.getTime() - date1.getTime();
                    long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
                    int pricee = Integer.parseInt(price.getText().toString());
                    total = (int) (difference_In_Days * pricee);
                    // Proceed to the next activity for payment, passing the total amount


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(RentCarActivity.this);
                builder.setTitle("Confirm Payment");
                builder.setMessage("Are you sure you want to proceed with the payment?");
                if (!country.equals("Tunisia")&& !country.equals("Tunisie")&& !country.equals("United States")&& !country.equals("États-Unis")) {
                    //set euro
                    total = (int) (total * 0.84);
                    //add euro symbol
                    String stotale = String.valueOf(total);
                    stotale = stotale + "€";
                    builder.setMessage("Are you sure you want to proceed with the payment? \n Total: " + stotale);
                }else if(country.equals("Tunisia")||country.equals("Tunisie")){
                    String stotale = String.valueOf(total);
                    stotale = stotale + "DT";
                    builder.setMessage("Are you sure you want to proceed with the payment? \n Total: " + stotale);

                }else if (country.equals("United States")||country.equals("États-Unis")){
                    //set dollar
                    total = (int) (total * 1.21);
                    String stotale = String.valueOf(total);
                    stotale = stotale + "$";
                    builder.setMessage("Are you sure you want to proceed with the payment? \n Total: " + stotale);


                }

                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // User clicked Yes button
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        Date date1 = sdf.parse(selectedDate1);
                        Date date2 = sdf.parse(selectedDate2);
                        long difference_In_Time = date2.getTime() - date1.getTime();
                        long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
                        int pricee = Integer.parseInt(price.getText().toString());
                        int total = (int) (difference_In_Days * pricee);
                        String stotale = String.valueOf(total);
                        //add new rent in firestore firebase
                        Map<String, Object> NewRent = new HashMap<>();
                        NewRent.put("carUID", uid);
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        NewRent.put("userID", user.getUid().toString()); //
                        NewRent.put("startDate", selectedDate1);
                        NewRent.put("endDate", selectedDate2);
                        NewRent.put("total", stotale);
                        NewRent.put("location", spinner.getSelectedItem().toString());
                        NewRent.put("status", "pending");
                        NewRent.put("carName", carname);
                        // make a random code from 20 characters and store it in a String variable:
                        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                + "0123456789"
                                + "abcdefghijklmnopqrstuvxyz";
                        StringBuilder sb = new StringBuilder(20);
                        for (int i = 0; i < 20; i++) {
                            int index
                                    = (int) (AlphaNumericString.length()
                                    * Math.random());
                            sb.append(AlphaNumericString
                                    .charAt(index));
                        }
                        NewRent.put("id", sb.toString());
                        db.collection("rents").document(sb.toString())
                                .set(NewRent).addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RentCarActivity.this, "Rent added successfully", Toast.LENGTH_SHORT).show();
                                    Intent j = new Intent(RentCarActivity.this, PaymentActivity.class);
                                    j.putExtra("id", sb.toString());
                                    j.putExtra("total", total);
                                    startActivity(j);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RentCarActivity.this, "Failed to add rent", Toast.LENGTH_SHORT).show();
                                });
                        // Proceed to the next activity for payment, passing the total amount


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                });

                builder.setNegativeButton("No", (dialog, which) -> {
                    // User clicked No button
                    dialog.dismiss();
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            } else {
                Toast.makeText(RentCarActivity.this, "Please select both dates", Toast.LENGTH_SHORT).show();
            }
        });

    }

}