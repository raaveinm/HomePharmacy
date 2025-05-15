package com.raaveinm.homepharmacy.ui.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.databinding.FragmentProfileBinding;
import com.raaveinm.homepharmacy.domain.ManageSharedPreferences;
import com.raaveinm.homepharmacy.ui.Registration;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private TextView profileName;
    private Button changePassword;
    private Button logOut;
    private EditText editTextNumberPassOld;
    private EditText editTextNumberPassNew;
    private EditText editTextNumberPassConf;
    private Button confirmPassword;
    private LinearLayout changePasswordLayout;
    private ManageSharedPreferences loginData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        profileName = binding.profileName;
        changePassword = binding.changePassword;
        logOut = binding.logOut;
        changePasswordLayout = binding.changePasswordLayout;
        editTextNumberPassOld = binding.editTextNumberPassOld;
        editTextNumberPassNew = binding.editTextNumberPassNew;
        editTextNumberPassConf = binding.editTextNumberPassConf;
        confirmPassword = binding.confirmPassword;
        loginData = new ManageSharedPreferences(requireContext());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        String username = loginData.getUsername();

        profileName.setText(getString(R.string.about_me) + " " + username);
        changePassword.setOnClickListener(v -> changePassword());
        logOut.setOnClickListener(v -> logout());
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }

    private void changePassword() {
        changePasswordLayout.setVisibility(View.VISIBLE);
        changePassword.setVisibility(View.GONE);
        logOut.setVisibility(View.GONE);
        confirmPassword.setEnabled(false);

        editTextNumberPassConf.setOnFocusChangeListener(
                (v, hasFocus) -> confirmPassword.setEnabled(true)
        );

        confirmPassword.setOnClickListener(v -> {

            int oldPassword = Integer.parseInt(editTextNumberPassOld.getText().toString());
            int newPassword = Integer.parseInt(editTextNumberPassNew.getText().toString());
            int confPassword = Integer.parseInt(editTextNumberPassConf.getText().toString());

            if (oldPassword == loginData.getPassword()) {
                if (newPassword == confPassword) {
                    loginData.changePassword(newPassword);
                    confirmPassword.setEnabled(true);
                    changePasswordLayout.setVisibility(View.GONE);
                    changePassword.setVisibility(View.VISIBLE);
                    logOut.setVisibility(View.VISIBLE);
                    Snackbar.make(
                            requireView(),
                            R.string.password_changed,
                            Snackbar.LENGTH_SHORT
                    ).show();
                } else {
                    Snackbar.make(
                            requireView(),
                            R.string.password_mismatch,
                            Snackbar.LENGTH_SHORT
                    ).show();
                }
            } else {
                Snackbar.make(
                        requireView(),
                        R.string.wrong_password,
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void logout() {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.log_out_confirm_title)
                .setMessage(R.string.log_out_confirm_message)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    loginData.loggedOut();
                    loginData.deleteData();
                    Intent intent = new Intent(getContext(), Registration.class);
                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );
                    startActivity(intent);
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}