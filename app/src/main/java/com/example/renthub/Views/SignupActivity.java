package com.example.renthub.Views;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.renthub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    LottieAnimationView lottieanimation;
    TextView Welcometext, sloganwelcome;
    TextInputLayout username, password, phone, email;
    Button callLogin, signup_btn;
    private boolean validateFullName() {
        String val = username.getEditText().getText().toString();
        String checkspaces = "\\A\\w{1,20}\\z";

        if (val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (val.length() > 20) {
            username.setError("Username is too large!");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("No White spaces are allowed!");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkspaces)) {
            email.setError("Invalid Email!");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePhoneNo() {
        String val = phone.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            phone.setError("Field can not be empty");
            return false;
        } else if (val.length() > 8) {
            phone.setError("Phone Number is too large!");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();
        String checkPassword = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{8,}" +               //at least 8 characters
                "$";
        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkPassword)) {
            password.setError("Password should contain 8 characters! at least 1 digit, 1 lower case letter, 1 upper case letter, 1 special character");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    public void registerUser(String uid){
        if(!validateFullName()|!validatePassword()|!validatePhoneNo()|!validateEmail()){
            return;
        }



    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        setContentView(R.layout.activity_signup);
        lottieanimation = findViewById(R.id.lottieAnimationView);
        Welcometext = findViewById(R.id.welcome);
        sloganwelcome = findViewById(R.id.continuee);
        username = findViewById(R.id.fullname);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        signup_btn = findViewById(R.id.buttongo);
        callLogin = findViewById(R.id.buttonlogin);
        callLogin.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                android.content.Intent intent = new android.content.Intent(SignupActivity.this, LoginActivity.class);
                Pair[] pairs = new Pair[8];
                pairs[0] = new Pair<android.view.View, String>(findViewById(R.id.lottieAnimationView), "lottie_img");
                pairs[1] = new Pair<android.view.View, String>(findViewById(R.id.welcome), "text_img");
                pairs[2] = new Pair<android.view.View, String>(findViewById(R.id.continuee), "text2_img");
                pairs[3] = new Pair<android.view.View, String>(findViewById(R.id.fullname), "input1_img");
                pairs[4] = new Pair<android.view.View, String>(findViewById(R.id.password), "input2_img");
                pairs[5] = new Pair<android.view.View, String>(findViewById(R.id.phone), "input3_img");
                pairs[6] = new Pair<android.view.View, String>(findViewById(R.id.email), "input4_img");
                pairs[7] = new Pair<android.view.View, String>(findViewById(R.id.buttongo), "button1_img");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignupActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
        signup_btn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                mAuth.createUserWithEmailAndPassword(email.getEditText().getText().toString(), password.getEditText().getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    // Create a new user with a first and last name
                                    Map<String, Object> Newuser = new HashMap<>();
                                    Newuser.put("username", username.getEditText().getText().toString());
                                    Newuser.put("email", email.getEditText().getText().toString());
                                    Newuser.put("phone", phone.getEditText().getText().toString());
                                    Newuser.put("password", password.getEditText().getText().toString());
                                    Newuser.put("uid", user.getUid());

                                    // Add a new document with a generated ID
                                    db.collection("users").document(user.getUid())
                                            .set(Newuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot added with ID: ");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });
                                    Intent j = new Intent(SignupActivity.this, AccountActivity.class);
                                    j.putExtra("user", user);
                                    startActivity(j);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

    }



}
