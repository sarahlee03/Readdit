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
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {
    final int TAKE_PHOTO_CODE = 0;
    final int CHOOSE_GALLERY_CODE = 1;
    boolean wasImageSelected = false;
    TextView txtFullName;
    TextView txtEmail;
    TextView txtPassword;
    ImageButton profileImage;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        txtFullName = findViewById(R.id.signup_name_input);
        txtEmail = findViewById(R.id.signup_email_input);
        txtPassword = findViewById(R.id.signup_password_input);
        profileImage = findViewById(R.id.signup_profile_img);
        btnSignUp = findViewById(R.id.signup_register_btn);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
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

    private boolean validateForm() {
        boolean isValid = true;

        if(!wasImageSelected) {
            Toast.makeText(SignUpActivity.this,
                    "Please choose a profile picture",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        if(txtFullName.getText().toString().isEmpty()){
            isValid = false;
            txtFullName.setError("Full name cannot be empty");
        }

        String email = txtEmail.getText().toString();
        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            isValid = false;
            txtEmail.setError("Please enter a valid email address");
        }

        String passPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        String password = txtPassword.getText().toString();
        if(password.isEmpty() || !Pattern.compile(passPattern).matcher(password).matches()) {
            isValid = false;
            txtPassword.setError("Password must contain at least 8 characters including UPPER and lower case letters and numbers");
        }

        if (!isValid) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please fill out the form correctly")
                    .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }

        return isValid;
    }
}