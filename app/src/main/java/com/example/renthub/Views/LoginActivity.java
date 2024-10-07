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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button callSignup, login_btn, forgetpassword;
    TextView Welcometext, sloganwelcome;
    LottieAnimationView lottieanimation;
    TextInputLayout username, password;

    private boolean validateEmail() {
        String val = username.getEditText().getText().toString().trim();
        String checkspaces = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            username.setError("Field can not be empty");
            return false;
        } else if (!val.matches(checkspaces)) {
            username.setError("Invalid Email!");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            password.setError("Field can not be empty");
            return false;

        }
        else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        setContentView(R.layout.activity_login);
        lottieanimation = findViewById(R.id.lottieAnimationView);
        Welcometext = findViewById(R.id.welcome);
        sloganwelcome = findViewById(R.id.continueee);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgetpassword = findViewById(R.id.buttonforgetpassword);
        login_btn = findViewById(R.id.buttonlogin);
        callSignup = findViewById(R.id.buttonsignup);

        callSignup.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                android.content.Intent intent = new android.content.Intent(LoginActivity.this, SignupActivity.class);
                Pair[] pairs = new Pair[8];
                pairs[0] = new Pair<android.view.View, String>(findViewById(R.id.lottieAnimationView), "lottie_img");
                pairs[1] = new Pair<android.view.View, String>(findViewById(R.id.welcome), "text_img");
                pairs[2] = new Pair<android.view.View, String>(findViewById(R.id.continueee), "text2_img");
                pairs[3] = new Pair<android.view.View, String>(findViewById(R.id.username), "input1_img");
                pairs[4] = new Pair<android.view.View, String>(findViewById(R.id.password), "input2_img");
                pairs[5] = new Pair<android.view.View, String>(findViewById(R.id.buttonforgetpassword), "button1_img");
                pairs[6] = new Pair<android.view.View, String>(findViewById(R.id.buttonlogin), "button2_img");
                pairs[7] = new Pair<android.view.View, String>(findViewById(R.id.buttonsignup), "button3_img");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, options.toBundle());
            }
        });
        forgetpassword.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                android.content.Intent intent = new android.content.Intent(LoginActivity.this, ForgetPasswordActivity.class);

                Pair[] pairs = new Pair[6];
                pairs[0] = new Pair<android.view.View, String>(findViewById(R.id.lottieAnimationView), "lottie_img");
                pairs[1] = new Pair<android.view.View, String>(findViewById(R.id.welcome), "text_img");
                pairs[2] = new Pair<android.view.View, String>(findViewById(R.id.continueee), "text2_img");
                pairs[3] = new Pair<android.view.View, String>(findViewById(R.id.username), "input1_img");
                pairs[4] = new Pair<android.view.View, String>(findViewById(R.id.buttonlogin), "button2_img");
                pairs[5] = new Pair<android.view.View, String>(findViewById(R.id.buttonsignup), "button3_img");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, options.toBundle());

            }
        });
        login_btn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(android.view.View v) {
                if(!validatePassword()|!validateEmail()){
                    return;
                }
                mAuth.signInWithEmailAndPassword(username.getEditText().getText().toString(), password.getEditText().getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent j = new Intent(LoginActivity.this, AccountActivity.class);
                                    j.putExtra("user", user);
                                    startActivity(j);
                                    finish();
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}