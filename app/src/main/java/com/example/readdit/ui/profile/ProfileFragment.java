package com.example.readdit.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.readdit.MainActivity;
import com.example.readdit.R;
import com.example.readdit.ReadditApplication;
import com.example.readdit.SignInActivity;
import com.example.readdit.model.Model;
import com.example.readdit.model.Review;
import com.example.readdit.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProfileFragment extends Fragment {
    private User currUser;
    private ProgressBar pbLoading;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
                EditText inputPass = new EditText(getContext());
                inputPass.setHint("Password*");
                inputPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).
                        setTitle("Are you sure?").
                        setMessage("This operation cannot be undone").
                        setView(inputPass).
                        setPositiveButton("Delete account", null)
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setCancelable(true)
                        .show();
                Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(inputPass.getText().toString().isEmpty()) {
                            inputPass.setError("Password is required");
                        }
                        else {
                            AuthCredential cred = EmailAuthProvider.getCredential(txtEmail.getText().toString(), inputPass.getText().toString());
                            mAuth.getCurrentUser().reauthenticate(cred)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    dialog.dismiss();
                                    deleteUser();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                        inputPass.setError("Password is incorrect");
                                    }
                                    else {
                                        dialog.dismiss();
                                        popErrorDialog();
                                    }
                                }
                            });

                        }
                    }
                });
            }
        });

        return view;
    }

    private void deleteUser() {
        pbLoading.setVisibility(View.VISIBLE);

        mAuth.getCurrentUser().delete()
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Model.instance.getReviewsListByUID(currUser.getUserID(), new Model.AsyncListener<List<Review>>() {
                    @Override
                    public void onComplete(List<Review> data) {
                        for (Review review: data) {
                            Model.instance.deleteReview(review, new Model.AsyncListener() {
                                @Override
                                public void onComplete(Object data) {}
                            });
                        }

                        ReadditApplication.currUser.removeObservers(getViewLifecycleOwner());
                        ReadditApplication.currUser.removeObservers(getActivity());
                        Model.instance.deleteUser(currUser, new Model.AsyncListener<Boolean>() {
                            @Override
                            public void onComplete(Boolean data) {
                                pbLoading.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getActivity(), SignInActivity.class);
                                getActivity().finish();  //Kill the activity from which you will go to next activity
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbLoading.setVisibility(View.INVISIBLE);
                popErrorDialog();
            }
        });
    }

    private void popErrorDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setTitle("Oops")
                .setMessage("here was a problem while deleting your account, please try again later :(")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}