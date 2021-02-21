package com.example.readdit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextInputLayout txtlayoutEmail;
    private TextInputLayout txtlayoutPassword;
    private TextView txtError;
    private ProgressBar pbLoading;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        txtlayoutEmail = findViewById(R.id.signin_email_layout);
        txtlayoutPassword = findViewById(R.id.signin_password_layout);
        txtError = findViewById(R.id.signin_error_txt);
        pbLoading = findViewById(R.id.signin_loading);

        Button btnSignIn = findViewById(R.id.signin_login_btn);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Link to sign up activity
        Button btnRegister = findViewById(R.id.signin_register_btn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {
        pbLoading.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(txtlayoutEmail.getEditText().getText().toString(), txtlayoutPassword.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            pbLoading.setVisibility(View.INVISIBLE);
                            updateUI();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pbLoading.setVisibility(View.INVISIBLE);

                        if (e instanceof FirebaseAuthInvalidCredentialsException ||
                            e instanceof FirebaseAuthInvalidUserException){
                            txtError.setVisibility(View.VISIBLE);
                            txtlayoutEmail.setError("Error");
                            txtlayoutPassword.setError("Error");
                        }
                        else {
                            new AlertDialog.Builder(SignInActivity.this)
                                    .setTitle("Oops")
                                    .setMessage("There was a problem while signing you in, please try again later :(")
                                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                });
    }

    private void updateUI() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        finish();  //Kill the activity from which you will go to next activity
        startActivity(intent);
    }
}