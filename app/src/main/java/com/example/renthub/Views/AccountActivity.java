package com.example.renthub.Views;

import static com.example.renthub.Views.SplashScreenActivity.area;
import static com.example.renthub.Views.SplashScreenActivity.city;
import static com.example.renthub.Views.SplashScreenActivity.country;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renthub.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AccountActivity extends AppCompatActivity {

    TextView CurrentLocation;
    RelativeLayout assistant;
    TextInputLayout username;
    TextInputLayout email;
    TextInputLayout phone;
    Button changePassword;
    ImageButton signOut;
    Button update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        setContentView(R.layout.activity_account);
        CurrentLocation = findViewById(R.id.currentlocation);
        assistant = findViewById(R.id.assistant);
        username = findViewById(R.id.usernameaccount);
        email = findViewById(R.id.emailAccount);
        phone = findViewById(R.id.phoneaccount);

        signOut = findViewById(R.id.signoutbutton);
        changePassword = findViewById(R.id.changepasswordbutton);
        update = findViewById(R.id.updatebuttonaccount);
        Button changePassword = findViewById(R.id.changepasswordbutton);
        ImageButton signOut = findViewById(R.id.signoutbutton);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email1 = user.getEmail();
            String phone1 = user.getPhoneNumber();
            email.getEditText().setText(email1);
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    android.util.Log.d("TAG", "DocumentSnapshot data: " + task.getResult().getData());
                    username.getEditText().setText(task.getResult().getData().get("username").toString());
                    phone.getEditText().setText(task.getResult().getData().get("phone").toString());

                } else {
                    android.util.Log.d("TAG", "get failed with ", task.getException());
                }
            });
        }
        signOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            android.content.Intent intent = new android.content.Intent(AccountActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        changePassword.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(AccountActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });
        update.setOnClickListener(v -> {
            // Validate user input (you can customize this validation as needed)
            if (isValidInput()) {
                // Perform the update operation
                DocumentReference washingtonRef = db.collection("users").document(user.getUid());
                washingtonRef
                        .update(
                                "username", username.getEditText().getText().toString(),
                                "phone", phone.getEditText().getText().toString()
                        )
                        .addOnSuccessListener(aVoid -> {
                            Log.d("TAG", "DocumentSnapshot successfully updated!");
                            // Show a success dialog
                            showAlertDialog("Update Successful", "User information updated successfully.");
                        })
                        .addOnFailureListener(e -> {
                            Log.w("TAG", "Error updating document", e);
                            // Show an error dialog
                            showAlertDialog("Update Failed", "Failed to update user information. Please try again.");
                        });
            } else {
                // Show an error dialog for invalid input
                showAlertDialog("Invalid Input", "Please enter valid information.");
            }
        });
        // Get the current location of the device
        String FullAddress = area+" "+city+" "+country;
        CurrentLocation.setText(FullAddress);
        //Assistant show a dialog
        assistant.setOnClickListener(v -> {
            showAlertDialog2("Assistant", "call us or email us on ltaief.khalil@gmail.com for more assistance");
        });
       //TO DO : UPDATE THE AUTHENTICATED USER DATA
    }
    private void showAlertDialog2(String title, String message) {
        // make a button open intent to call and another button to visit email and specify the email to send to ltaief.khalil@gmail.com .
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Call", (dialog, which) -> {
                    android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_DIAL);
                    intent.setData(android.net.Uri.parse("tel:123456789"));
                    startActivity(intent);
                })
                .setNegativeButton("Email", (dialog, which) -> {
                    android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"ltaief.khalil@gmail.com"});
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "this custommer needs help");
                    intent.putExtra(android.content.Intent.EXTRA_TEXT, "this custommer needs help"+ "\n" + "please contact him");
                    startActivity(intent);
                })
                .show();
    }
    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private boolean isValidInput() {
        return !TextUtils.isEmpty(username.getEditText().getText())
                && !TextUtils.isEmpty(phone.getEditText().getText());
    }
}