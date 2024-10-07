package com.example.renthub.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.renthub.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    LottieAnimationView lottieAnimationView;
    TextView forget;
    TextView enteremail;
    TextInputLayout email;
    Button reset;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN); // to hide the status bar
        setContentView(R.layout.activity_reset_password);
        lottieAnimationView = findViewById(R.id.lottieAnimationView);
        forget = findViewById(R.id.forget);
        enteremail = findViewById(R.id.enteremailtext);
        email = findViewById(R.id.emailverification);
        reset = findViewById(R.id.reset);

        reset.setOnClickListener(v -> {
            mAuth = FirebaseAuth.getInstance();
            if(email.getEditText().getText().toString().isEmpty()){
                email.setError("Field can not be empty");
                return;
            }else {
                email.setError(null);
                email.setErrorEnabled(false);
            }
            ResetPassword();

        });



    }
    private void ResetPassword(){

        mAuth.sendPasswordResetEmail(email.getEditText().getText().toString())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        android.util.Log.d("TAG", "Email sent.");
                        Toast.makeText(ResetPasswordActivity.this, "Please chack your Email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}