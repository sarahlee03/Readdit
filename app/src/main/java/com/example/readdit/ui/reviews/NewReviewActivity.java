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
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.readdit.MainActivity;
import com.example.readdit.R;
import com.example.readdit.ReadditApplication;
import com.example.readdit.SignUpActivity;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.regex.Pattern;

import static com.example.readdit.R.layout.new_review_activity;

public class NewReviewActivity extends AppCompatActivity {
    NewReviewViewModel viewModel;
    private final int TAKE_PHOTO_CODE = 0;
    private final int CHOOSE_GALLERY_CODE = 1;
    protected boolean imageSelected = false;
    protected Button btnSave;
    protected Button btnCancel;
    protected ImageView bookImage;
    protected EditText book;
    protected EditText author;
    protected Spinner category;
    protected RatingBar rating;
    protected EditText summary;
    protected EditText textReview;
    protected FirebaseAuth mAuth;
    protected ProgressBar busy;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NewReviewViewModel.class);
        setContentView(new_review_activity);
        setTitle("New Review");
        mAuth = FirebaseAuth.getInstance();

        busy = findViewById(R.id.newreview_progress);
        busy.setVisibility(View.GONE);
        btnSave = findViewById(R.id.newreview_save_button);
        btnCancel = findViewById(R.id.newreview_cancel_button);
        bookImage = findViewById(R.id.newreview_book_img);
        book = findViewById(R.id.newreview_bookname_et);
        author = findViewById(R.id.newreview_author_et);
        category = findViewById(R.id.newreview_category_spinner);
        rating = findViewById(R.id.newreview_ratingbar);
        summary = findViewById(R.id.newreview_summary_et);
        textReview = findViewById(R.id.newreview_review_et);

        String[] categories = new String[]{"Fantasy", "Action", "Comedy", "Drama", "Horror", "Romance"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveReview();
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

    protected boolean isFormValid() {
        boolean isValid = true;

        if(!imageSelected) {
            Toast toast = Toast.makeText(NewReviewActivity.this,
                    "Please upload picture of your book",
                    Toast.LENGTH_SHORT);
            toast.show();
            isValid = false;
        }
        if(book.getText().toString().isEmpty()){
            isValid = false;
            book.setError("Book name cannot be empty");
        }
        if(author.getText().toString().isEmpty()){
            isValid = false;
            author.setError("Author name cannot be empty");
        }
        if(((TextView)category.getSelectedView()).getText().toString().isEmpty()){
            isValid = false;
            ((TextView)category.getSelectedView()).setError("Category cannot be empty");
        }
        if(summary.getText().toString().isEmpty()){
            isValid = false;
            summary.setError("Summary cannot be empty");
        }
        if(textReview.getText().toString().isEmpty()){
            isValid = false;
            textReview.setError("Review cannot be empty");
        }

        return isValid;
    }

    protected void busy() {
        busy.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);
        btnCancel.setEnabled(false);
        book.setEnabled(false);
        author.setEnabled(false);
        category.setEnabled(false);
        rating.setEnabled(false);
        summary.setEnabled(false);
        textReview.setEnabled(false);
        bookImage.setEnabled(false);
    }

    private void saveReview() {
        if(!isFormValid()) { return; }
        busy();
        Review review = new Review();
        review.setBook(book.getText().toString());
        review.setAuthor(author.getText().toString());
        review.setCategory(((TextView)category.getSelectedView()).getText().toString());
        review.setRating(rating.getRating());
        review.setSummary(summary.getText().toString());
        review.setReview(textReview.getText().toString());
        // save image
        if (bookImage.getDrawable() != null) {
            Bitmap bitMap = ((BitmapDrawable) bookImage.getDrawable()).getBitmap();
            viewModel.uploadImage(bitMap, ReadditApplication.BOOKS_FOLDER, Model.instance.getCurrentUserID() + "/" + System.currentTimeMillis(), new Model.AsyncListener<String>() {
                @Override
                // after image saved
                public void onComplete(String data) {
                    review.setImage(data);
                    viewModel.getCurrentUser().observe(NewReviewActivity.this, new Observer<User>() {
                        @Override
                        public void onChanged(User user) {
                            if(user != null) {
                                review.setUserId(user.getUserID());
                            }

                            viewModel.addReview(review, new Model.AsyncListener() {
                                @Override
                                public void onComplete(Object data) {
                                    finish();
                                }
                            });
                        }
                    });
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
                        imageSelected = true;
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
                                imageSelected = true;
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
