package com.raaveinm.homepharmacy.ui.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.raaveinm.homepharmacy.MainActivity;
import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.domain.ManageSharedPreferences;
import com.google.android.material.snackbar.Snackbar;
import com.raaveinm.homepharmacy.ui.Registration;


public class LoginFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        ManageSharedPreferences loginData = new ManageSharedPreferences(requireContext());
        TextView passwordText = requireView().findViewById(R.id.editTextNumberPasswordLogin);
        TextView login = requireView().findViewById(R.id.submitPassword);
        TextView welcome = requireView().findViewById(R.id.welcomeText);

        welcome.setText(getString(R.string.welcome) + " " + loginData.getUsername());

        passwordText.setOnFocusChangeListener((v, hasFocus) -> login.setEnabled(true));

        login.setOnClickListener(view -> {
            if (loginData.getPassword() == Integer.parseInt(passwordText.getText().toString())) {
                loginData.loggedIn();
                Intent intent = new Intent(requireContext(), MainActivity.class);

                intent.setFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP
                );

                startActivity(intent);
            } else {
                Snackbar.make(requireView(), R.string.wrong_password, Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}