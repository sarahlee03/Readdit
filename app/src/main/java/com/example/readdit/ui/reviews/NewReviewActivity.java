package com.example.readdit.ui.reviews;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.readdit.R;
import com.example.readdit.SignUpActivity;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.readdit.R.layout.new_review_activity;

public class NewReviewActivity extends AppCompatActivity {
    private final int TAKE_PHOTO_CODE = 0;
    private final int CHOOSE_GALLERY_CODE = 1;
    final String BOOKS_FOLDER = "books";
    private Button btnSave;
    private Button btnCancel;
    private ImageView bookImage;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new_review_activity);
        btnSave = findViewById(R.id.newreview_save_button);
        btnCancel = findViewById(R.id.newreview_cancel_button);
        bookImage = findViewById(R.id.newreview_book_img);
        mAuth = FirebaseAuth.getInstance();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReview();
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
    }

    private void saveReview() {
        // save image
        Review review = new Review();
        review.id = "1";
        if (bookImage.getDrawable() != null) {
            Bitmap bitMap = ((BitmapDrawable) bookImage.getDrawable()).getBitmap();
            // review.id = the file name
            Model.instance.uploadImage(bitMap, BOOKS_FOLDER, review.id, new Model.AsyncListener<String>() {
                @Override
                // after image saved
                public void onComplete(String data) {
                    // save review with image url - lesson 9 1:15
                }
            });
        }
    }

    private void selectImage() {
        final String TAKE_PHOTO = "Take a photo";
        final String CHOOSE_GALLERY = "Choose from gallery";

        final CharSequence[] options = { TAKE_PHOTO, CHOOSE_GALLERY };
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Choose your book picture")
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
                    if (ContextCompat.checkSelfPermission(NewReviewActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        // Ask for permissions
                        ActivityCompat.requestPermissions(NewReviewActivity.this,
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        bookImage.setImageBitmap((Bitmap) data.getExtras().get("data"));
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
                                bookImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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
                Toast.makeText(NewReviewActivity.this,
                        "Permission denied to upload image from external storage",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

}
