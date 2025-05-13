package com.raaveinm.homepharmacy.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.raaveinm.homepharmacy.data.Item;
import com.raaveinm.homepharmacy.data.ItemDao;
import com.raaveinm.homepharmacy.data.ItemDatabase;
import com.raaveinm.homepharmacy.databinding.FragmentHomeBinding;
import com.raaveinm.homepharmacy.domain.RecyclerViewAdapter;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerViewAdapter recyclerViewAdapter;

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewAdapter = new RecyclerViewAdapter();
        binding.viewFragmentRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.viewFragmentRecycleView.setAdapter(recyclerViewAdapter);

        ItemDatabase db = Room.databaseBuilder(
                requireContext().getApplicationContext(),
                ItemDatabase.class,
                "database-name"
        ).build();

        ItemDao itemDao = db.itemDao();

        itemDao.getAllItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                recyclerViewAdapter.dataChanged(items);
                for (Item item : items) {
                    System.out.println("Item Fetched: " + item.itemName);
                }
            }
        });
    }

    @Override public void onDestroyView() { super.onDestroyView(); binding = null; }
}