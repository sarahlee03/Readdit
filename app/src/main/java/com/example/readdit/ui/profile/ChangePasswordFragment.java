package com.example.readdit.ui.profile;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.readdit.MainActivity;
import com.example.readdit.R;
import com.example.readdit.ReadditApplication;
import com.example.readdit.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class ChangePasswordFragment extends Fragment {
    private String strEmail;
    private View view;
    private TextInputLayout txtOldPass;
    private TextInputLayout txtNewPass;
    private ProgressBar pbLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Change Password");

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        txtOldPass = view.findViewById(R.id.changepass_old_pass_layout);
        txtNewPass = view.findViewById(R.id.changepass_new_pass_layout);
        pbLoading = view.findViewById(R.id.changepass_loading);

        // Get email
        ReadditApplication.currUser.observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                strEmail = user.getEmail();
            }
        });

        // Save Button
        Button btnSave = view.findViewById(R.id.changepass_save_btn);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFormValid()) {
                    changePassword();
                }
            }
        });

        // Cancel Button
        Button btnCancel = view.findViewById(R.id.changepass_cancel_btn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).popBackStack(R.id.nav_profile, false);
            }
        });

        return view;
    }

    private boolean isFormValid() {
        txtOldPass.setError("");
        txtNewPass.setError("");
        boolean isValid = true;

        if (txtOldPass.getEditText().getText().toString().isEmpty()) {
            isValid = false;
            txtOldPass.setError("You must enter your old password");
        }

        String passPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        String password = txtNewPass.getEditText().getText().toString();
        if(password.isEmpty() || !Pattern.compile(passPattern).matcher(password).matches()) {
            isValid = false;
            txtNewPass.setError("Password must contain at least 8 characters including UPPER and lower case letters and numbers");
        }

        return isValid;
    }

    private void changePassword() {
        pbLoading.setVisibility(View.VISIBLE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.reauthenticate(EmailAuthProvider.getCredential(strEmail, txtOldPass.getEditText().getText().toString()))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(txtNewPass.getEditText().getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        pbLoading.setVisibility(View.INVISIBLE);
                                        if(task.isSuccessful()) {
                                            Toast.makeText(getActivity(), "Password changed!", Toast.LENGTH_SHORT).show();
                                            Navigation.findNavController(view).popBackStack(R.id.nav_profile, false);

                                        }
                                        else {
                                            popErrorDialog();
                                        }
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pbLoading.setVisibility(View.INVISIBLE);

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            txtOldPass.setError("Password is incorrect");
                        }
                        else {
                            popErrorDialog();
                        }
                    }
                });
    }

    private void popErrorDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Oops")
                .setMessage("There was a problem while changing your password, please try again later :(")
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}