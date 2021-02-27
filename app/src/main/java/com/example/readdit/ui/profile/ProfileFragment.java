package com.example.readdit.ui.profile;

import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.readdit.R;
import com.example.readdit.ReadditApplication;
import com.example.readdit.SignInActivity;
import com.example.readdit.model.Model;
import com.example.readdit.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private User currUser;
    private ProgressBar pbLoading;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        TextView txtName = view.findViewById(R.id.profile_name_txt);
        TextView txtEmail = view.findViewById(R.id.profile_email_txt);
        ImageView imgProfile = view.findViewById(R.id.profile_profile_img);
        pbLoading = view.findViewById(R.id.profile_loading);

        // Update user details
        ReadditApplication.currUser.observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currUser = user;
                txtName.setText(user.getFullName());
                txtEmail.setText(user.getEmail());
                Picasso.get().load(user.getImageUri()).placeholder(R.drawable.profile_placeholder).into(imgProfile);
            }
        });

        // Navigate to change password
        Button btnChangePass = view.findViewById(R.id.profile_change_pass_btn);
        btnChangePass.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_profile_to_changePasswordFragment));

        // Navigate to edit info
        Button btnEditInfo = view.findViewById(R.id.profile_edit_info_btn);
        btnEditInfo.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_profile_to_editInfoFragment));

        // Delete account
        Button btnDelete = view.findViewById(R.id.profile_delete_account);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity()).
                        setTitle("Are you sure?").
                        setMessage("This operation cannot be undone").
                        setPositiveButton("Delete account", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteUser();
                            }
                        }).setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setCancelable(true)
                        .show();
            }
        });

        return view;
    }

    private void deleteUser() {
        pbLoading.setVisibility(View.VISIBLE);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.getCurrentUser().delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Model.instance.deleteUser(currUser, new Model.AsyncListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean data) {
                                pbLoading.setVisibility(View.INVISIBLE);
                                ReadditApplication.currUser.removeObservers(getActivity());
                                Intent intent = new Intent(getActivity(), SignInActivity.class);
                                getActivity().finish();  //Kill the activity from which you will go to next activity
                                startActivity(intent);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pbLoading.setVisibility(View.INVISIBLE);
                        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                                .setTitle("Oops")
                                .setMessage("There was a problem while deleting your account, please try again later :(")
                                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .show();
                    }
                });
    }
}