package com.example.readdit.ui.profile;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.readdit.MainActivity;
import com.example.readdit.R;
import com.example.readdit.ReadditApplication;
import com.example.readdit.model.Model;
import com.example.readdit.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class EditInfoFragment extends Fragment {
    private final int TAKE_PHOTO_CODE = 0;
    private final int CHOOSE_GALLERY_CODE = 1;
    private String formerName;
    private boolean wasImageSelected = false;
    private View view;
    private ImageView imgProfile;
    private TextInputLayout txtName;
    private TextInputLayout txtEmail;
    private ProgressBar pbLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Edit Info");
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_info, container, false);
        txtName = view.findViewById(R.id.editinfo_name_layout);
        txtEmail = view.findViewById(R.id.editinfo_email_layout);
        imgProfile = view.findViewById(R.id.editinfo_profile_img);
        pbLoading = view.findViewById(R.id.editinfo_loading);
        FloatingActionButton btnEditImg = view.findViewById(R.id.editinfo_edit_img_btn);

        // Update user details
        ReadditApplication.currUser.observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                formerName = user.getFullName();
                txtName.getEditText().setText(user.getFullName());
                txtEmail.getEditText().setText(user.getEmail());
                Picasso.get().load(user.getImageUri()).placeholder(R.drawable.profile_placeholder).into(imgProfile);
            }
        });

        btnEditImg.setColorFilter(Color.WHITE);
        btnEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        // Save Button
        Button btnSave = view.findViewById(R.id.editinfo_save_btn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFormValid()) {
                    saveUser();
                }
            }
        });

        // Cancel Button
        Button btnCancel = view.findViewById(R.id.editinfo_cancel_btn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack(R.id.nav_profile, false);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        imgProfile.setImageBitmap((Bitmap) data.getExtras().get("data"));
                        wasImageSelected = true;
                    }
                    break;
                case CHOOSE_GALLERY_CODE:
                    if (resultCode == Activity.RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                imgProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                wasImageSelected = true;
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    private void selectImage() {
        final String TAKE_PHOTO = "Take a photo";
        final String CHOOSE_GALLERY = "Choose from gallery";

        final CharSequence[] options = { TAKE_PHOTO, CHOOSE_GALLERY};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
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
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                        // Ask for permissions
                        ActivityCompat.requestPermissions(getActivity(),
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


    private boolean isFormValid(){
        txtName.setError("");
        boolean isValid = true;

        if (txtName.getEditText().getText().toString().isEmpty()) {
            isValid = false;
            txtName.setError("Full name cannot be empty");
        }

        return isValid;
    }

    private void saveUser() {
        String strName = txtName.getEditText().getText().toString();

        // Check if data was changed
        if (wasImageSelected || !formerName.equals(strName)) {
            pbLoading.setVisibility(View.VISIBLE);
            if (imgProfile.getDrawable() != null) {
                Bitmap bitMap = ((BitmapDrawable) imgProfile.getDrawable()).getBitmap();
                Model.instance.uploadImage(bitMap, ReadditApplication.PROFILES_FOLDER, Model.instance.getCurrentUserID(), new Model.AsyncListener<String>() {
                    @Override
                    public void onComplete(String data) {
                        if (data != null) {
                            User user = new User(Model.instance.getCurrentUserID(),
                                    strName,
                                    txtEmail.getEditText().getText().toString(),
                                    data,
                                    false);
                            Model.instance.addUser(user,
                                    data1 -> {
                                        Toast.makeText(getActivity(),
                                                "Saved!",
                                                Toast.LENGTH_SHORT).show();
                                        Model.instance.refreshAllUsers(new Model.AsyncListener() {
                                            @Override
                                            public void onComplete(Object data) {
                                                ReadditApplication.setCurrentUser(Model.instance.getUserById(Model.instance.getCurrentUserID()));
                                                pbLoading.setVisibility(View.INVISIBLE);
                                                Navigation.findNavController(view).popBackStack(R.id.nav_profile, false);
                                            }
                                        });
                                    });
                        }
                    }
                });
            }
        }
        else {
            Navigation.findNavController(view).popBackStack(R.id.nav_profile, false);
        }
    }
}