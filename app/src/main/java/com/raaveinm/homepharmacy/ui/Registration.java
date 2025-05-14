package com.raaveinm.homepharmacy.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.raaveinm.homepharmacy.R;
import com.raaveinm.homepharmacy.domain.ManageSharedPreferences;
import com.raaveinm.homepharmacy.ui.login.LoginFragment;
import com.raaveinm.homepharmacy.ui.login.RegistrationFragment;

import java.util.Objects;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Objects.requireNonNull(getSupportActionBar()).hide();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ManageSharedPreferences loginData = new ManageSharedPreferences(this);

        if (loginData.getUsername().equals("ERR_USER_DOES_NOT_EXIST")) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_fragment_container, new RegistrationFragment())
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.registration_fragment_container, new LoginFragment())
                    .commit();
        }
    }
}