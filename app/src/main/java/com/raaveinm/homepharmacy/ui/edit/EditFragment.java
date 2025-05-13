package com.raaveinm.homepharmacy.ui.edit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.raaveinm.homepharmacy.databinding.FragmentDashboardBinding;


public class EditFragment extends Fragment {
    private FragmentDashboardBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }



    @Override
    public void onDestroyView() { super.onDestroyView();binding = null; }
}