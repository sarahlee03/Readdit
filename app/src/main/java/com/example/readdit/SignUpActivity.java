package com.example.readdit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.readdit.model.Model;
import com.example.readdit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    final int TAKE_PHOTO_CODE = 0;
    final int CHOOSE_GALLERY_CODE = 1;
    boolean wasImageSelected = false;
    private FirebaseAuth mAuth;
    private TextInputLayout txtlayoutName;
    private TextInputLayout txtlayoutEmail;
    private TextInputLayout txtlayoutPassword;
    private ImageButton profileImage;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        txtlayoutName = findViewById(R.id.signup_name_layout);
        txtlayoutEmail = findViewById(R.id.signup_email_layout);
        txtlayoutPassword = findViewById(R.id.signup_password_layout);
        profileImage = findViewById(R.id.signup_profile_img);
        pbLoading = findViewById(R.id.signup_loading);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        Button btnSignUp = findViewById(R.id.signup_register_btn);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormValid()) {
                    createAccount();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        profileImage.setImageBitmap((Bitmap) data.getExtras().get("data"));
                        wasImageSelected = true;
                    }
                    break;
                case CHOOSE_GALLERY_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        if (selectedImage != null) {
                            Cursor cursor = this.getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                wasImageSelected = true;
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                startActivityForResult(pickPhoto, CHOOSE_GALLERY_CODE);
            }
            else {
                // Permission denied
                Toast.makeText(SignUpActivity.this,
                        "Permission denied to upload image from external storage",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImage() {
        final String TAKE_PHOTO = "Take a photo";
        final String CHOOSE_GALLERY = "Choose from gallery";

        final CharSequence[] options = { TAKE_PHOTO, CHOOSE_GALLERY};
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Choose a profile picture:")
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int item) {
                if (options[item].equals(TAKE_PHOTO)) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_PHOTO_CODE);
                }
                else if (options[item].equals(CHOOSE_GALLERY)) {
                    //Check for permissions
                    if (ContextCompat.checkSelfPermission(SignUpActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        // Ask for permissions
                        ActivityCompat.requestPermissions(SignUpActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                    }
                    else {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                        pickPhoto.setType("image/*");
                        startActivityForResult(pickPhoto, CHOOSE_GALLERY_CODE);
                    }
                }
            }
        });

        builder.show();
    }

    private boolean isFormValid() {
        txtlayoutName.setError("");
        txtlayoutEmail.setError("");
        txtlayoutPassword.setError("");
        boolean isValid = true;

        if(!wasImageSelected) {
            Toast.makeText(SignUpActivity.this,
                    "Please choose a profile picture",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(txtlayoutName.getEditText().getText().toString().isEmpty()){
            isValid = false;
            txtlayoutName.setError("Full name cannot be empty");
        }

        String email = txtlayoutEmail.getEditText().getText().toString();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
            txtlayoutEmail.setError("Please enter a valid email address");
        }

        String passPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        String password = txtlayoutPassword.getEditText().getText().toString();
        if(password.isEmpty() || !Pattern.compile(passPattern).matcher(password).matches()) {
            isValid = false;
            txtlayoutPassword.setError("Password must contain at least 8 characters including UPPER and lower case letters and numbers");
        }

        return isValid;
    }

    private void createAccount() {
        pbLoading.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(txtlayoutEmail.getEditText().getText().toString(), txtlayoutPassword.getEditText().getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveUser();
                            pbLoading.setVisibility(View.INVISIBLE);
                            finish();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pbLoading.setVisibility(View.INVISIBLE);
                        if (e instanceof FirebaseAuthUserCollisionException){
                            txtlayoutEmail.setError(e.getMessage());
                        }
                        else {
                            new AlertDialog.Builder(SignUpActivity.this)
                                    .setTitle("Oops")
                                    .setMessage("There was a problem while signing you up, please try again later :(")
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

    private void saveUser() {
        if (profileImage.getDrawable() != null) {
            Bitmap bitMap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
            Model.instance.uploadImage(bitMap, mAuth.getCurrentUser().getUid(), new Model.AsyncListener<String>() {
                @Override
                public void onComplete(String data) {
                    if (data != null) {
                        User user = new User(mAuth.getCurrentUser().getUid(),
                                txtlayoutName.getEditText().getText().toString(),
                                txtlayoutEmail.getEditText().getText().toString(),
                                data);
                        Model.instance.addUser(user,
                                data1 -> {
                                    Model.instance.getAllUsers();
                                });
                    }
                }
            });
        }
    }
}